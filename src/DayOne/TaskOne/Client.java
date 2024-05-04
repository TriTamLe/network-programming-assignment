package DayOne.TaskOne;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 5420;

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Enter the string: ");
            String message = reader.readLine();

            // send the message to the server
            writer.println(message);

            // read the response from the server
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("From the server: ");

            String line;
            while ((line = serverReader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
