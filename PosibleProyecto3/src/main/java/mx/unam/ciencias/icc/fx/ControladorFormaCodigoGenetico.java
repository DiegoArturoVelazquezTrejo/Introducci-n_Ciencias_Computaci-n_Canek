package mx.unam.ciencias.icc.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import mx.unam.ciencias.icc.CodigoGenetico;

/**
 * Clase para el controlador del contenido del diálogo para editar y crear
 * códigos genéticos.
 */
public class ControladorFormaCodigoGenetico extends ControladorForma {

    /* La entrada verificable para el nombre. */
    @FXML private EntradaVerificable entradaNombre;
    /* La entrada verificable para el valor numérico. */
    @FXML private EntradaVerificable entradaValorNumerico;
    /* La entrada verificable para la categoría. */
    @FXML private EntradaVerificable entradaCategoria;
    /* La entrada verificable para la traduccion. */
    @FXML private EntradaVerificable entradaTraduccion;
    /* La entrada verificable para el adn. */
    @FXML private EntradaVerificable entradaAdn;

    /* El valor del nombre. */
    private String nombre;
    /* El valor del valor numérico. */
    private double valorNumerico;
    /* El valor de la categoría. */
    private int categoria;
    /* El valor del adn. */
    private String adn;
    /* El valor del número de cadenas. */
    private int numeroCadenas;
    /* El valor de la traducción . */
    private String traduccion;

    /* El código genético creado o editado. */
    private CodigoGenetico codigo;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaNombre.setVerificador(n -> verificaNombre(n));
        entradaTraduccion.setVerificador(n -> verificaTraduccion(n));
        entradaValorNumerico.setVerificador(c -> verificaValorNumerico(c));
        entradaCategoria.setVerificador(p -> verificaCategoria(p));
        entradaAdn.setVerificador(n -> verificaAdn(n));


        entradaNombre.textProperty().addListener(
            (o, v, n) -> verificaCodigoGenetico());
        entradaTraduccion.textProperty().addListener(
            (o, v, n) -> verificaCodigoGenetico());
        entradaValorNumerico.textProperty().addListener(
            (o, v, n) -> verificaCodigoGenetico());
        entradaCategoria.textProperty().addListener(
            (o, v, n) -> verificaCodigoGenetico());
        entradaAdn.textProperty().addListener(
            (o, v, n) -> verificaCodigoGenetico());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        actualizaCodigoGenetico();
        aceptado = true;
        escenario.close();
    }

    /**
     * Define el código genético del diálogo.
     * @param codigo el nuevo código del diálogo.
     */
    public void setCodigoGenetico(CodigoGenetico codigo) {
        this.codigo = codigo;
        if (codigo == null)
            return;
        this.codigo = new CodigoGenetico(null, null, 0, 0, null);
        this.codigo.actualiza(codigo);
        entradaNombre.setText(codigo.getNombre());
        entradaAdn.setText(codigo.getAdn());
        entradaTraduccion.setText(codigo.getTraduccion());
        String c = String.format("%09d", codigo.getCategoria());
        entradaCategoria.setText(c);
        String p = String.format("%2.2f", codigo.getValorNumerico());
        entradaValorNumerico.setText(p);
    }

    /* Verifica que los cuatro campos sean válidos. */
    private void verificaCodigoGenetico() {
        boolean n = entradaNombre.esValida();
        boolean c = entradaAdn.esValida();
        boolean p = entradaCategoria.esValida();
        boolean e = entradaValorNumerico.esValida();
        boolean m = entradaTraduccion.esValida();
        botonAceptar.setDisable(!n || !c || !p || !e || !m);
    }


    /* Verifica que el nombre sea válido. */
    private boolean verificaNombre(String n) {
        if (n == null || n.isEmpty())
            return false;
        nombre = n;
        return true;
    }
    /* Verifica que la traducción sea válida. */
    private boolean verificaTraduccion(String n) {
        if (n == null || n.isEmpty())
            return false;
        traduccion = n;
        return true;
    }
    /* Verifica que el adn sea válido. */
    private boolean verificaAdn(String n) {
        if (n == null || n.isEmpty())
            return false;
        adn = n;
        return true;
    }
    /* Verifica que la categoría sea válido. */
    private boolean verificaCategoria(String e) {
        if (e == null || e.isEmpty())
            return false;
        try {
            categoria = Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return categoria >= 1 && categoria <= 100;
    }

    /* Verifica que el valor numérico a buscar sea válido. */
    private boolean verificaValorNumerico(String p) {
        if (p == null || p.isEmpty())
            return false;
        try {
            valorNumerico = Double.parseDouble(p);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return valorNumerico >= 0.0;
    }

    /* Actualiza al código genético, o lo crea si no existe. */
    private void actualizaCodigoGenetico() {
        if (codigo != null) {
            codigo.setNombre(nombre);
            codigo.setAdn(adn);
            codigo.setValorNumerico(valorNumerico);
            codigo.setTraduccion(traduccion);
            codigo.setCategoria(categoria);
        } else {
            codigo = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        }
    }

    /**
     * Regresa el código genético del diálogo.
     * @return el codigo del diálogo.
     */
    public CodigoGenetico getCodigoGenetico() {
        return codigo;
    }

    /**
     * Define el verbo del botón de aceptar.
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) {
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaNombre.requestFocus();
    }
}
