import java.io.BufferedReader;
import java.io.DataOutputStream;
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
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class ProtocoloServidor {
    private Map<String, Integer> tablaPaquetes;

    public ProtocoloServidor() {
        tablaPaquetes = new HashMap<>();
       /*  tablaPaquetes.put("user1|package1", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user2|package2", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user3|package3", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user4|package4", EstadoPaquete.DESPACHADO); */
        tablaPaquetes.put("user1|package1", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user2|package2", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user3|package3", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user4|package4", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user5|package5", EstadoPaquete.ENENTREGA);
        tablaPaquetes.put("user6|package6", EstadoPaquete.ENTREGADO);
        tablaPaquetes.put("user7|package7", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user8|package8", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user9|package9", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user10|package10", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user11|package11", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user12|package12", EstadoPaquete.ENENTREGA);
        tablaPaquetes.put("user13|package13", EstadoPaquete.ENTREGADO);
        tablaPaquetes.put("user14|package14", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user15|package15", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user16|package16", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user17|package17", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user18|package18", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user19|package19", EstadoPaquete.ENENTREGA);
        tablaPaquetes.put("user20|package20", EstadoPaquete.ENTREGADO);
        tablaPaquetes.put("user21|package21", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user22|package22", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user23|package23", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user24|package24", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user25|package25", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user26|package26", EstadoPaquete.ENENTREGA);
        tablaPaquetes.put("user27|package27", EstadoPaquete.ENTREGADO);
        tablaPaquetes.put("user28|package28", EstadoPaquete.DESPACHADO);
        tablaPaquetes.put("user29|package29", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user30|package30", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user31|package31", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user32|package32", EstadoPaquete.DESPACHADO);
    }

    public void procesar(BufferedReader pIn, PrintWriter pOut, DataOutputStream pOut2) throws Exception {
        /* String inputLine;
        
        while ((inputLine = pIn.readLine()) != null) {
            System.out.println("Consulta recibida: " + inputLine);
            String[] datos = inputLine.split("\\|");
            
            if (datos.length == 2) {
                String usuario = datos[0];
                String paquete = datos[1];
                String clave = usuario + "|" + paquete;

                // Buscar el estado en la tabla
                int estado = tablaPaquetes.getOrDefault(clave, EstadoPaquete.DESCONOCIDO);
                String respuesta = EstadoPaquete.getNombreEstado(estado);

                pOut.println(respuesta);  // Enviar la respuesta al cliente
            } else {
                pOut.println("Formato incorrecto. Use: <usuario>|<paquete>");
            }
        } */
      
            String inputLine;
            String outputLine ="";
            int estado = 0;
            BigInteger x;
            BigInteger p;

            PrivateKey llavePriv= leerClavePrivada("privateKey.pem");
            PublicKey llavePub = leerClavePublica("publicKey.pem");
            System.out.println("llaves leidas");
   

            while (estado < 17 && (inputLine = pIn.readLine()) != null) {
                System.out.println("Entrada a procesar: " + inputLine);
                switch (estado) {
                    case 0:
                        if (inputLine.equalsIgnoreCase("SECINIT")) {
                            outputLine = "Listo_SECINIT";
                            estado++;
                        } else {
                            outputLine = "SECINIT_FALLA";
                            estado = 0;
                        }
                        break;
                    case 1:
                        String RBase64 =inputLine;
                        byte[] R = Base64.getDecoder().decode(RBase64);
                        byte[] rta = descifrarReto(R, llavePriv);
                        //enviar rta
                        outputLine = Base64.getEncoder().encodeToString(rta);
                        estado++;
                        break;
                    case 2:
                        if (inputLine.equalsIgnoreCase("OK")){
                        //7 generar G P G^X
                        BigInteger[] PyG = generarPyG();
                        p = PyG[0];
                        BigInteger g = PyG[1];
                        BigInteger[] Gx_x = generarGx(PyG);
                        BigInteger gx = Gx_x[0];
                        x = Gx_x[1];

                        byte[] firma = firmarMensaje(p, g, gx, llavePriv);


                        enviarBigInteger(pOut2, p);
                        enviarBigInteger(pOut2, g);
                        enviarBigInteger(pOut2, gx);
                        enviarFirma(pOut2, firma);
                        
                        System.out.println("Valores enviados al cliente");
                        estado++;
                        }
                        else{
                        estado = 0;
                        }
                    break;
                    case 3:
                        try {
                            String gyString = inputLine;
                            byte[] gyByte = Base64.getDecoder().decode(gyString);
                            BigInteger gy = new BigInteger(gyByte);
                            //BigInteger z = generarZ(gy, x, p);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    case 4:
                        try {
                            System.out.println("Consulta recibida: " + inputLine);
                            String[] datos = inputLine.split("\\|");
                            
                            if (datos.length == 2) {
                                String usuario = datos[0];
                                String paquete = datos[1];
                                String clave = usuario + "|" + paquete;
                
                                // Buscar el estado en la tabla
                                int estadoTablas = tablaPaquetes.getOrDefault(clave, EstadoPaquete.DESCONOCIDO);
                                String respuesta = EstadoPaquete.getNombreEstado(estadoTablas);
                
                                outputLine = respuesta;  // Enviar la respuesta al cliente
                            } else {
                                outputLine = "Formato incorrecto. Use: <usuario>|<paquete>";
                            }
                            estado++;
                        } catch (Exception e) {
                            outputLine = "ERROR en argumento esperado";
                            estado = 0;
                        }
                        break;
                    default:
                    outputLine = "ERROR";
                    estado = 0;
                }
                pOut.println(outputLine);
            }
    }
    public static void generarDH(BigInteger[] pyg){

    }

    public static BigInteger[] generarPyG(){
        try {
            // Cambia esta ruta a la ubicación de tu openssl
            String opensslPath = "\"D:\\OpenSSL-1.1.1h_win32\\openssl.exe\"";
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
        String clavePublicaPEM = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        clavePublicaPEM = clavePublicaPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] bytesClavePublica = Base64.getDecoder().decode(clavePublicaPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytesClavePublica);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public static PrivateKey leerClavePrivada(String rutaArchivo) throws Exception {
        String clavePrivadaPEM = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        clavePrivadaPEM = clavePrivadaPEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");  // Elimina espacios en blanco y saltos de línea
        byte[] bytesClavePrivada = Base64.getDecoder().decode(clavePrivadaPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytesClavePrivada);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    public static byte[] descifrarReto(byte[] R, PrivateKey clavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, clavePrivada);
        return cipher.doFinal(R);
    }

    public static byte[] firmarMensaje(BigInteger p, BigInteger g, BigInteger gx, PrivateKey clavePrivada) throws Exception {
        byte[] pByte = p.toByteArray();
        byte[] gByte = g.toByteArray();
        byte[] gxByte = gx.toByteArray();

        byte[] concat = new byte[pByte.length + gByte.length + gxByte.length];
        System.arraycopy(pByte, 0, concat, 0, pByte.length);
        System.arraycopy(gByte, 0, concat, pByte.length, gByte.length);
        System.arraycopy(gxByte, 0, concat, pByte.length + gByte.length, gxByte.length);

        Signature signature = Signature.getInstance("SHA1withRSA"); 
        signature.initSign(clavePrivada);
        signature.update(concat); 
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

    public static BigInteger generarZ(BigInteger gy, BigInteger x, BigInteger p) throws NoSuchAlgorithmException{

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

    private static void enviarBigInteger(DataOutputStream salida, BigInteger valor) throws IOException {
        byte[] valorBytes = valor.toByteArray();
        salida.writeInt(valorBytes.length); // Enviar el tamaño del byte array
        salida.write(valorBytes); // Enviar el contenido del byte array
    }

    // Método para enviar la firma
    private static void enviarFirma(DataOutputStream salida, byte[] firma) throws IOException {
        salida.writeInt(firma.length); // Enviar el tamaño del byte array de la firma
        salida.write(firma); // Enviar el contenido de la firma
    }
    
}
