package mx.unam.ciencias.icc.red;

/**
 * Enumeración para eventos de la conexión.
 */
public enum EventoConexion {
 
    /**
     * La conexión ha terminado.
     */
    DESCONEXION,

    /**
     * La conexión modificó la base de datos.
     */
    MODIFICACION,

    /**
     * La conexión solicita detener el servidor.
     */
    TERMINACION,

    /**
     * La conexión mandó una advertencia.
     */
    ADVERTENCIA,

    /**
     * La conexión mandó un error.
     */
    ERROR;
}
