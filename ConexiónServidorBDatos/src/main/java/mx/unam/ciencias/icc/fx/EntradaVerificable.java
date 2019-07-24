package mx.unam.ciencias.icc.fx;

import javafx.scene.control.TextField;

/**
 * Clase para entradas verificables.
 */
public class EntradaVerificable extends TextField {

    /* El verificador de la entrada. */
    private Verificador verificador;

    /**
     * Define el estado inicial de una entrada verificable.
     */
    public EntradaVerificable() {
        verificador = e -> false;
    }

    /**
     * Define el verificador de la entrada.
     * @param verificador el nuevo verificador de la entrada.
     */
    public void setVerificador(Verificador verificador) {
        this.verificador = verificador;
    }

    /**
     * Nos dice si la entrada es válida.
     * @return <code>true</code> si la entrada es válida, <code>false</code> en
     *         otro caso.
     */
    public boolean esValida() {
        boolean b = verificador.verifica(getText());
        String s = b ? "" : "-fx-background-color:FFCCCCCC;";
        setStyle(s);
        return b;
    }
}
