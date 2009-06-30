package topchat.client.reference;

public class Reference
{
	private int source;
	private int destination;

	public Reference(int source, int destination)
	{
		this.source = source;
		this.destination = destination;
	}

	public int getSource()
	{
		return source;
	}

	public int getDestination()
	{
		return destination;
	}
}