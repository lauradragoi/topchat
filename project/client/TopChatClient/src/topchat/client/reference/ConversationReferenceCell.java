package topchat.client.reference;


import java.awt.Graphics;
import java.util.Vector;
import javax.swing.JPanel;

/**
 * O celula din tabel in care afisez referinte
 */
public class ConversationReferenceCell extends JPanel
{
	private Vector<Reference> references = new Vector<Reference>();
	private int row = -1;

	public ConversationReferenceCell()
	{
	}

	public ConversationReferenceCell(int row)
	{
		super(true);
		setVisible(true);
		this.row = row;
	}

	public synchronized void addReference(int source, int destination)
	{
		System.out.println("adding reference " + source + " dest " + destination + " at " + row);
		references.add(new Reference(source, destination));
		//this.append("[" + source + "," + destination + "]");
	}

    @Override
	public synchronized void paintComponent(Graphics g)
	{
		int w = g.getClipBounds().width;
		int h = g.getClipBounds().height;

		int pad = 10;

		for (Reference r : references)
		{
			//int x = (int) ((r.getSource() - r.getDestination()) / (r.getSource() * 1.0) * (w - pad));
			int x = (int) ((r.getDestination()) / (r.getSource() * 1.0) * (w - 2 * pad)) + pad;
			System.out.println("referinta "+ r.getSource() + " " + r.getDestination() + " r " + row + " x = " +x);

			if (row == r.getSource())
			{
				g.drawLine(x, h/2, w, h/2);
				g.drawLine(x, 0, x, h/2);
			}
			else if (row == r.getDestination())
			{
				g.drawLine(x, h/2, w, h/2);
				g.drawLine(x, h/2, x, h);
			}
			else
			{
				g.drawLine(x, 0, x, h);
			}

		}

	}
}