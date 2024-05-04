package DayOne.TaskThree;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5420;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            // Tạo một luồng riêng biệt để nhận tin nhắn từ server
            Thread receiveThread = new Thread(() -> {
                String message;
                try {
                    while ((message = serverReader.readLine()) != null) {
                        System.out.println("Server: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // Gửi tin nhắn từ client đến server
            String userInputMessage;
            while (true) {
                userInputMessage = userInput.readLine();
                serverWriter.println(userInputMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
