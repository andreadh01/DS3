package htmlparser;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.util.Enumeration;

public class ImgCounter extends HTMLEditorKit.ParserCallback {
    private  int imgCounter;

    public ImgCounter() {
        super();
        imgCounter = 0;
    }

    @Override
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if ( t == HTML.Tag.IMG ) {
            imgCounter++;
            Enumeration attributeNames = a.getAttributeNames();

            // mostrar atributos y valores
            while (attributeNames.hasMoreElements()) {
                Object  name =  attributeNames.nextElement();
                System.out.printf("%s = %s\n", name, a.getAttribute(name));
            }
            System.out.println();
        }
    }

    public int getImgCounter() {
        return imgCounter;
    }
}
