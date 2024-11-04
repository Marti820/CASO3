public class EstadoPaquete {
    public static final int ENOFICINA = 0;
    public static final int RECOGIDO = 1;
    public static final int ENCLASIFICACION = 2;
    public static final int DESPACHADO = 3;
    public static final int ENENTREGA = 4;
    public static final int ENTREGADO = 5;
    public static final int DESCONOCIDO = 6;

    public static String getNombreEstado(int estado) {
        switch (estado) {
            case ENOFICINA:
                return "ENOFICINA";
            case RECOGIDO:
                return "RECOGIDO";
            case ENCLASIFICACION:
                return "ENCLASIFICACION";
            case DESPACHADO:
                return "DESPACHADO";
            case ENENTREGA:
                return "ENENTREGA";
            case ENTREGADO:
                return "ENTREGADO";
            default:
                return "DESCONOCIDO";
        }
    }
}
