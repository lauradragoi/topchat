package topchat.client.reference;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;

/**
 * O celula din tabel in care afisez mesaje
 */
public class ConversationMessageCell extends JTextArea
{
    public int id;
    public Color color;
    
    public void setFont() {
        Font f = new Font("Times New Roman", Font.PLAIN, 12);
        super.setFont(f);
    }

    public void setWrapStyleWord() {
        super.setWrapStyleWord(true);
    }

    public void setLineWrap() {
        super.setLineWrap(true);
    }

    @Override
    public void setColumns(int columns) {
        super.setColumns(columns);
    }

    @Override
    public void setRows(int rows) {
        super.setRows(rows);
    }

    public int getCustomHeight(){
        return 0;
    }
    public ConversationMessageCell(){

	}

	public ConversationMessageCell(String msg,int id,Color c){

		super(msg);
        this.id = id;
        this.color = c;
        this.setSize(300,300);
        this.setRows(10);
	}
}