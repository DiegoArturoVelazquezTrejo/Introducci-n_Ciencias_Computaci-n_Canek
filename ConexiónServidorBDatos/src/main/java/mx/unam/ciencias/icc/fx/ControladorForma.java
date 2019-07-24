package mx.unam.ciencias.icc.fx;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Clase abstracta para controladores del contenido de diálogo con formas que se
 * aceptan o rechazan.
 */
public abstract class ControladorForma {

    /** El botón para aceptar. */
    @FXML protected Button botonAceptar;

    /** La ventana del diálogo. */
    protected Stage escenario;
    /** Si el usuario aceptó la forma. */
    protected boolean aceptado;

    /**
     * Manejador para cuando se activa el botón cancelar.
     * @param evento el evento que generó la acción.
     */
    @FXML protected void cancelar(ActionEvent evento) {
        aceptado = false;
        escenario.close();
    }

    /**
     * Define el escenario del diálogo.
     * @param escenario el nuevo escenario del diálogo.
     */
    public void setEscenario(Stage escenario) {
        this.escenario = escenario;
        Scene escena = escenario.getScene();
        KeyCodeCombination combinacion;
        combinacion = new KeyCodeCombination(KeyCode.ENTER,
                                             KeyCombination.CONTROL_DOWN);
        ObservableMap<KeyCombination, Runnable> accs = escena.getAccelerators();
        accs.put(combinacion, () -> botonAceptar.fire());
    }

    /**
     * Regresa el escenario del diálogo.
     * @return el escenario del diálogo.
     */
    public Stage getEscenario() {
        return escenario;
    }

    /**
     * Nos dice si el usuario activó el botón de aceptar.
     * @return <code>true</code> si el usuario activó el botón de aceptar,
     *         <code>false</code> en otro caso.
     */
    public boolean isAceptado() {
        return aceptado;
    }

    /**
     * Define el foco incial del diálogo.
     */
    public abstract void defineFoco();
}
