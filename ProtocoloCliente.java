import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ProtocoloCliente {
    public synchronized void procesar(String id,BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws IOException {
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
            //System.out.print("Ingrese el identificador de usuario: ");
            //String usuario = stdIn.readLine();
            //System.out.print("Ingrese el identificador del paquete: ");
            //String paquete = stdIn.readLine();
            String usuario = "user"+id;
            String paquete = "package"+id;
            String consulta = usuario + "|" + paquete;
            pOut.println(consulta);
    
            String respuesta = pIn.readLine();
            System.out.println("Estado del paquete: " + respuesta);
            ejecutar = false;
        }
    }

    public void procesar2(BufferedReader pIn, PrintWriter pOut) throws IOException {
        boolean ejecutar = true;
        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); // Mover stdIn aquí
        Scanner scanner = new Scanner(System.in);
        while (ejecutar) {
            System.out.print("Ingrese el identificador de usuario: ");
            String usuario = scanner.next();
            System.out.print("Ingrese el identificador del paquete: ");
            String paquete = scanner.next();
            String consulta = usuario + "|" + paquete;
            pOut.println(consulta);
        
            String respuesta = pIn.readLine();
            System.out.println("Estado del paquete: " + respuesta);
    
            ejecutar = false;
        }
        //scanner.close();
        //stdIn.close(); // Mueve el cierre de stdIn fuera de este método
    }
    

    public synchronized void procesar3(String id,BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut) throws IOException {
        boolean ejecutar = true;

        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); // Mover stdIn aquí
        Scanner scanner = new Scanner(System.in);
        while (ejecutar) {
            System.out.print("Ingrese el identificador de usuario: ");
            String usuario = scanner.next();
            System.out.print("Ingrese el identificador del paquete: ");
            String paquete = scanner.next();
            String consulta = usuario + "|" + paquete;
            for (int i = 0; i < 31; i++) {
                //aqui toca hacer la verificacion de nuevo 
                pOut.println(consulta);
                String respuesta = pIn.readLine();
                System.out.println("Estado del paquete: " + respuesta);
            }
            ejecutar = false;
        }
        //scanner.close();
        //stdIn.close(); // Mueve el cierre de stdIn fuera de este método
    }
}

