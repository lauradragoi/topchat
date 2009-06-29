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
package topchat.server.protocol.xmpp.tls;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Vector;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;

import org.apache.log4j.Logger;

import topchat.server.interfaces.Net;
import topchat.server.util.Utils;

/**
 * Created when a connection needs to be secured with TLS. Acts as a running
 * thread until the handshake process is complete. Afterwards only acts as a
 * container for the methods needed to encode/decode secure data.
 */
public class TLSHandler implements Runnable
{

	/** The network module */
	public Net net;
	/** The socket to be secured */
	public SocketChannel socket;

	/** The SSLEngine */
	public SSLEngine sslEngine;
	/** The status of the handshake */
	public HandshakeStatus status;
	/** Flag indicating if the handshake is complete */
	public boolean handshakeComplete;

	/** Application byte buffer */
	public ByteBuffer appBB = null;

	/** A dummy empty buffer */
	private static ByteBuffer dummyBB = ByteBuffer.allocate(0);

	/** Read byte buffer */
	private ByteBuffer readBuffer;
	/** Write byte buffer */
	private ByteBuffer writeBuffer;

	/**
	 * Flag indicating if there is data that should be processed by the protocol
	 */
	private boolean dataToProcess = false;

	/** The SSL result */
	SSLEngineResult result = null;

	private static Logger logger = Logger.getLogger(TLSHandler.class);

	/**
	 * Constructs a TLSHandler
	 * 
	 * @param net
	 *            the network module
	 * @param socket
	 *            the socket to be secured
	 * @param sslEngine
	 *            the SSLEngine to be used for securing
	 */
	public TLSHandler(Net net, SocketChannel socket, SSLEngine sslEngine)
	{
		this.net = net;
		this.socket = socket;
		this.sslEngine = sslEngine;

		this.status = HandshakeStatus.NEED_UNWRAP;
		this.handshakeComplete = false;

		int netBBSize = sslEngine.getSession().getPacketBufferSize();
		int appBBSize = sslEngine.getSession().getApplicationBufferSize();
		appBB = ByteBuffer.allocate(appBBSize);

		readBuffer = ByteBuffer.allocate(netBBSize);

		writeBuffer = ByteBuffer.allocate(netBBSize);
		writeBuffer.position(0);
		writeBuffer.limit(0);
	}

	/**
	 * Decode secure received data into raw application data
	 * 
	 * @param readBuffer
	 *            the received data
	 * @return the decoded data
	 */
	public synchronized byte[] processData(ByteBuffer readBuffer)
	{
		if (!handshakeComplete)
		{
			// put the data in my local buffer
			readBuffer.flip();
			this.readBuffer.put(readBuffer);

			// modify flag to announce new data
			dataToProcess = true;
			return null;
		} else
		{
			logger.debug("Decoding data");

			SSLEngineResult result = null;
			Vector<byte[]> secureDataVector = new Vector<byte[]>();

			// prepare buffer for draining
			readBuffer.flip();

			// allocate the buffer to contain the decoded application data
			appBB = ByteBuffer.allocate(sslEngine.getSession()
					.getApplicationBufferSize());

			// while there is undecoded data
			while (readBuffer.hasRemaining())
			{
				// prepare the buffer that will contain decoded data
				appBB.clear();

				try
				{
					result = sslEngine.unwrap(readBuffer, appBB);
				} catch (SSLException e)
				{
					logger.fatal("unwrap error");
					e.printStackTrace();
				}

				logger.debug("Unwrap result " + result);

				if (result.getStatus() == SSLEngineResult.Status.OK)
				{
					// add the data to secured data vector
					secureDataVector.add(Utils.ByteBufferToByteArray(appBB));
				} else if (result.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW)
				{
					// The data left in the buffer will be unwrapped
					// after more data is added to the read buffer.
					break;
				}
			}

			// prepare it for next reading
			readBuffer.compact();

			return Utils.ByteArrayVectorToByteArray(secureDataVector);
		}
	}

