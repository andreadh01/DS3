package htmlparser;
import javax.swing.text.html.HTMLEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Logger;

/*
*   Andrea Duarte Hernandez
*   Materia: Desarrollo de Sistemas III
*   Fecha: 1/nov/2021
*/

public class HTMLReader implements Runnable {
    // https://people.sc.fsu.edu/~jburkardt/data/csv/csv.html
    // https://people.sc.fsu.edu/~jburkardt/data/csv/cities.csv
    // https://people.sc.fsu.edu/~jburkardt/data/xml/ell_65.xml

    public static final String TAG = HTMLReader.class.getSimpleName();

    private URL finalUrl;
    public static final Logger LOG = Logger.getLogger(TAG);

    public static final String URL_STRING =
            "https://people.sc.fsu.edu/~jburkardt/data/csv/csv.html";
    public HTMLReader(URL url) {
        finalUrl = url;
    }

    public static void main(String[] args) {

        URL webPage = null;
        URLConnection connection = null;

        try {
            webPage = new URL(URL_STRING);

            // abrir conexion
            connection = webPage.openConnection();

            String tipoContenido = connection.getContentType();
            long size = connection.getContentLengthLong();

            //System.out.println("Tipo: " + tipoContenido );
            //System.out.println("Tamanio en bytes: " + size );
            //System.out.println("");

        } catch (MalformedURLException ex) {
            LOG.severe(ex.getMessage());
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
        }

        // Crear un flujo para leer datos del URL
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader( connection.getInputStream() ));
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
        }

        HTMLParser parserGetter = new HTMLParser();

        HTMLEditorKit.Parser parser = parserGetter.getParser();

        URLParser callback = new URLParser(webPage);
        //HTMLEditorKit.ParserCallback callback = new ElementHandler() ;
        //ImgCounter callback = new ImgCounter() ;

        try {
            parser.parse(in , callback, false);
        } catch (IOException ex) {

        }

        ArrayList<URL> lista = callback.getUrl();

        for (URL link: lista) {
            Thread hilo = new Thread( new HTMLReader(link));
            hilo.start();
        }
        
        //System.out.println("Total de imagenes: " + callback.getImgCounter());

    }

    @Override
    public void run() {
        URLConnection conexion = null;
        int contador = 0;
        try {
            conexion = finalUrl.openConnection();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            conexion.getInputStream()));
            while ((br.readLine()) != null) {
                contador++;
            }
            System.out.printf(" Hilo: %s%n%s tiene %d registros %n", Thread.currentThread().getName(), finalUrl.toString(), contador);
        } catch (IOException e) {
            System.out.println(finalUrl + " no existe");
        }
    }
}
