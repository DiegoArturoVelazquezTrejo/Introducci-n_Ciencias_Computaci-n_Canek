package mx.unam.ciencias.icc.red;

import mx.unam.ciencias.icc.Registro;

/**
 * Escucha para eventos de conexiones.
 */
@FunctionalInterface
public interface EscuchaConexion<R extends Registro<R, ?>> {

    /**
     * Notifica de un evento que ocurrió en la comunicación entre el cliente y
     * el servidor.
     * @param conexion la conexión donde ocurrió el evento.
     * @param evento el evento ocurrido.
     * @param mensaje el mensaje asociado al evento.
     */
    public void eventoOcurrido(Conexion<R> conexion,EventoConexion evento,String mensaje);
}
