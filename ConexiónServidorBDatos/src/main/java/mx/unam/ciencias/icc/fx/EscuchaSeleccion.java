package mx.unam.ciencias.icc.fx;

/**
 * Escucha para la selección de la tabla de registros.
 */
@FunctionalInterface
public interface EscuchaSeleccion {

    /**
     * Notifica que cambió el número de registros seleccionados.
     * @param n el número de registros seleccionados.
     */
    public void renglonesSeleccionados(int n);
}
