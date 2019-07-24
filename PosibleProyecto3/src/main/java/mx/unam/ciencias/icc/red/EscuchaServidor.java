package mx.unam.ciencias.icc.red;

/**
 * Interfaz para los escuchas de un servidor.
 */
@FunctionalInterface
public interface EscuchaServidor {

    /**
     * Los escuchas de servidor únicamente procesan mensajes, utilizando el
     * formato de {@link java.io.PrintWriter#printf(String,Object...)}.
     * @param formato una cadena de formato.
     * @param argumentos los argumentos a formatear.
     */
    public void procesaMensaje(String formato, Object ... argumentos);
}
 
