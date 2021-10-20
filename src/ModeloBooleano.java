import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
*   Andrea Duarte Hernández
*   Materia: Desarrollo de Sistemas III
*   Fecha: 19/oct/2021
 */

public class ModeloBooleano {
    public static void main(String[] args) {
        // TODO code application logic here

        if (args.length == 0) {
            System.out.println("Falta el nombre de archivo");
            System.exit(0);
        }

        File folder = new File(args[0]);
        Hashtable<String, LinkedList> tabla = new Hashtable<>();
        File[] listOfFiles = folder.listFiles();
        FileReader fr = null;

        for (int i = 0; i < listOfFiles.length; i++) {
            try {
                fr = new FileReader(listOfFiles[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("Procesando archivo '" + listOfFiles[i].getName() + "'...");

            BufferedReader in = new BufferedReader(fr);

            String line = null;

            //int lineCount = 0;
            //int wordCount = 0;

            //long startTime = System.currentTimeMillis();

            String delimiters = "\\s+|,\\s*|\\.\\s*|\\;\\s*|\\:\\s*|\\!\\s*|\\¡\\s*|\\¿\\s*|\\?\\s*|\\-\\s*|\\_\\s*|\"\\s*|\\[\\s*|\\]\\s*|\\(\\s*|\\)\\s*";

            Pattern pat = Pattern.compile("[0-9]*");

            HashSet<String> stopWords = loadStopWords();

            try {
                while ((line = in.readLine()) != null) {
                    // lineCount++;

                    if (line.trim().length() == 0) {
                        continue;
                    }

                    String words[] = line.split(delimiters);

                    //System.out.println(line);
                    for (String word : words) {
                        word = word.toLowerCase();
                        Matcher mat = pat.matcher(word);
                        if (!mat.matches() && !stopWords.contains(word)) {
                            agregarPalabra(tabla, word, listOfFiles[i].getName());
                        }
                    }
                    //System.out.println();
                    // wordCount += words.length;
                }

                in.close();
                fr.close();
                //long total = System.currentTimeMillis() - startTime;
                //System.out.printf("%2.3f  segundos, %,2d lineas y %,3d palabras\n", total / 1000.00, lineCount, wordCount);
            } catch (IOException ex) {
                Logger.getLogger(ModeloBooleano.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (String key : tabla.keySet()) {
            System.out.println(key + " --> " + tabla.get(key));
        }

        boolean continuar = true;
        while(continuar) {
            Scanner sc = new Scanner(System.in);

            System.out.println();
            System.out.println("Ingrese una palabra que quiera buscar: ");
            String word1 = sc.nextLine();

            System.out.println("Ingrese otra palabra que quiera buscar: ");
            String word2 = sc.nextLine();

            System.out.println("Ahora, elija que operacion quiere realizar: AND, OR, NOT");
            String operacion = sc.nextLine();

            consulta(operacion, word1, word2, tabla);

            System.out.println("Le gustaria realizar otra operacion? Ingrese '1', en caso de salir pulsa cualquier otra tecla");
            int num;
            try{
                num = sc.nextInt();
                if(num!=1){
                    System.exit(2);
                }
            } catch (InputMismatchException e){
                System.exit(2);
            }

        }

    }

    public static HashSet<String> loadStopWords() {
        FileReader fi = null;
        try {
            fi = new FileReader("stopwords-es.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ModeloBooleano.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader in = new BufferedReader(fi);

        HashSet<String> table = new HashSet<>();

        String line = null;

        try {
            while ((line = in.readLine()) != null) {
                table.add(line);
            }
            in.close();
            fi.close();
        } catch (IOException ex) {
            Logger.getLogger(ModeloBooleano.class.getName()).log(Level.SEVERE, null, ex);
        }

        return table;

    }

    public static void agregarPalabra(Hashtable<String, LinkedList> tabla, String word, String document) {
        LinkedList<String> lista = tabla.get(word);

        if (lista != null) {
            if (!lista.contains(document)) {
                lista.add(document);
            }

        } else { //si no existe, creamos lista y agregamos lista al hashtable
            LinkedList<String> lista_nueva = new LinkedList<>();
            lista_nueva.add(document);
            tabla.put(word, lista_nueva);
        }


    }

    public static void consulta(String tipo, String first_word, String sec_word, Hashtable<String, LinkedList> tabla) {
        String tipoMayus = tipo.toUpperCase(Locale.ROOT);
        switch (tipoMayus) {
            case "AND":
                System.out.println(opAND(first_word, sec_word, tabla));
                break;
            case "OR":
                System.out.println(opOR(first_word, sec_word, tabla));
                break;
            case "NOT":
                System.out.println(opNOT(first_word, sec_word, tabla));
                break;
            default:
                System.out.println("Ingresa una consulta valida");
                System.exit(2);
                break;
        }
    }

    private static LinkedList<String> opNOT(String first_word, String sec_word, Hashtable<String, LinkedList> tabla) {
        LinkedList<String> lista1 = tabla.get(first_word);
        LinkedList<String> lista2 = tabla.get(sec_word);
        LinkedList<String> interseccion = new LinkedList<>();
        LinkedList<String> diferencia = new LinkedList<>();

        if (lista1 != null && lista2 != null) {

            System.out.println("Documentos donde se encuentra la palabra " + first_word + " pero no se encuentre la palabra " + sec_word + ":");

            for (String doc : lista1) {
                for (String doc2 : lista2) {
                    if (lista2.contains(doc)){
                        if (!interseccion.contains(doc)) {
                            interseccion.add(doc);
                        }
                    }
                    if (!interseccion.contains(doc) && !doc.equals(doc2) && !diferencia.contains(doc)) {
                        diferencia.add(doc);
                    }
                }

            }
        }
        return diferencia;
    }

    private static LinkedList<String> opOR(String first_word, String sec_word, Hashtable<String, LinkedList> tabla) {
        LinkedList<String> lista1 = tabla.get(first_word);
        LinkedList<String> lista2 = tabla.get(sec_word);
        LinkedList<String> union =  new LinkedList<>();

        if (lista1 != null && lista2 != null) {
            System.out.println("Las palabras " + first_word + " o " + sec_word + " se encuentran en los siguientes documentos: ");
            for (String doc : lista1) {
                union.add(doc);
            }
            for (String doc : lista2) {
                if(!union.contains(doc)) {
                    union.add(doc);
                }
            }
        }
        return union;
    }



    private static LinkedList<String> opAND(String first_word, String sec_word, Hashtable<String, LinkedList> tabla) {
        LinkedList<String> lista1 = tabla.get(first_word);
        LinkedList<String> lista2 = tabla.get(sec_word);
        LinkedList<String> interseccion =  new LinkedList<>();

        if (lista1 != null && lista2 != null) {
            System.out.println("Las palabras " + first_word + " y/e " + sec_word + " se encuentran en los siguientes documentos: ");
            for (String doc : lista1) {
                for (String doc2 : lista2) {
                    if (doc.equals(doc2)) {
                        interseccion.add(doc);
                    }
                }
            }
        }

        return interseccion;
    }
}
