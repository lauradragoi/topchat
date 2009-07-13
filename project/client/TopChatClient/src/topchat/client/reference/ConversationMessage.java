package topchat.client.reference;

import java.awt.Color;
import java.util.Vector;

/**
 * Pentru a adauga un nou mesaj trebuie adaugate in tabel o celula pentru mesaj si o celula pentru referinta
 */
public class ConversationMessage extends Vector
{
	public ConversationMessage(String msg, int row,int id,Color c)
	{
		super();
    	this.add(new ConversationReferenceCell(row));
        
        ConversationMessageCell cell = new ConversationMessageCell(msg,id,c);
        cell.setFont();
        cell.setWrapStyleWord();
        cell.setLineWrap();
        //cell.setBackground(c);
        cell.setForeground(c);
        
    	this.add(cell);
	}

}