	/**
	 * Obtain secure data corresponding to raw data
	 * 
	 * @param data
	 *            the raw data
	 * @return the secured data
	 */
	public synchronized byte[] getSecureData(byte[] data)
	{
		// The result of the SSL Engine operation (wrap in this case)
		SSLEngineResult result = null;

		// The secured data will not necessarily
		// be obtained only in one single step.
		// The bits of secured data that are obtained are collected
		// in this vector.
		Vector<byte[]> secureDataVector = new Vector<byte[]>();

		// Buffer used for holding the data that needs to be secured
		appBB = ByteBuffer.allocate(sslEngine.getSession()
				.getApplicationBufferSize());
		appBB.put(data);
		appBB.flip();

		// While I still have data to be secured
		while (appBB.hasRemaining())
		{
			// clear the buffer in which I put the secured data
			writeBuffer.clear();

			// wrap it - it might not all be wrapped now
			try
			{
				result = sslEngine.wrap(appBB, writeBuffer);
			} catch (SSLException e)
			{
				logger.warn("wrap exception: " + e);
			}

			logger.debug("wrap result " + result);

			// if wrap was ok
			if (result.getStatus() == SSLEngineResult.Status.OK)
			{
				// add the data to secured data vector
				secureDataVector.add(Utils.ByteBufferToByteArray(writeBuffer));
			} else
				logger.warn("wrap was not ok " + result);
		}

		return Utils.ByteArrayVectorToByteArray(secureDataVector);
	}

	/**
	 * Handles the TLS negotiation
	 */
	public void run()
	{
		logger.debug("processing");

		// this will run until handshake is complete
		while (!handshakeComplete)
		{
			boolean result = processHandshaking();

			if (!result)
			{
				// have some beauty sleep until new data is received
				try
				{
					Thread.sleep(50);
				} catch (Exception e)
				{
				}
			}
		}
	}

	/**
	 * This method handles the processing of the handshake data accordingly to
	 * the status of the handshake
	 * 
	 * @return false if there is no data to process, true otherwise
	 */
	public synchronized boolean processHandshaking()
	{

		switch (status)
		{
			case NEED_UNWRAP :

				if (!dataToProcess)
					return false;

				dataToProcess = false;
				needIO : while (status == HandshakeStatus.NEED_UNWRAP)
				{
					readBuffer.flip();

					try
					{
						result = sslEngine.unwrap(readBuffer, appBB);
					} catch (SSLException e)
					{
						logger.warn("unwrap exception " + e);
					}

					readBuffer.compact();

					logger.debug("unwrap result " + result);

					status = result.getHandshakeStatus();

					switch (result.getStatus())
					{
						case OK :
							switch (status)
							{
								case NOT_HANDSHAKING :
									logger
											.debug("Not handshaking in initial handshake");
									continue;
								case NEED_TASK :
									status = doTasks();
									break;
								case FINISHED :
									handshakeComplete = true;
									return true;
							}
							break;

						case BUFFER_UNDERFLOW :
							break needIO;

						default : // BUFFER_OVERFLOW/CLOSED:
							logger.debug("Received" + result.getStatus()
									+ "during initial handshaking");
							continue;
					}

					logger.debug("new status " + status);

				}

				if (status != HandshakeStatus.NEED_WRAP)
					break;

				// fall through
			case NEED_WRAP :

				writeBuffer.clear();
				try
				{
					result = sslEngine.wrap(dummyBB, writeBuffer);
				} catch (SSLException e)
				{
					logger.warn("wrap exception: " + e);
				}
				writeBuffer.flip();

				status = result.getHandshakeStatus();

				logger.debug("wrap result " + result);

				switch (result.getStatus())
				{
					case OK :
						if (status == HandshakeStatus.NEED_TASK)
						{
							status = doTasks();
						}

						// drain the buffer that was filled
						flush(writeBuffer);

						if (status == HandshakeStatus.FINISHED)
						{
							logger.debug("Handshake complete.");
							handshakeComplete = true;
							return true;
						}

						break;

					default : // BUFFER_OVERFLOW/BUFFER_UNDERFLOW/CLOSED:
						logger.debug("Received" + result.getStatus()
								+ "during initial handshaking");

						// continue;
				}
				break;

			default : // NOT_HANDSHAKING/NEED_TASK/FINISHED
				logger.warn("Invalid Handshaking State" + status);
		}

		return true;
	}

	/**
	 * Send data to the network module to be sent
	 * 
	 * @param bb
	 *            the buffer containing the data
	 */
	private void flush(ByteBuffer bb)
	{
		int count = bb.remaining();
		byte[] data = new byte[count];

		bb.get(data);

		logger.debug("Writing " + count);

		net.sendRaw(socket, data);
	}

	/**
	 * Handle tasks from the tlsEngine
	 * 
	 * @return the status of the handshake
	 */
	private SSLEngineResult.HandshakeStatus doTasks()
	{
		logger.debug("Doing tasks");
		Runnable runnable;

		while ((runnable = sslEngine.getDelegatedTask()) != null)
		{
			logger.debug("running task");
			runnable.run();
		}

		logger.debug("status after task " + sslEngine.getHandshakeStatus());

		return sslEngine.getHandshakeStatus();
	}

}
