package mx.unam.ciencias.icc.red;

/**
 * Enumeración para los mensajes que se transmiten en una conexión entre el
 * servidor y el cliente de bases de datos.
 */
public enum Mensaje {

    /**
     * Si el servidor recibe este mensaje, contesta enviando toda la base de
     * datos. Si el cliente recibe este mensaje, entonces comienza a cargar la
     * base de datos.
     */
    BASE_DE_DATOS,

    /**
     * El interlocutor agregó un registro. Si este mensaje es recibido por el
     * servidor o el cliente, inmediatamente después recibirá un registro que
     * debe agregar a la base de datos.
     */
    REGISTRO_AGREGADO,

    /**
     * El interlocutor eliminó un registro. Si este mensaje es recibido por el
     * servidor o el cliente, inmediatamente después recibirá un registro que
     * debe eliminar de la base de datos.
     */
    REGISTRO_ELIMINADO,

    /**
     * El interlocutor modificó un registro. Si este mensaje es recibido por el
     * servidor o el cliente, inmediatamente después recibirá dos registros: el
     * primero será el registro original, y el segundo el registro modificado.
     */
    REGISTRO_MODIFICADO,

    /**
     * El interlocutor solicita una desconexión limpia. Tanto el servidor como
     * cliente cierran la conexión correspondiente.
     */
    DESCONECTAR,

    /**
     * El servidor debe detenerse, desconectando a todos los clientes que
     * pudieran estar conectados. Los clientes ignoran este mensaje, después de
     * imprimir una advertencia en consola.
     */
    DETENER_SERVICIO,

    /**
     * Mensaje de eco. El servidor y el cliente deben regresar el mensaje de eco
     * cuando reciban el mensaje de eco. Es necesario para poder probar el
     * servidor.
     */
    ECO,

    /**
     * El mensaje no es reconocido.
     */
    INVALIDO;

    /* El prefijo para eventos. */
    private static final String MENSAJE = "|=MENSAJE:";

    /**
     * Descifra un cadena recibida y la traduce a un mensaje.
     * @param mensaje la cadena de texto con el mensaje. La cadena recibida debe
     *        comenzar con "|=MENSAJE:", seguido del mensaje, de otra forma se le
     *        considerará inválido.
     * @return el mensaje correspondiente a la línea.
     */
    public static Mensaje getMensaje(String mensaje) {
        if(mensaje.startsWith(MENSAJE)){

          String m = mensaje.replace(MENSAJE, "");
          switch(m){
            case "BASE_DE_DATOS":
              return BASE_DE_DATOS;
            case "REGISTRO_AGREGADO":
              return REGISTRO_AGREGADO;
            case "REGISTRO_ELIMINADO":
              return REGISTRO_ELIMINADO;
            case "REGISTRO_MODIFICADO":
              return REGISTRO_MODIFICADO;
            case "DESCONECTAR":
              return DESCONECTAR;
            case "DETENER_SERVICIO":
              return DETENER_SERVICIO;
            case "ECO":
              return ECO;
            default:
              return INVALIDO;
          }
        }else{
          return INVALIDO;
        }
    }

    /**
     * Genera una cadena con un mensaje válido para {@link getMensaje}.
     * @return la cadena con el mensaje correspondiente al mensaje.
     */
    public String toString() {
        switch(this){
          case BASE_DE_DATOS:
            return this.MENSAJE + "BASE_DE_DATOS";
          case REGISTRO_AGREGADO:
            return this.MENSAJE + "REGISTRO_AGREGADO";
          case REGISTRO_ELIMINADO:
            return this.MENSAJE + "REGISTRO_ELIMINADO";
          case REGISTRO_MODIFICADO:
            return this.MENSAJE + "REGISTRO_MODIFICADO";
          case DESCONECTAR:
            return this.MENSAJE + "DESCONECTAR";
          case DETENER_SERVICIO:
            return this.MENSAJE + "DETENER_SERVICIO";
          case ECO:
            return this.MENSAJE + "ECO";
          case INVALIDO:
            return this.MENSAJE + "INVALIDO";
        }
        return null;
    }
}
