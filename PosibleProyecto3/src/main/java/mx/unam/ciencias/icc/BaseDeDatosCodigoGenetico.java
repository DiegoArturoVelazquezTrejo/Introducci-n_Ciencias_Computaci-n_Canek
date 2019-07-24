package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de código genético.
 */
public class BaseDeDatosCodigoGenetico extends BaseDeDatos<CodigoGenetico, CampoCodigoGenetico> {
    /**
     * Crea un código genético en blanco.
     * @return un código genético en blanco.
     */
     @Override public CodigoGenetico creaRegistro() {
         return  new CodigoGenetico(null, null, 0 , 0, null);
     }
}
