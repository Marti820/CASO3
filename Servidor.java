import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class Servidor {
    private static PublicKey clavePublica;
    private static PrivateKey clavePrivada;
    public static final int PUERTO = 3400;
    private static boolean continuar = true;

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 2) {
            System.out.println("===== Menú de Opciones =====");
            System.out.println("1. Generar llaves asimetricas");
            System.out.println("2. Iniciar servidor");
            System.out.print("Elige una opción: ");
            
            opcion = scanner.nextInt();

            if (opcion == 1) {
                System.out.println("GENERANDO LLAVES EN LOS ARCHIVOS: ");
                generarLlaves();
            } else if (opcion == 2) {
                System.out.println("INICIANDO EL SERVIDOR");
                try {
                    //clavePublica = leerClavePublica("publicKey.key");
                    //clavePrivada = leerClavePrivada("privateKey.key");
                    iniciarServidor();
                } catch (Exception e) {
                    System.err.println("Error al cargar las llaves: " + e.getMessage());
                    e.printStackTrace();
                }
            }
             else {
                System.out.println("Opción inválida. Por favor, elige 1 o 2.");
            }
            System.out.println(); 
        }
        scanner.close(); 
    }
    public static void generarLlaves() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        PublicKey publica = keyPair.getPublic();
        PrivateKey privada = keyPair.getPrivate();

        // Guardar clave pública en formato PEM
        try (FileWriter fos = new FileWriter("publicKey.pem")) {
            fos.write("-----BEGIN PUBLIC KEY-----\n");
            fos.write(Base64.getEncoder().encodeToString(publica.getEncoded()));
            fos.write("\n-----END PUBLIC KEY-----\n");
        }

        // Guardar clave privada en formato PEM
        try (FileWriter fos = new FileWriter("privateKey.pem")) {
            fos.write("-----BEGIN PRIVATE KEY-----\n");
            fos.write(Base64.getEncoder().encodeToString(privada.getEncoded()));
            fos.write("\n-----END PRIVATE KEY-----\n");
        }

        System.out.println("Las llaves han sido generadas y guardadas en archivos PEM");
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
