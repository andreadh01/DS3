package msp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    public static final String CLASS_NAME = ConnectionHandler.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);


    private UserManager users;
    private Socket clientSocket = null;

    private BufferedReader input;
    private PrintWriter output;


    public ConnectionHandler(UserManager u, Socket s) {
        users = u;
        clientSocket = s;

        try {
            input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String buffer = null;
        while (true) {
            try {
                buffer = input.readLine();
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                e.printStackTrace();
            }
            String command = buffer.trim();
            // CONNECT Juan
            if (command.startsWith("CONNECT")) {
                String userName = command.substring(command.indexOf(' ')).trim();
                System.out.println(userName);
                boolean isConnected = users.connect(userName, clientSocket);
                if (isConnected) {
                    output.println("OK");
                } else {
                    output.println("FAIL");
                }
            }

            // SEND #<mensaje>@<usuario>
            if (command.startsWith("SEND")) {
                String message = command.substring(command.indexOf('#') + 1,
                        command.indexOf('@'));
                System.out.println(message);
                String userName = command.substring(command.indexOf('@') + 1).trim();
                System.out.println(userName);

                users.send(message);
                output.println(message);
            }

            //DISCONNECT <nombre_usuario>
            if (command.startsWith("DISCONNECT")) {
                String userName = command.substring(command.indexOf(' ')).trim();
                System.out.println(userName);
                System.out.println(clientSocket);
                users.disconnect(userName, clientSocket);
                output.println("BYE");

            }

            if (buffer.startsWith("LIST")) {
                String list = users.list();
                output.println(list);
            }


        }
    }
}
