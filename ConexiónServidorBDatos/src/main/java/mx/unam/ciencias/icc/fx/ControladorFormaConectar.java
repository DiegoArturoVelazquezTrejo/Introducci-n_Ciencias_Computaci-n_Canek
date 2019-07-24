package mx.unam.ciencias.icc.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Clase para el controlador del diálogo para conectar al servidor.
 */
public class ControladorFormaConectar extends ControladorForma {

    /* El campo verificable para el servidor. */
    @FXML private EntradaVerificable entradaServidor;
    /* El campo verificable para el puerto. */
    @FXML private EntradaVerificable entradaPuerto;

    /* El servidor. */
    private String servidor;
    /* El puerto. */
    private int puerto;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaServidor.setVerificador(s -> verificaServidor(s));
        entradaPuerto.setVerificador(p -> verificaPuerto(p));

        entradaServidor.textProperty().addListener(
            (o, v, n) -> conexionValida());
        entradaPuerto.textProperty().addListener(
            (o, v, n) -> conexionValida());
    }

    /* Manejador para cuando se activa el botón conectar. */
    @FXML private void conectar(ActionEvent evento) {
        aceptado = true;
        escenario.close();
    }

    /* Determina si los campos son válidos. */
    private void conexionValida() {
        boolean s = entradaServidor.esValida();
        boolean p = entradaPuerto.esValida();
        botonAceptar.setDisable(!s || !p);
    }

    /* Verifica que el servidor sea válido. */
    private boolean verificaServidor(String s) {
        if (s == null || s.isEmpty())
            return false;
        servidor = s;
        return true;
    }

    /* Verifica que el puerto sea válido. */
    private boolean verificaPuerto(String p) {
        if (p == null || p.isEmpty())
            return false;
        try {
            puerto = Integer.parseInt(p);
        } catch (NumberFormatException nfe) {
            return false;
        }
        if (puerto < 1025 || puerto > 65535)
            return false;
        return true;
    }

    /**
     * Regresa el servidor del diálogo.
     * @return el servidor del diálogo.
     */
    public String getServidor() {
        return servidor;
    }

    /**
     * Regresa el puerto del diálogo.
     * @return el puerto del diálogo.
     */
    public int getPuerto() {
        return puerto;
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaServidor.requestFocus();
    }
}
