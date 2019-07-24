package mx.unam.ciencias.icc.red;

import java.io.IOException;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosCodigoGenetico;
import mx.unam.ciencias.icc.CampoCodigoGenetico;
import mx.unam.ciencias.icc.CodigoGenetico;

/**
 * Clase para servidores de bases de datos de código genético.
 */
public class ServidorBaseDeDatosCodigoGenetico
    extends ServidorBaseDeDatos<CodigoGenetico> {

    /**
     * Construye un servidor de base de datos de código genético.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param archivo el archivo en el disco del cual cargar/guardar la base de
     *                datos.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatosCodigoGenetico(int puerto, String archivo)
        throws IOException {
        super(puerto, archivo);
    }

    /**
     * Crea una base de datos de códigos genéticos.
     * @return una base de datos de códigos genéticos.
     */
    @Override public
    BaseDeDatos<CodigoGenetico, CampoCodigoGenetico> creaBaseDeDatos() {
        BaseDeDatosCodigoGenetico bdd = new BaseDeDatosCodigoGenetico();
        return bdd;
    }
}
