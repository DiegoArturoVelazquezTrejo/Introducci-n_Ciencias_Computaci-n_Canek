package mx.unam.ciencias.icc.red;

import java.io.IOException;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.EscuchaBaseDeDatos;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Registro;

/**
 * <p>Clase para las conexiones de clientes de la base de datos.</p>
 *
 * <p>Las conexiones de clientes agregan un escucha a la base de datos, para que
 * cuando el usuario modifique la base de datos, el mensaje correspondiente sea
 * automáticamente transmitido al servidor.</p>
 *
 * <p>El escucha evita que las modificaciones a la base de datos originadas en
 * el servidor sean transmitidas de regreso al mismo.</p>
 *
 * <p>Las conexiones de clientes manejan el mensaje {@link
 * Mensaje#BASE_DE_DATOS} cargando la base de datos de su entrada.</p>
 */
public class ConexionCliente<R extends Registro<R, ?>>
    extends Conexion<R> {

    /* Bandera para ignorar eventos. */
    private boolean ignorarEventos;
    /* El escucha de la base de datos. */
    private EscuchaBaseDeDatos<R> escucha;

    /**
     * Define el estado inicial de una nueva conexión de cliente. Además de
     * hacer lo mismo que {@link Conexion#Conexion}, agrega un escucha a la base
     * de datos para notificar al servidor de cambios hechos por el usuario.
     * También manda el mensaje de petición de la base de datos.
     * @param bdd la base de datos.
     * @param enchufe el enchufe de la conexión ya establecida.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ConexionCliente(BaseDeDatos<R, ?> bdd, Socket enchufe) throws IOException {
        super(bdd, enchufe);
        this.escucha = (e, r1, r2) -> manejaEventos(e, r1, r2);
        bdd.agregaEscucha(this.escucha);
        out.write(Mensaje.BASE_DE_DATOS.toString());
        out.newLine();
        out.flush();
    }

    /* Maneja los eventos de la base de datos. */
    private void manejaEventos(EventoBaseDeDatos evento, R registro1, R registro2) {
        if (ignorarEventos)
            return;
        switch (evento) {
        case REGISTRO_AGREGADO:
            enviaAlteracionRegistro(Mensaje.REGISTRO_AGREGADO, registro1);
            break;
        case REGISTRO_ELIMINADO:
            enviaAlteracionRegistro(Mensaje.REGISTRO_ELIMINADO, registro1);
            break;
        case REGISTRO_MODIFICADO:
            enviaModificacionRegistro(registro1, registro2);
            break;
        }
    }

    /**
     * Agrega el primer registro a la base de datos, evitando que el escucha de
     * la base de datos notifique de nuevo al servidor del evento.
     */
    @Override protected synchronized void agregaRegistro() {
        ignorarEventos = true;
        super.agregaRegistro();
        ignorarEventos = false;
    }

    /**
     * Elimina el primer registro de la base de datos, evitando que el escucha de
     * la base de datos notifique de nuevo al servidor del evento.
     */
    @Override protected synchronized void eliminaRegistro() {
      ignorarEventos = true;
      super.eliminaRegistro();
      ignorarEventos = false;
    }

    /**
     * Modifica el primer registro de la base de datos con el segundo registro,
     * evitando que el escucha de la base de datos notifique de nuevo al
     * servidor del evento.
     */
    @Override protected synchronized void modificaRegistro() {
      ignorarEventos = true;
      super.modificaRegistro();
      ignorarEventos = false;
    }

    /**
     * Maneja un mensaje {@link Mensaje#BASE_DE_DATOS} para recibir la base de
     * datos, evitando que su escucha notifique de nuevo al servidor del evento.
     */
    @Override protected void manejaBaseDeDatos() {
        try{
          ignorarEventos = true;
          this.bdd.carga(this.in);
          ignorarEventos = false;
        }catch(IOException e){
          this.avisaEscuchas(EventoConexion.ERROR, "Error al cargar la base de datos.");
        }
    }

    /**
     * Maneja un mensaje {@link Mensaje#DETENER_SERVICIO}.
     */
    @Override protected void manejaDetenerServicio() {
        /* El cliente no hace nada con este mensaje. */
    }

    /**
     * Maneja un mensaje {@link Mensaje#ECO}.
     */
    @Override protected void manejaEco() {
        /* El cliente no hace nada con este mensaje. */
    }

    /**
     * Regresa -1.
     * @return -1.
     */
    @Override public int getSerial() {
        return -1;
    }

    /**
     * Elimina el escucha de la base de datos y cierra la conexión.
     */
    @Override public void desconecta() {
        ignorarEventos = true;
        super.desconecta();
        ignorarEventos = false;
    }
}
