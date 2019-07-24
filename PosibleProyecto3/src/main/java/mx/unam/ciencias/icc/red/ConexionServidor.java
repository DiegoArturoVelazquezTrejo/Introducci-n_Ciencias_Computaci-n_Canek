package mx.unam.ciencias.icc.red;

import java.io.IOException;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Registro;

/**
 * <p>Clase para las conexiones de servidor de la base de datos.</p>
 *
 * <p>Las conexiones de servidor manejan el mensaje {@link
 * Mensaje#DETENER_SERVICIO} notificando del evento {@link
 * EventoConexion#TERMINACION} a sus escuchas; el mensaje {@link Mensaje#ECO}
 * enviando el mismo mensajes de regreso; y el mensaje {@link
 * Mensaje#BASE_DE_DATOS} guardando la base de datos en su salida.</p>
 *
 * <p>Además, las conexiones de servidor cuentan con un método {@link #informa},
 * que permite a una conexión del servidor propagar a otra conexión un mensaje y
 * sus datos correspondientes.</p>
 */
public class ConexionServidor<R extends Registro<R, ?>>
    extends Conexion<R> {

    /* Contador de números seriales. */
    private static int contadorSerial;

    /* El número serial único de la conexión. */
    private int serial;

    /**
     * Define el estado inicial de una nueva conexión de cliente. Hace lo mismo
     * que {@link Conexion#Conexion}.
     * @param bdd la base de datos.
     * @param enchufe el enchufe de la conexión ya establecida.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ConexionServidor(BaseDeDatos<R, ?> bdd,
                            Socket enchufe) throws IOException {
        // Aquí va su código.
        super(bdd, enchufe);
        this.serial = ++this.contadorSerial;

    }

    /**
     * Maneja un mensaje {@link Mensaje#BASE_DE_DATOS} para enviar la base de
     * datos.
     */
    @Override protected void manejaBaseDeDatos() {
        // Aquí va su código.
        try{
          this.out.write(Mensaje.BASE_DE_DATOS.toString());
          this.out.newLine();
          this.bdd.guarda(this.out);
          this.out.newLine();
          this.out.flush();
        }catch(IOException e){
          e.printStackTrace();
        }
    }

    /**
     * Maneja un mensaje {@link Mensaje#DETENER_SERVICIO}.
     */
    @Override protected void manejaDetenerServicio() {
        // Aquí va su código.
        avisaEscuchas(EventoConexion.TERMINACION, "Servicio detenido");
    }

    /**
     * Maneja un mensaje {@link Mensaje#ECO}.
     */
    @Override protected void manejaEco() {
        // Aquí va su código.
        try{
          this.out.write(Mensaje.ECO.toString());
          this.out.newLine();
          this.out.flush();
        }catch(IOException e){
          e.printStackTrace();
        }
    }

    /**
     * Informa a la conexión el mensaje y datos de la conexión recibida. Si la
     * conexión recibida es la misma que la que llama el método, nada ocurre.
     * @param conexion la conexión con el mensaje y datos a informar.
     */
    public void informa(Conexion<R> conexion) {
        if (this == conexion)
            return;
        switch (conexion.mensaje) {
        case REGISTRO_AGREGADO:
            enviaAlteracionRegistro(conexion.mensaje, conexion.registro1);
            break;
        case REGISTRO_ELIMINADO:
            enviaAlteracionRegistro(conexion.mensaje, conexion.registro1);
            break;
        case REGISTRO_MODIFICADO:
            enviaModificacionRegistro(conexion.registro1, conexion.registro2);
            break;
        case DESCONECTAR:
            enviaDesconectar();
            break;
        }

    }

    /**
     * Regresa un número serial único para cada conexión.
     * @return un número serial único para cada conexión.
     */
    @Override public int getSerial() {
        // Aquí va su código.
        return serial;
    }
}
