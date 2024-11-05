import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                    clavePublica = leerClavePublica("publicKey.key");
                    clavePrivada = leerClavePrivada("privateKey.key");
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

        System.out.println("Las llaves han sido generadas y guardadas en archivos planos.");
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

    public static void generarDH(BigInteger[] pyg){

    }

    public static BigInteger[] generarPyG(){
        try {
            // Cambia esta ruta a la ubicación de tu openssl
            String opensslPath = "C:\\Users\\User\\Desktop\\OpenSSL-1.1.1h_win32\\openssl";
            String command = opensslPath + " dhparam -text 1024";

            // Ejecutar el comando
            Process process = Runtime.getRuntime().exec(command);

            // Leer la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            // Almacenar toda la salida
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            process.waitFor();

            // La salida completa
            String outputString = output.toString();

            // Expresión regular para capturar el valor de `prime` y `generator`
            Pattern primePattern = Pattern.compile("prime:\\s*((?:[0-9a-fA-F]{2}:?\\s*)+)", Pattern.MULTILINE);
            Pattern generatorPattern = Pattern.compile("generator:\\s*([0-9a-fA-F]+)");

            Matcher primeMatcher = primePattern.matcher(outputString);
            Matcher generatorMatcher = generatorPattern.matcher(outputString);

            BigInteger p = null;
            BigInteger g = null;

            if (primeMatcher.find()) {
                // Extraemos el valor de `prime`, quitamos los espacios y los ':' para formar un solo número hexadecimal
                String primeHex = primeMatcher.group(1).replaceAll("[\\s:]", "");
                p = new BigInteger(primeHex, 16);  // Convertir hexadecimal a BigInteger
            } else {
                System.out.println("No se encontró 'prime' en la salida.");
            }

            if (generatorMatcher.find()) {
                String generatorHex = generatorMatcher.group(1);
                g = new BigInteger(generatorHex, 16);  // Convertir hexadecimal a BigInteger
            } else {
                System.out.println("No se encontró 'generator' en la salida.");
            }

            // Devolver P y G en un array
            return new BigInteger[]{p, g};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static PublicKey leerClavePublica(String rutaArchivo) throws Exception {
        byte[] bytesClavePublica = Files.readAllBytes(Paths.get(rutaArchivo));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytesClavePublica);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }


    public static PrivateKey leerClavePrivada(String rutaArchivo) throws Exception {
        byte[] bytesClavePrivada = Files.readAllBytes(Paths.get(rutaArchivo));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytesClavePrivada);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public static byte[] descifrarReto(byte[] R, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        return cipher.doFinal(R);
    }

    public static byte[] firmarMensaje(byte[] mensaje, PrivateKey clavePrivada) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA"); 
        signature.initSign(clavePrivada);
        signature.update(mensaje); 
        return signature.sign();
    }

    public static BigInteger[] generarGx(BigInteger[] PyG){
        
        BigInteger p = PyG[0];
        BigInteger g = PyG[1];

        SecureRandom random = new SecureRandom();

        BigInteger x;

        do{
            x = new BigInteger(p.bitLength()-1,random);
        } while (x.compareTo(BigInteger.ONE)<0 || x.compareTo(p.subtract(BigInteger.ONE))>=0);

        BigInteger gx = g.modPow(x,p);

        return new BigInteger[]{gx, x};

    }

    public static BigInteger generarZ(BigInteger gy, BigInteger x, BigInteger p){

        BigInteger z = gy.modPow(x, p);

        return z;
    }

    public static Map<String,byte[]> generarClavesMap(BigInteger z) throws NoSuchAlgorithmException{
        byte[] claveBytes = z.toByteArray();
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashClave = digest.digest(claveBytes);

        byte[] claveCifrado = Arrays.copyOfRange(hashClave,0,32);
        byte[] claveHMAC = Arrays.copyOfRange(hashClave,32,64);

        Map<String, byte[]> claves = new HashMap<>();
        claves.put("claveCifrado", claveCifrado);
        claves.put("claveHMAC", claveHMAC);

        return claves;
    }

    public static byte[] generarIV() {
        byte[] iv = new byte[16]; 
        SecureRandom random = new SecureRandom(); 
        random.nextBytes(iv); 
        return iv;
    }

    public static byte[] cifradoSimetrico(byte[] iv, Key claveCifrado, byte[] estado) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{ 
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, claveCifrado,ivSpec);

        return cipher.doFinal(estado);

    }

    public static byte[] cifradoHMAC(Key claveHMAC, byte[] estado) throws NoSuchAlgorithmException, InvalidKeyException{

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(claveHMAC);

        return mac.doFinal(estado);

    }
    
}
