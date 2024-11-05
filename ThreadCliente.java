import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ThreadCliente extends Thread {
    public static final int PUERTO = 3400;
    public static final String SERVIDOR = "localhost";
    public String id_cliente;

    public ThreadCliente(String id){
        this.id_cliente = id;
    }

    @Override
    public void run(){
        
        if (id_cliente.equals("0")){
        
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;

        System.out.println("Cliente (unica consulta)...");

        try {
            socket = new Socket(SERVIDOR, PUERTO);
            escritor = new PrintWriter(socket.getOutputStream(), true);
            lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Exception: " + e.getMessage());
            System.exit(1);
        }

       
        ProtocoloCliente protocolo = new ProtocoloCliente();
        try {
            protocolo.procesar2(lector, escritor);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
       
        try {
            escritor.close();
            lector.close();
            socket.close();
           
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }   
    
    else if (id_cliente.equals("-1")){
        
            Socket socket = null;
            PrintWriter escritor = null;
            BufferedReader lector = null;

            System.out.println("Cliente (Unico Multi consulta) ...");

            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(SERVIDOR, PUERTO), 5000); // Timeout de 5 segundos
                escritor = new PrintWriter(socket.getOutputStream(), true);
                lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.err.println("Exception: " + e.getMessage());
                System.exit(1);
            }

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            try {
                ProtocoloCliente protocolo = new ProtocoloCliente();
                protocolo.procesar3(id_cliente, stdIn, lector, escritor);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        
            try {
                escritor.close();
                lector.close();
                socket.close();
                stdIn.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    else {
    
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector = null;

        System.out.println("Cliente (Concurrentes) ...");

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SERVIDOR, PUERTO), 5000); // Timeout de 5 segundos
            escritor = new PrintWriter(socket.getOutputStream(), true);
            lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Exception: " + e.getMessage());
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            ProtocoloCliente protocolo = new ProtocoloCliente();
            protocolo.procesar(id_cliente, stdIn, lector, escritor);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    
        try {
            escritor.close();
            lector.close();
            socket.close();
            stdIn.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
    }
    }

}
    /* public static final int PUERTO = 3400;
    public static final String SERVIDOR = "localhost";

    private Socket socket;
    private PrintWriter escritor;
    private BufferedReader lector;

    public ThreadCliente() {
        try {
            this.socket = new Socket(SERVIDOR, PUERTO);
            this.escritor = new PrintWriter(socket.getOutputStream(), true);
            this.lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error al crear la conexión con el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Cliente iniciado en el hilo " + Thread.currentThread().getName());

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            ProtocoloCliente.procesar(stdIn, lector, escritor);
        } catch (IOException e) {
            System.err.println("Excepción en Cliente durante el procesamiento: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                cerrarRecursos(stdIn);
            } catch (IOException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cerrarRecursos(BufferedReader stdIn) throws IOException {
        if (escritor != null) escritor.close();
        if (lector != null) lector.close();
        if (socket != null) socket.close();
        if (stdIn != null) stdIn.close();  // Cerrar stdIn aquí, al final
        System.out.println("Recursos cerrados para el hilo " + Thread.currentThread().getName());
    } */
//}
