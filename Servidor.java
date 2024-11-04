import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static final int PUERTO = 3400;
    private static boolean continuar = true;

    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;

        System.out.println("Main Server ...");

        try {
            ss = new ServerSocket(PUERTO);
        } catch (IOException e) {
            System.err.println("No se pudo crear el socket en el puerto: " + PUERTO);
            System.exit(-1);
        }

        while (continuar) {
            // Crear el socket
            Socket socket = ss.accept();

            // Crear el thread con el socket y el id
            ThreadServidor thread = new ThreadServidor(socket, socket.hashCode());
            thread.start();
        }
        ss.close();
    }
}
