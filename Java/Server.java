import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(10314)) {
            Socket socket = null;

            while (true) {
                socket = server.accept();
                try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                    RemoteMethod method = (RemoteMethod) ois.readObject();
                    Object result = handleRemoteMethod(method);

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(result);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static Object handleRemoteMethod(RemoteMethod method) {
        switch (method.getMethodName()) {
            case "echo":
                return Server.echo((String) method.getArgs()[0]);
            case "add":
                int lhs = (int) method.getArgs()[0];
                int rhs = (int) method.getArgs()[1];
                return Server.add(lhs, rhs);
            case "divide":
                int num = (int) method.getArgs()[0];
                int denom = (int) method.getArgs()[1];
                try {
                    Object result = Server.divide(num, denom);
                    return result;
                } catch (ArithmeticException e) {
                    return e;
                }
            default:
                throw new IllegalArgumentException("Unknown method: " + method.getMethodName());
        }
    }

    // Do not modify any code below tihs line
    // --------------------------------------
    public static String echo(String message) {
        return "You said " + message + "!";
    }

    public static int add(int lhs, int rhs) {
        return lhs + rhs;
    }

    public static int divide(int num, int denom) {
        if (denom == 0)
            throw new ArithmeticException();

        return num / denom;
    }
}