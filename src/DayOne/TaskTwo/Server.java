package DayOne.TaskTwo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

public class Server {
    public static void main(String[] args) {
        final int PORT = 5420;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running");
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

            String expression = reader.readLine();
            System.out.println("From the client: " + expression);


            String result = calculateExpression(expression);
            writer.println(result);

            socket.close();
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