package DayTwo.TaskTwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Stack;

public class Server {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(5420);
            System.out.println("Server is running...");


            while (true) {
                Thread thread = new Thread(new ClientHandler(serverSocket));
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements  Runnable {
    private final DatagramSocket serverSocket;
    private final byte[] receiveData = new byte[1024];


    public ClientHandler(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            DatagramPacket receivePacket = new DatagramPacket(this.receiveData, this.receiveData.length);
            this.serverSocket.receive(receivePacket);

            String expression = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("From the client: " + expression);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();


            String result = calculateExpression(expression);
            byte[] sendData = result.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String calculateExpression(String expression) {

        expression = expression.replaceAll("\\s+", "");
        char[] tokens = expression.toCharArray();

        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();


        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (Character.isDigit(tokens[i])) {
                StringBuilder sb = new StringBuilder();

                while (i < tokens.length && Character.isDigit(tokens[i])) {
                    sb.append(tokens[i++]);
                }
                i--;
                values.push(Integer.parseInt(sb.toString()));
            } else if (tokens[i] == ')') {

                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {

                while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }


        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }


        return String.valueOf(values.pop());
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private int applyOperation(char operator, int b, int a) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new UnsupportedOperationException("Không thể chia cho 0");
                yield a / b;
            }
            default -> 0;
        };
    }
}