import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ProtocoloCliente {
    public synchronized void procesar(String id,BufferedReader stdIn, BufferedReader pIn, PrintWriter pOut, DataInputStream pIn2) throws Exception {
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
            
            //0b leer clave publica servidor 
            PublicKey llavePub = leerClavePublica("publicKey.pem");
            System.out.println("llave leidas");

            //1. SECINIT
            System.out.println("envia: SECINIT");
            pOut.println("SECINIT");

            //2a. CALCULAR R = C(K_W+, RETO)
            String respuesta1  = pIn.readLine();
            if (!"Listo_SECINIT".equals(respuesta1)){
               ejecutar = false;
            }
            System.out.println("CALCULANDO RETO");
            byte[] reto= generarReto(100);
            byte[] R = cifrarReto(reto, llavePub);
            //2b. enviar R
            System.out.println("ENVIANDO RETO");
            String RBase64 = Base64.getEncoder().encodeToString(R);
            pOut.println(RBase64); 
            //5 verificar RTA==R
            System.out.println("VERIFICAR RETO=RTA");
            String respuesta2 = pIn.readLine();
            byte[] rta = Base64.getDecoder().decode(respuesta2);
            if (Arrays.equals(reto, rta)){
                System.out.println("RETO==RTA BIEN");
                pOut.println("Ok");
            }
            else{
                System.out.println("RETO==RTA MAL :( ");
                pOut.println("ERROR");
            }
            //9. verificar firma
            BigInteger p = recibirBigInteger(pIn2);
            BigInteger g = recibirBigInteger(pIn2);
            BigInteger gx = recibirBigInteger(pIn2);
            byte[] firma = recibirFirma(pIn2);
            boolean verificada = verificarFirma(p, g, gx, firma, llavePub);
            if (verificada){
                System.out.println("Firma verificada");
                pOut.println("Ok");
            }
            else{
                System.out.println("Firma no verificada");
                pOut.println("ERROR");
            }
            /* 
            BigInteger gy = generarGy(null);
            BigInteger z = generarZ(null, null, null);
            Map<String, Key> claves = generarClavesMap(z);
            Key claveCifrado = claves.get("claveCifrado");
            Key claveHMAC = claves.get("claveHMAC");

            String gyString = Base64.getEncoder().encodeToString(gy.toByteArray());
            pOut.println(gyString);
            */

            if (true){
                String usuario = "user"+id;
                String paquete = "package"+id;
                String consulta = usuario + "|" + paquete;
                pOut.println(consulta);
            }
            String respuesta20 = pIn.readLine();
            System.out.println("Estado del paquete: " + respuesta20);
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
    public static PublicKey leerClavePublica(String rutaArchivo) throws Exception {
        String clavePublicaPEM = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        clavePublicaPEM = clavePublicaPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");  // Elimina espacios en blanco y saltos de línea
        byte[] bytesClavePublica = Base64.getDecoder().decode(clavePublicaPEM);
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

    public static boolean verificarFirma(BigInteger p, BigInteger g, BigInteger gx, byte[] firma, PublicKey clavePublica) throws Exception {
        
        byte[] pByte = p.toByteArray();
        byte[] gByte = g.toByteArray();
        byte[] gxByte = gx.toByteArray();

        // Concatenar los byte arrays en el mismo orden
        byte[] concat = new byte[pByte.length + gByte.length + gxByte.length];
        System.arraycopy(pByte, 0, concat, 0, pByte.length);
        System.arraycopy(gByte, 0, concat, pByte.length, gByte.length);
        System.arraycopy(gxByte, 0, concat, pByte.length + gByte.length, gxByte.length);

        // Crear una instancia de Signature para la verificación
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(clavePublica);

        // Actualizar la firma con los datos concatenados
        signature.update(concat);

        // Verificar la firma recibida
        return signature.verify(firma);
    }
    

    // Método para recibir un BigInteger
    private static BigInteger recibirBigInteger(DataInputStream entrada) throws IOException {
        int length = entrada.readInt(); // Leer el tamaño del byte array
        byte[] valorBytes = new byte[length];
        entrada.readFully(valorBytes); // Leer el contenido del byte array
        return new BigInteger(valorBytes); // Reconstruir el BigInteger
    }

    // Método para recibir la firma
    private static byte[] recibirFirma(DataInputStream entrada) throws IOException {
        int length = entrada.readInt(); // Leer el tamaño del byte array de la firma
        byte[] firma = new byte[length];
        entrada.readFully(firma); // Leer el contenido de la firma
        return firma;
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

