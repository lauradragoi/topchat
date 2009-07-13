package topchat.client.reference;

import java.awt.Color;

public class Reference
{
	private int source;
	private int destination;
    public Color color;

	public Reference(int source, int destination,Color c)
	{
		this.source = source;
		this.destination = destination;
        this.color = c;
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