package main.java.mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link CodigoGenetico}.
 */
public enum CampoCodigoGenetico {

    /** La cadena del código genético. */
    ADN,
    /** La cadena del arn del código genético. */
    ARN,
    /** El valor numérico del código genético. */
    VALORNUMERICO,
    /** Las permutaciones del código genético. */
    PERMUTACIONES,
    /** La categoría del código genético .*/
    CATEGORIA,
    /**Indicador si tiene proteina el código genético .*/
    PROTEINA;

    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
        switch(this){
          case ADN: return "Adn";
          case ARN: return "Arn";
          case VALORNUMERICO: return "Valor numerico";
          case PERMUTACIONES: return "Permutaciones";
          case CATEGORIA: return "Categoria";
          case PROTEINA: return "Proteina";
          default: throw new IllegalArgumentException(); 
        }
    }
}
