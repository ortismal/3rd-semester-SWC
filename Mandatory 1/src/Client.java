import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class Client {

    private String name;
    private Socket s;
    private InputStream input;
    private OutputStream output;

//    public Client(String name, Socket s, InputStream input, OutputStream output) {
//        this.name = name;
//        this.s = s;
//        this.input = input;
//        this.output = output;
//    }

    public Client(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", s=" + s +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}