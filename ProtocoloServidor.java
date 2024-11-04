import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
