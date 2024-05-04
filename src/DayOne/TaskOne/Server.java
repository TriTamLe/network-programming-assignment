package DayOne.TaskOne;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5420; //Listening on port 5400

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to the client");
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String message = reader.readLine(); // Read the message from client
            System.out.println("From client: " + message);


            String upperMessage = message.toUpperCase();
            String lowerMessage = message.toLowerCase();
            int wordCount = message.split("\\s+").length;
            String response = "Upper case: " + upperMessage + '\n' + "Lower case: " + lowerMessage + '\n'
                    + "Word count: " + String.valueOf(wordCount);

            writer.println(response);

            // Close the connection
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
