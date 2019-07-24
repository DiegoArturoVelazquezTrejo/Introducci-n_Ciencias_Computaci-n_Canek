package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link CodigoGenetico}.
 */
public enum CampoCodigoGenetico {

    /** EL nombre de la cadena del código genético. */
    NOMBRE,
    /** ADN del código genético. */
    ADN,
    /** El valor numérico del código genético. */
    VALORNUMERICO,
    /** La traducción del código genético. */
    TRADUCCION,
    /** La categoría del código genético .*/
    CATEGORIA,
    /** Número de cadenas que tiene el código genético .*/
    NUMEROCADENAS;


    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
        switch(this){
          case NOMBRE: return "Nombre";
          case ADN: return "Adn";
          case VALORNUMERICO: return "Valor numerico";
          case TRADUCCION: return "Traduccion";
          case CATEGORIA: return "Categoria";
          case NUMEROCADENAS: return "Numero Cadenas";
          default: throw new IllegalArgumentException();
        }
    }
}
