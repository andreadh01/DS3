package msp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

public class UserManager {

    public static final String CLASS_NAME = UserManager.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private HashMap<String, Socket> connections;

    public UserManager() {
        super();
        connections = new  HashMap<String, Socket>();
    }

    public boolean connect(String user, Socket socket) {
        boolean result = true;

       Socket s = connections.put(user,socket);
       if( s != null) {
           result = false;
       }
       return result;
    }

    public void disconnect(String user, Socket socket) {
        connections.remove(user, socket);
    }

    public Socket get(String user) {

        Socket s = connections.get(user);

        return s;
    }

    public String list(){

        Set<String> users = connections.keySet();
        String list = "";

        for (String user: users) {
            list += user + "\t";
        }

        return list;

    }

    public void send(String message) {
        //Obtener los valores de un hashMap
        Collection<Socket> conexiones = connections.values();

        //Para una de las conexiones debemos de tener output para enviar el mensaje
        for (Socket s : conexiones) {
            try {
                PrintWriter output = new PrintWriter(s.getOutputStream(), true);
                output.println(message);
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
