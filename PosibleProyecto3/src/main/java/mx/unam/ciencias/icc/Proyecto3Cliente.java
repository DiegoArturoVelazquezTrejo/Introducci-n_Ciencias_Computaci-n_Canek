package mx.unam.ciencias.icc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.fx.ControladorTablaCodigoGenetico;
import mx.unam.ciencias.icc.fx.ControladorInterfazCodigoGenetico;

/**
 * Proyecto3: Interfaces gráficas.
 */
public class Proyecto3Cliente extends Application {

    /* Vista de la interfaz códigos genéticos. */
    private static final String INTERFAZ_CODIGOGENETICO_FXML =
        "fxml/interfaz-codigosGeneticos.fxml";
    /* Vista de la tabla de codigos genéticos. */
    private static final String TABLA_CODIGOGENETICO_FXML =
        "fxml/tabla-codigosGeneticos.fxml";
    /* Ícono de la Facultad de Ciencias. */
    private static final String ICONO_CIENCIAS =
        "icons/ciencias.png";

    /**
     * Inicia la aplicación.
     * @param escenario la ventana principal de la aplicación.
     * @throws Exception si algo sale mal. Literalmente así está definido.
     */
    @Override public void start(Stage escenario) throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        String url = cl.getResource(ICONO_CIENCIAS).toString();
        escenario.getIcons().add(new Image(url));
        escenario.setTitle("Administrador de Códigos Genéticos");

        FXMLLoader cargador;
        cargador = new FXMLLoader(cl.getResource(TABLA_CODIGOGENETICO_FXML));
        GridPane tablaCodigoGeneticos = (GridPane)cargador.load();
        ControladorTablaCodigoGenetico controladorTablaCodigoGenetico =
            cargador.getController();

        cargador = new FXMLLoader(cl.getResource(INTERFAZ_CODIGOGENETICO_FXML));
        BorderPane interfazCodigoGenetico = (BorderPane)cargador.load();
        interfazCodigoGenetico.setCenter(tablaCodigoGeneticos);
        ControladorInterfazCodigoGenetico controladorInterfazCodigoGenetico =
            cargador.getController();
        controladorInterfazCodigoGenetico.setEscenario(escenario);
        controladorInterfazCodigoGenetico.setControladorTablaCodigoGenetico(
            controladorTablaCodigoGenetico);

        Scene escena = new Scene(interfazCodigoGenetico);
        escenario.setScene(escena);
        escenario.setOnCloseRequest(
            e -> controladorInterfazCodigoGenetico.salir(null));
        escenario.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
