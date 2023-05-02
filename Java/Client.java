import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ConnectException;

public class Client {
    /**
     * This method name and parameters must remain as-is
     */
    public static int add(int lhs, int rhs) {
        RemoteMethod add = new RemoteMethod("add", new Object[] { lhs, rhs });
        return (int) handleRequest(add);
    }

    /**
     * This method name and parameters must remain as-is
     */
    public static int divide(int num, int denom) throws ArithmeticException {
        RemoteMethod divide = new RemoteMethod("divide", new Object[] { num, denom });
        try {
            Object response = handleRequest(divide);
            return (int) response;
        } catch (Exception err) {
            throw new ArithmeticException();
        }
    }

    /**
     * This method name and parameters must remain as-is
     */
    public static String echo(String message) {
        RemoteMethod echo = new RemoteMethod("echo", new Object[] { message });
        return (String) handleRequest(echo);
    }

    public static Object handleRequest(RemoteMethod method) {
        try (Socket socket = new Socket("localhost", PORT)) {

            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(method);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object response = ois.readObject();

            return response;

        } catch (ConnectException err) {
            System.out.println("Server error. Cannot reach localhost:" + PORT);
            System.exit(1);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    // Do not modify any code below this line
    // --------------------------------------
    String server = "localhost";
    public static final int PORT = 10314;

    public static void main(String... args) {
        // All of the code below this line must be uncommented
        // to be successfully graded.
        System.out.print("Testing... ");

        if (add(2, 4) == 6)
            System.out.print(".");
        else
            System.out.print("X");

        try {
            divide(1, 0);
            System.out.print("X");
        } catch (ArithmeticException x) {
            System.out.print(".");
        }

        if (echo("Hello").equals("You said Hello!"))
            System.out.print(".");
        else
            System.out.print("X");

        System.out.println(" Finished");
    }
}
