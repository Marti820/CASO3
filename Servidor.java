import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class Servidor {
    public static final int PUERTO = 3400;
    private static boolean continuar = true;

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 2) {
            System.out.println("===== Menú de Opciones =====");
            System.out.println("1. Generar llaves simetricas");
            System.out.println("2. Iniciar servidor");
            System.out.print("Elige una opción: ");
            
            opcion = scanner.nextInt();

            if (opcion == 1) {
                System.out.println("GENERANDO LLAVES EN LOS ARCHIVOS: ");
                generarLlaves();
            } else if (opcion == 2) {
                System.out.println("INICIANDO EL SERVIDOR");
                try {
                    iniciarServidor();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                System.out.println("Opción inválida. Por favor, elige 1 o 2.");
            }
            System.out.println(); 
        }
        scanner.close(); 
    }
    public static void generarLlaves() throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(1024);
		KeyPair keyPair = generator.generateKeyPair();
		PublicKey publica = keyPair.getPublic();
		PrivateKey privada = keyPair.getPrivate();

        
        try (FileOutputStream fos = new FileOutputStream("publicKey.key")) {
            fos.write(publica.getEncoded());
        }

       
        try (FileOutputStream fos = new FileOutputStream("privateKey.key")) {
            fos.write(privada.getEncoded());
        }

        System.out.println("Las llaves han sido generadas y guardadas en archivos");
    }
    


    public static void iniciarServidor() throws IOException{
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
