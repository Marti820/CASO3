import java.io.IOException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws IOException {
        System.out.println("1. Generar Clientes Concurrentes");
        System.out.println("Cuentos desea generar?");
        Scanner scanner = new Scanner(System.in);
        int numClient = scanner.nextInt();
        //ThreadCliente cliente = new ThreadCliente();
        //cliente.start();
        for (int i = 0; i < numClient; i++) {
            //ThreadCliente cliente = new ThreadCliente();
            //cliente.start(); 
            System.out.println(i);
            try {
                Thread.sleep(100); 
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrumpido: " + ie.getMessage());
            }
        }
        
        scanner.close(); 
        /* Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        System.out.println("===== Menú de Opciones =====");
        System.out.println("1. Generar Clientes Concurrentes");
        System.out.println("2. Generar un Cliente con muchas solicitudes");
        System.out.print("Elige una opción: ");

        opcion = scanner.nextInt();

        if (opcion == 1) {
            System.out.print("Ingrese el número de clientes a conectar: ");
            int numClient = scanner.nextInt();
            System.out.println("GENERANDO LAS INSTANCIAS: ");
            for (int i = 0; i < numClient; i++) {
                ThreadCliente cliente = new ThreadCliente();
                cliente.start(); // Inicia el cliente en su propio hilo
                try {
                    Thread.sleep(100); // Retraso opcional para evitar sobrecarga
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrumpido: " + ie.getMessage());
                }
            }
            

        } else if (opcion == 2) {
            System.out.println("GENERANDO EL CLIENTE");
            // Aquí puedes añadir la lógica de la segunda opción
        } else {
            System.out.println("Opción inválida. Por favor, elige 1 o 2.");
        }
        System.out.println();
        

        scanner.close(); */
    }
}
