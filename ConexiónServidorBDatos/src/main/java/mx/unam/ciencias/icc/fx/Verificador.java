package mx.unam.ciencias.icc.fx;

/**
 * Interfaz funcional para objetos que pueden verificar texto.
 */
@FunctionalInterface
public interface Verificador {

    /**
     * Verifica la cadena de texto.
     * @param texto la cadena de texto.
     * @return <code>true</code> si la cadena de texto es válida,
     *         <code>false</code> en otro caso.
     */
    public boolean verifica(String texto);
}
