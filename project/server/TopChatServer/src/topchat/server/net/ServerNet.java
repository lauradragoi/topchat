/**
    TopChatServer 
    Copyright (C) 2009 Laura Dragoi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package topchat.server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import topchat.server.defaults.DefaultConnectionManager;
import topchat.server.defaults.DefaultContext;
import topchat.server.interfaces.Net;
import topchat.server.interfaces.NetMediator;
import topchat.server.interfaces.Protocol;

/**
 * The network module of the server
 * 
 * @author ldragoi
 * 
 */
public class ServerNet implements Net, NetConstants {

	private NetMediator med = null;
	private Protocol prot = null;

	private ServerSocketChannel serverSocketChannel = null;
	private Selector selector = null;

	private static boolean running = false;

	private static ExecutorService pool = Executors
			.newFixedThreadPool(DEFAULT_EXECUTOR_THREADS);

	private static Logger logger = Logger.getLogger(ServerNet.class);

	/**
	 * Constructor for the network module
	 * 
	 * @param med
	 */
	public ServerNet(NetMediator med) {
		setMediator(med);
		med.setNet(this);

		logger.info("NET initiated");
	}

	@Override
	public void setMediator(NetMediator med) {
		this.med = med;
	}

	public void startListening(int port) {

		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception on Selector.open");
			cleanup();
			return;
		}

		try {
			serverSocketChannel = ServerSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception on opening server socket channel.");
			cleanup();
			return;
		}

		try {
			serverSocketChannel.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
			logger
					.fatal("Exception in configuring blocking on server socket channel.");
			cleanup();
			return;
		}

		try {
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception when binding on port " + port);
			cleanup();
			return;
		}

		try {
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			e.printStackTrace();
			logger.fatal("Exception when registering selector");
			cleanup();
			return;
		}

		logger.info("Awaiting connection on port " + port);
	}

	public void update() {

		try {
			selector.select();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal("Exception on select");
			cleanup();
			return;
		}

		for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it
				.hasNext();) {
			SelectionKey key = it.next();
			it.remove();

			if (key.isAcceptable()) {

				try {
					accept(key);
				} catch (IOException e) {
					e.printStackTrace();
					logger.fatal("Exception on accept.");
					cleanup();
					return;
				}

			} else if (key.isReadable()) {

				try {
					read(key);
				} catch (IOException e) {
					e.printStackTrace();
					logger.fatal("Exception on read.");
					cleanup();
					return;
				}

			} else if (key.isWritable()) {

				try {
					write(key);
				} catch (IOException e) {
					e.printStackTrace();
					logger.fatal("Exception on write");
					cleanup();
					return;
				}

			}
		}
	}

	public void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		DefaultConnectionManager connManager = prot.getConnectionManager();
				
		// start reading after accept
		socketChannel.register(key.selector(), SelectionKey.OP_READ, connManager);
		connManager.setKey(key);
		
		logger.info("Accepted connection from "
				+ socketChannel.socket().getRemoteSocketAddress());
	}
	

	public void read(final SelectionKey key) throws IOException {
		// remove all interests
		key.interestOps(0);

		pool.execute(new Runnable() {
			public void run() {
				int bytes;
				DefaultConnectionManager conn = (DefaultConnectionManager) key.attachment();
				ByteBuffer buf = conn.getReadBuffer();
				SocketChannel socketChannel = (SocketChannel) key.channel();

				try {
					// read as much as you can
					while ((bytes = socketChannel.read(buf)) > 0)
						;

					// check for EOF
					if (bytes == -1)
						throw new IOException("EOF");

					processRead(conn, buf);

					// keep on reading
					key.interestOps(SelectionKey.OP_READ);

					// update selector
					key.selector().wakeup();
				} catch (IOException e) {
					logger.fatal("Connection closed: " + e.getMessage());

					try {
						socketChannel.close();
					} catch (IOException exc) {
					}
				}
			}
		});
	}

	public void write(SelectionKey key) throws IOException {
		int bytes;
		DefaultConnectionManager conn = (DefaultConnectionManager) key.attachment();
		ByteBuffer buf = conn.getWriteBuffer();
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			while ((bytes = socketChannel.write(buf)) > 0)
				;

			if (!buf.hasRemaining()) {
				buf.clear();
				key.interestOps(SelectionKey.OP_READ);
			}

		} catch (IOException e) {
			logger.fatal("Connection closed: " + e.getMessage());
			socketChannel.close();

		}
	}

	private void cleanup() {
		if (selector != null)
			try {
				selector.close();
			} catch (IOException e) {
			}

		if (serverSocketChannel != null)
			try {
				serverSocketChannel.close();
			} catch (IOException e) {
			}
	}

	private void processRead(DefaultConnectionManager conn, ByteBuffer buf) {
		buf.flip();

		int count = buf.remaining();
		byte[] rd = new byte[count];

		buf.get(rd);
		buf.clear();

		conn.processRead(rd);
	}

	@Override
	public void start(int port) {
		startListening(port);

		// start the main loop in a new thread: we don't want to block the
		// invoking thread
		new Thread() {
			public void run() {
				mainLoop();
			}
		}.start();
	}

	private void mainLoop() {
		logger.info("Started network module loop");

		running = true;

		while (running) {
			update();
		}
	}

	@Override
	public void setProtocol(Protocol prot) {
		this.prot = prot;
	}
}
