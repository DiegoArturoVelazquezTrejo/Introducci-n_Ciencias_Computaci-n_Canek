package mx.unam.ciencias.icc;

/**
 * Interface para escuchas de bases de datos.
 */
@FunctionalInterface
public interface EscuchaBaseDeDatos<T extends Registro> {
 
    /**
     * Nos dice si hubo algún cambio en la base de datos.
     * @param evento el tipo de cambio.
     * @param registro1 el registro afectado, o <code>null</code> si la base de
     *                  datos fue limpiada.
     * @param registro2 el registro dónde obtener la información de modificación
     *                   del registro, <code>null</code> en otro caso.
     */
    public void baseDeDatosModificada(EventoBaseDeDatos evento,
                                      T registro1, T registro2);
}
