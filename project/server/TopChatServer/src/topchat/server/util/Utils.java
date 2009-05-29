package topchat.server.util;

import java.nio.ByteBuffer;

/**
 * Useful methods
 */
public class Utils {
	
	/**
	 * Obtain a string from a buffer
	 * @param buf
	 * @return
	 */
	public static String getStringFromBuffer(ByteBuffer buf) {
		byte length = buf.get();
		byte[] contents = new byte[length];
		buf.get(contents);
		return new String(contents);
	}
	
	/**
	 * Put a string in a buffer
	 * @param str
	 * @param buf
	 */
	public static void putStringToBuffer(String str, ByteBuffer buf) {
		buf.put(str.getBytes());
	}
	
}
