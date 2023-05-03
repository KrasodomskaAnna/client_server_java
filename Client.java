package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public String startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(Integer msg) {
        try {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendMessage(Message msg) {
        try {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
    /** Write the object to a Base64 string. */
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static void main(String[] args) {
        Client client1 = new Client();
        System.out.println(client1.startConnection("127.0.0.1", 5555));

        Scanner scan = new Scanner(System.in);
        System.out.println("Give n");
        Integer n = scan.nextInt();
        System.out.println(client1.sendMessage(n));
        for (int i=0; i < n; i++) {
            Message message = new Message();

            System.out.println("Give number for " + i + " object of Message");
            Integer number = scan.nextInt();
            message.setNumber(number);

            System.out.println("Give content for " + i + " object of Message");
            String content = scan.next();
            message.setContent(content);

            try {
                client1.sendMessage(toString(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            System.out.println(client1.in.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client1.stopConnection();
    }
}