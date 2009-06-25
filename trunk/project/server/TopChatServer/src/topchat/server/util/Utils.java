package topchat.server.util;

import java.nio.ByteBuffer;
import java.util.Vector;

/**
 * Useful methods
 */
public class Utils {
	
	public static byte[] ByteBufferToByteArray(ByteBuffer byteBuffer)
	{
		byteBuffer.flip();
				
		int count = byteBuffer.remaining();
		byte[] byteArray = new byte[count];

		byteBuffer.get(byteArray);
		
		return byteArray;
	}
	
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
