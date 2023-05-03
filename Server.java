package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class Server {
    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while (true)
                new EchoClientHandler(serverSocket.accept()).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server=new Server();
        server.start(5555);
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private List<Message> list;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                list = new ArrayList<>();
                out.println("ready");

                String inputLine;
                int n = 0;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    } else if (isNumeric(inputLine)) {
                        out.println("ready for messages");
                        n = Integer.parseInt(inputLine);
                    }
                    else {
                        try {
                            list.add((Message) fromString(inputLine));
                            n -= 1;
                            if (n == 0) {
                                out.println("finished");
                                break;
                            }
                        } catch (ClassNotFoundException e) {
                            continue;
                        }
                    }
                    out.println(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();
//                for (Message e : list) {
//                    System.out.println(e.getNumber() + ", " + e.getContent());
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean isNumeric(String strNum) {
            if (strNum == null) {
                return false;
            }
            try {
                double i = Integer.parseInt(strNum);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        }

        /** Read the object from Base64 string. */
        private static Object fromString( String s ) throws IOException ,
                ClassNotFoundException {
            byte [] data = Base64.getDecoder().decode( s );
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(  data ) );
            Object o  = ois.readObject();
            ois.close();
            return o;
        }
    }
}