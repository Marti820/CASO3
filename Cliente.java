import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cliente {
    private static PublicKey clavePublicaServidor;
    public static void main(String[] args) throws IOException {
        System.out.println("----Menu----");
        System.out.println("1. Generar Clientes Concurrentes");
        //System.out.println("Cuentos desea generar?");
        System.out.println("2. Generar 1 Cliente 1 consulta");
        System.out.println("3. Generar 1 Cliente Muchas consultas");

        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        if (opcion==1){
            System.out.println("Cuentos desea generar?");
            int numClient = scanner.nextInt();
            //ThreadCliente cliente = new ThreadCliente();
            //cliente.start();
            for (int i = 0; i < numClient; i++) {
                
                ThreadCliente cliente = new ThreadCliente(String.valueOf(i+1));
                cliente.start(); 
            // System.out.println(i);
                try {
                    Thread.sleep(100); 
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrumpido: " + ie.getMessage());
                }
            }}
        else if (opcion==2){
            System.out.println("AQUI");
            ThreadCliente cliente = new ThreadCliente("0");
            cliente.start();
        }
        else if (opcion==3){
            ThreadCliente cliente = new ThreadCliente("-1");
            cliente.start();
        }
        //scanner.close();
       
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
