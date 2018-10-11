import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//klasse til at håndtere klient-objekters forbindelse til TCP serveren

public class TCPClient {

    public static void main(String[] args) {
        System.out.println("=============CLIENT==============");

        Scanner sc = new Scanner(System.in);
        System.out.print("What is the IP for the server (type 0 for localhost): ");
        String ipToConnect = args.length >= 1 ? args[0] : sc.nextLine();

        System.out.print("What is the PORT for the server: ");
        int portToConnect = args.length >= 2 ? Integer.parseInt(args[1]) : sc.nextInt();


        final int PORT_SERVER = portToConnect;
        final String IP_SERVER_STR = ipToConnect.equals("0") ? "127.0.0.1" : ipToConnect;

        try {
            InetAddress ip = InetAddress.getByName(IP_SERVER_STR);

            System.out.println("\nConnecting...");
            System.out.println("SERVER IP: " + IP_SERVER_STR);
            System.out.println("SERVER PORT: " + PORT_SERVER + "\n");

            Socket socket = new Socket(ip, PORT_SERVER);

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            sc = new Scanner(System.in);
            String userName;
            String msgToSend;
            byte[] dataToSend;


            do {
                do {
                    System.out.println("What is your username?");
                    userName = sc.nextLine();
                    if ((userName.matches("^[a-zA-Z\\d-_]{0,12}$"))) {
                        break;
                    }
                    System.out.println("Username can only be 12 characters long, and can only consist of letters a-z, digits, ‘-‘ and ‘_’.");
                } while (true);

                msgToSend = "JOIN " + userName + ", " + IP_SERVER_STR + ":" + PORT_SERVER;

                dataToSend = msgToSend.getBytes();
                output.write(dataToSend);


                byte[] dataIn = new byte[1024];
                input.read(dataIn);
                String msgIn = new String(dataIn);
                msgIn = msgIn.trim();

                if(msgIn.contains("J_OK")){
                    System.out.println("J_OK");
                    break;
                }
                System.out.println("IN -->" + msgIn + "<--");
            } while (true);

            Thread receive = new Thread(() -> {
                while (true) {
                    byte[] dataReceive = new byte[1024];
                    try {
                        input.read(dataReceive);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String msg = new String(dataReceive);
                    msg = msg.trim();
                    System.out.println("\n" + msg);
                }
            });

            receive.start();
            
            do {
                do {
                    System.out.print("Please type your text: ");
                    msgToSend = sc.nextLine();
                    if((msgToSend.length() < 249)){
                        break;
                    }
                    System.out.println("Maximum length of message is 250 characters, try again.");
                } while (true);
                msgToSend = "DATA " + userName + ": " + msgToSend;
                dataToSend = msgToSend.getBytes();
                output.write(dataToSend);

            } while (!msgToSend.equalsIgnoreCase("quit"));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}