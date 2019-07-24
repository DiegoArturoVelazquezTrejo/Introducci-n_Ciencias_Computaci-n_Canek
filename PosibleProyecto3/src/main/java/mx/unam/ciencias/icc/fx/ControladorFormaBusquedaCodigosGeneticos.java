package mx.unam.ciencias.icc.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoCodigoGenetico;

/**
 * Clase para el controlador del contenido del diálogo para buscar códigos genéticos.
 */
public class ControladorFormaBusquedaCodigosGeneticos extends ControladorForma {

    /* El combo del campo. */
    @FXML private ComboBox<CampoCodigoGenetico> opcionesCampo;
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
        case CATEGORIA:
            m = "Buscar por categoría necesita un número entre " +
                "1 y 99";
            break;
        case VALORNUMERICO:
            m = "Buscar por categoría necesita un número entre " +
                "1000000 y 99999999";
            break;
        case ADN:
            m = "Buscar por edad necesita al menos un carácter";
            break;

        case TRADUCCION:
            m = "Buscar por traducción necesita al menos un carácter";
            break;
        case NUMEROCADENAS:
            m = "Buscar por número de cadenas necesita un número entre 1 y 100";
            break;
        }
        return new Tooltip(m);
    }

    /* Verifica el valor. */
    private boolean verificaValor(String s) {
        switch (opcionesCampo.getValue()) {
        case NOMBRE:   return verificaNombre(s);
        case ADN:   return verificaAdn(s);
        case CATEGORIA: return verificaCategoria(s);
        case TRADUCCION:     return verificaTraduccion(s);
        case VALORNUMERICO: return verificaValorNumerico(s);
        case NUMEROCADENAS: return verificaNumeroCadenas(s);
        default:       return false;
        }
    }

    /* Verifica que el nombre a buscar sea válido. */
    private boolean verificaNombre(String n) {
        return n != null && !n.isEmpty();
    }

    /* Verifica que el adn a buscar sea válido. */
    private boolean verificaAdn(String c) {
        return c != null && !c.isEmpty();
    }

    /* Verifica que la traducción a buscar sea válido. */
    private boolean verificaTraduccion(String n) {
        return n != null && !n.isEmpty();
    }

    /* Verifica que el valor numérico a buscar sea válido. */
    private boolean verificaValorNumerico(String p) {
        if (p == null || p.isEmpty())
            return false;
        double valorNumerico = -1.0;
        try {
            valorNumerico = Double.parseDouble(p);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return valorNumerico >= 0.0;
    }

    /* Verifica que la categoría a buscar sea válida. */
    private boolean verificaCategoria(String e) {
        if (e == null || e.isEmpty())
            return false;
        int categoria = -1;
        try {
            categoria = Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return categoria >= 1 && categoria <= 100;
    }

    /* Verifica que el número de cadenas a buscar sea válida. */
    private boolean verificaNumeroCadenas(String e) {
        if (e == null || e.isEmpty())
            return false;
        int cadenas = -1;
        try {
            cadenas = Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return cadenas >= 1 && cadenas <= 100;
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoCodigoGenetico getCampo() {
        return opcionesCampo.getValue();
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {
      switch (opcionesCampo.getValue()) {
        case NOMBRE:            return entradaValor.getText();
        case ADN:               return entradaValor.getText();
        case CATEGORIA:         return Integer.parseInt(entradaValor.getText());
        case VALORNUMERICO:     return Double.parseDouble(entradaValor.getText());
        case NUMEROCADENAS:     return Integer.parseInt(entradaValor.getText());
        default:                return entradaValor.getText(); // No debería ocurrir.
      }

    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaValor.requestFocus();
    }
}
