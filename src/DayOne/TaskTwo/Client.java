package DayOne.TaskTwo;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 5420;

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Enter your expression: ");
            String expression = reader.readLine();

            writer.println(expression);

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = serverReader.readLine();
            System.out.println("Result from server: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
