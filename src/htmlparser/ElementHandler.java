package htmlparser;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class ElementHandler extends HTMLEditorKit.ParserCallback  {
    @Override
    public void handleText(char[] data, int pos) {
        String text = new String(data);
        System.out.println(text);
    }

    @Override
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
       System.out.println( t.toString().toUpperCase() );
    }

    @Override
    public void handleEndTag(HTML.Tag t, int pos) {
        super.handleEndTag(t, pos);
    }

    @Override
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        System.out.println( t.toString() );
    }
}
