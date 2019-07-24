package mx.unam.ciencias.icc.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoEstudiante;

/**
 * Clase para el controlador del contenido del diálogo para buscar estudiantes.
 */
public class ControladorFormaBusquedaEstudiantes extends ControladorForma {

    /* El combo del campo. */
    @FXML private ComboBox<CampoEstudiante> opcionesCampo;
    /* El campo de texto para el valor. */
    @FXML private EntradaVerificable entradaValor;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaValor.setVerificador(s -> verificaValor(s));
        entradaValor.textProperty().addListener(
            (o, v, n) -> revisaValor(null));
    }

    /* Revisa el valor después de un cambio. */
    @FXML private void revisaValor(ActionEvent evento) {
        Tooltip.install(entradaValor, getTooltip());
        String s = entradaValor.getText();
        botonAceptar.setDisable(!entradaValor.esValida());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        aceptado = true;
        escenario.close();
    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {
        String m = "";
        switch (opcionesCampo.getValue()) {
        case NOMBRE:
            m = "Buscar por nombre necesita al menos un carácter";
            break;
        case CUENTA:
            m = "Buscar por cuenta necesita un número entre " +
                "1000000 y 99999999";
            break;
        case PROMEDIO:
            m = "Buscar por promedio necesita un número entre 0.0 y 10.0";
            break;
        case EDAD:
            m = "Buscar por edad necesita un número entre 13 y 9";
            break;
        }
        return new Tooltip(m);
    }

    /* Verifica el valor. */
    private boolean verificaValor(String s) {
        switch (opcionesCampo.getValue()) {
        case NOMBRE:   return verificaNombre(s);
        case CUENTA:   return verificaCuenta(s);
        case PROMEDIO: return verificaPromedio(s);
        case EDAD:     return verificaEdad(s);
        default:       return false;
        }
    }

    /* Verifica que el nombre a buscar sea válido. */
    private boolean verificaNombre(String n) {
        return n != null && !n.isEmpty();
    }

    /* Verifica que el número de cuenta a buscar sea válido. */
    private boolean verificaCuenta(String c) {
        if (c == null || c.isEmpty())
            return false;
        int cuenta = -1;
        try {
            cuenta = Integer.parseInt(c);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return cuenta >= 1000000 && cuenta < 99999999;
    }

    /* Verifica que el promedio a buscar sea válido. */
    private boolean verificaPromedio(String p) {
        if (p == null || p.isEmpty())
            return false;
        double promedio = -1.0;
        try {
            promedio = Double.parseDouble(p);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return promedio >= 0.0 && promedio <= 10.0;
    }

    /* Verifica que la edad a buscar sea válida. */
    private boolean verificaEdad(String e) {
        if (e == null || e.isEmpty())
            return false;
        int edad = -1;
        try {
            edad = Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return edad >= 13 && edad <= 99;
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoEstudiante getCampo() {
        return opcionesCampo.getValue();
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {
        switch (opcionesCampo.getValue()) {
        case NOMBRE:   return entradaValor.getText();
        case CUENTA:   return Integer.parseInt(entradaValor.getText());
        case PROMEDIO: return Double.parseDouble(entradaValor.getText());
        case EDAD:     return Integer.parseInt(entradaValor.getText());
        default:       return entradaValor.getText(); // No debería ocurrir.
        }
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaValor.requestFocus();
    }
}
