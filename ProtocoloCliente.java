import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ProtocoloCliente {
    public static void procesar(BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws IOException {
        String fromServer;
        String fromUser;

        boolean ejecutar = true;

        while (ejecutar) {
           /*  // Lee del teclado
            System.out.println("Escriba el mensaje para enviar: ");
            fromUser = stdIn.readLine();

            if (fromUser != null) {
                System.out.println("El usuario escribió: " + fromUser);
                if (fromUser.equalsIgnoreCase("OK")) {
                    ejecutar = false;
                }

                // Envía por la red
                pOut.println(fromUser);
            }

            // Lee lo que llega por la red
            if ((fromServer = pIn.readLine()) != null) {
                System.out.println("Respuesta del Servidor: " + fromServer);
            } */
            System.out.print("Ingrese el identificador de usuario: ");
            String usuario = stdIn.readLine();
            System.out.print("Ingrese el identificador del paquete: ");
            String paquete = stdIn.readLine();
    
            String consulta = usuario + "|" + paquete;
            pOut.println(consulta);
    
            String respuesta = pIn.readLine();
            System.out.println("Estado del paquete: " + respuesta);
            
        }
    }
}
