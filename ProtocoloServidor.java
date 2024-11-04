import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ProtocoloServidor {
    private Map<String, Integer> tablaPaquetes;

    public ProtocoloServidor() {
        // Inicializar la tabla con login de usuario, identificador de paquete y estado
        tablaPaquetes = new HashMap<>();
        tablaPaquetes.put("user1|package1", EstadoPaquete.ENOFICINA);
        tablaPaquetes.put("user2|package2", EstadoPaquete.RECOGIDO);
        tablaPaquetes.put("user3|package3", EstadoPaquete.ENCLASIFICACION);
        tablaPaquetes.put("user4|package4", EstadoPaquete.DESPACHADO);
        // Agrega más datos hasta completar los 32 paquetes
    }

    public void procesar(BufferedReader pIn, PrintWriter pOut) throws IOException {
        String inputLine;
        
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
        }
    }
}
