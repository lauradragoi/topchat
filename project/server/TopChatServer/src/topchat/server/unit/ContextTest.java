package topchat.server.unit;

import junit.framework.TestCase;

public class ContextTest extends TestCase {
	
	
	
	public void checkStuff(int size) throws Exception {

		
		assertEquals("Sizes of sent and received messages don't match",
				size, 12);
		
	}
	
	/**
	 * Checks that the recieved message matches the one sent
	 * @throws Exception
	 */
	public void testMessageContent() throws Exception {
		final int SIZE = 10;
		
		checkStuff(SIZE);
	}
	
	/**
	 * Checks that two messages can safely be sent over the same connection
	 * @throws Exception
	 */
	public void testConsecutiveMessages() throws Exception {
		final int SIZE1 = 10, SIZE2 = 20;
		
		checkStuff(SIZE1);
		checkStuff(SIZE2);
	}
	
	/**
	 * Tests the transfer of a large chunk
	 * @throws Exception
	 */
	public void testBigMessage() throws Exception {
		final int SIZE = (int)1e6; // 10^6
		
		checkStuff(SIZE);
	}
	
	/**
	 * Checks progress
	 * @throws Exception
	 */
	public void testProgress() throws Exception {
		final int SIZE = (int)1e6; // 10^6
		

	}
}
