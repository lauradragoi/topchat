package topchat.server.util;

import java.nio.ByteBuffer;
import java.util.Vector;

/**
 * Useful methods
 */
public class Utils {
	
	/**
	 * Convert a ByteBuffer to a byte array
	 * @param byteBuffer the ByteBuffer to be converted
	 * @return the byte array
	 */
	public static byte[] ByteBufferToByteArray(ByteBuffer byteBuffer)
	{
		byteBuffer.flip();
				
		int count = byteBuffer.remaining();
		byte[] byteArray = new byte[count];

		byteBuffer.get(byteArray);
		
		return byteArray;
	}
	
	/**
	 * Convert a vector of byte arrays to a byte array
	 * @param vector the vector of byte arrays
	 * @return a byte array containing the concatenated arrays in the vector
	 */
	public static byte[] ByteArrayVectorToByteArray(Vector<byte[]> vector)
	{
		int count = 0;
		for (byte[] b : vector)
			if (b != null)
				count += b.length;
		
		if (count == 0)
			return null;
				
		byte[] byteArray = new byte[count];
		
		int offset = 0;
		for (byte[] b : vector)
		{
			if (b != null)
			{
				System.arraycopy(b, 0, byteArray, offset, b.length);
				offset += b.length;
			}
		}

		return byteArray;
	}
}
