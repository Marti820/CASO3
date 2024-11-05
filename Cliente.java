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
        System.out.println("Cuentos desea generar?");

        try {
            clavePublicaServidor = leerClavePublica("publicKey.key");
        } catch (Exception e) {
            System.err.println("Error al cargar la llave: " + e.getMessage());
            e.printStackTrace();
        }
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

    public static PublicKey leerClavePublica(String rutaArchivo) throws Exception {
        byte[] bytesClavePublica = Files.readAllBytes(Paths.get(rutaArchivo));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytesClavePublica);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }


    public static byte[] cifrarReto(byte[] reto, PublicKey clavePublica) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
        return cipher.doFinal(reto);
    }

     public static byte[] generarReto(int tamanio) {
        byte[] reto = new byte[tamanio];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(reto);
        return reto;
    }

    public static boolean verificarFirma(byte[] mensaje, byte[] firma, PublicKey clavePublica) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(clavePublica); 
        signature.update(mensaje); 
        return signature.verify(firma); 
    }
    

    public static BigInteger generarGy(BigInteger[] PyG){
        
        BigInteger p = PyG[0];
        BigInteger g = PyG[1];

        SecureRandom random = new SecureRandom();

        BigInteger y;

        do{
            y = new BigInteger(p.bitLength()-1,random);
        } while (y.compareTo(BigInteger.ONE)<0 || y.compareTo(p.subtract(BigInteger.ONE))>=0);

        BigInteger gy = g.modPow(y,p);

        return gy;

    }

    public static BigInteger generarZ(BigInteger gx, BigInteger y, BigInteger p){

        BigInteger z = gx.modPow(y, p);

        return z;
    }


    public static Map<String,Key> generarClavesMap(BigInteger z) throws NoSuchAlgorithmException{
        byte[] claveBytes = z.toByteArray();
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashClave = digest.digest(claveBytes);

        byte[] claveCifrado1 = Arrays.copyOfRange(hashClave,0,32);
        byte[] claveHMAC1 = Arrays.copyOfRange(hashClave,32,64);

        Key claveCifrado = new SecretKeySpec(claveCifrado1, "AES");
        Key claveHMAC = new SecretKeySpec(claveHMAC1, "HmacSHA256");


        Map<String, Key> claves = new HashMap<>();
        claves.put("claveCifrado", claveCifrado);
        claves.put("claveHMAC", claveHMAC);

        return claves;
    }


    public static byte[] cifradoSimetrico(byte[] iv, Key claveCifrado, byte[] id) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{ 
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, claveCifrado,ivSpec);

        return cipher.doFinal(id);

    }

    public static byte[] cifradoHMAC(Key claveHMAC, byte[] id) throws NoSuchAlgorithmException, InvalidKeyException{

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(claveHMAC);

        return mac.doFinal(id);

    }
}
