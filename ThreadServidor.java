import java.io.*;
import java.net.Socket;

public class ThreadServidor extends Thread {
    private Socket sktCliente;
    private int id;

    public ThreadServidor(Socket pSocket, int pId) {
        this.sktCliente = pSocket;
        this.id = pId;
    }

    public void run() {
        System.out.println("Inicio de un nuevo thread: " + id);

        try {
            PrintWriter escritor = new PrintWriter(sktCliente.getOutputStream(), true);
            BufferedReader lector = new BufferedReader(new InputStreamReader(sktCliente.getInputStream()));
            DataOutputStream outPutStream = new DataOutputStream(sktCliente.getOutputStream());

            ProtocoloServidor protocolo = new ProtocoloServidor();
            protocolo.procesar(lector, escritor, outPutStream);

            escritor.close();
            lector.close();
            sktCliente.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
