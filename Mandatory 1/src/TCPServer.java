import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TCPServer {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting TCP Server main program");
        ArrayList<Client> clients = new ArrayList<>();
        ServerSocket socket = new ServerSocket(5656);

        System.out.println("Socket created!");
        System.out.println("Waiting for a connection..");

        while (true) {
            final Socket s = socket.accept();
            System.out.println("Client request recieved: " + s);

            Thread threads = new Thread(() -> {
                try {
                    Client client = new Client();
                    InputStream input = s.getInputStream();
                    OutputStream output = s.getOutputStream();

                    String msgIn;
                    String userName;
                    byte[] dataIn;

                    do{
                    dataIn = new byte[1024];
                    input.read(dataIn);

                    msgIn = new String(dataIn);
                    msgIn = msgIn.trim();
                    userName = msgIn.substring(5, msgIn.lastIndexOf(","));

                    client.setS(s);
                    client.setInput(input);
                    client.setOutput(output);

                        if(!isDuplicate(userName, clients)) {
                            client.setName(userName);
                            clients.add(client);
                            System.out.println(msgIn);
                            sendMsg("J_OK", output);
                            break;
                        }
                        sendMsg("J_ER 01: Name already exists", output);

                    } while (true);


                    do {
                        dataIn = new byte[1024];
                        client.getInput().read(dataIn);

                        msgIn = new String(dataIn);
                        msgIn = msgIn.trim();
                        if (msgIn.equalsIgnoreCase("!commands")) {
                            commands();
                        }
                        if (msgIn.equalsIgnoreCase("!list")) {
                            list(clients);
                        } else {
                            System.out.print("\n" + msgIn);
                            for (Client c : clients) {
                                sendMsg("\n" + msgIn, c.getOutput());
                            }
                        }

                    } while (!msgIn.equalsIgnoreCase("quit"));

                    System.out.println(client.getName() + " has left the chat!");

                    s.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threads.start();
        }

    }

    // Printer en oversigt over forskellige chat-commands.
    public static void commands() {
        System.out.println("List of commands: ");
        System.out.println("1. Quit - leave the server.");
        System.out.println("2. List - prints a list of active clients");
    }

    // Printer en oversigt over aktive brugere i chatten
    public static void list(ArrayList<Client> clients) {
        for (int i = 0; i < clients.size(); i++) {
            System.out.println(clients.get(i).toString());
        }
    }

    // Check if name already exists
    public static boolean isDuplicate(String userName, ArrayList<Client> clients) {
        for (Client c : clients){
            if(c.getName().equalsIgnoreCase(userName)){
                return true;
            }
        }
        return false;
    }

    public static void sendMsg(String msg, OutputStream output) {
        byte[] dataOut = msg.getBytes();
        try {
            output.write(dataOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
