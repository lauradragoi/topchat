package topchat.server.util;

import java.nio.ByteBuffer;

public class Utils {
	
	public static String getStringFromBuffer(ByteBuffer buf) {
		byte length = buf.get();
		byte[] contents = new byte[length];
		buf.get(contents);
		return new String(contents);
	}
	
	public static void putStringToBuffer(String str, ByteBuffer buf) {
		buf.put((byte)str.length());
		buf.put(str.getBytes());
	}
	
}
