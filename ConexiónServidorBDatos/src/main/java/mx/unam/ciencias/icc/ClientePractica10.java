package mx.unam.ciencias.icc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.fx.ControladorInterfazEstudiantes;
import mx.unam.ciencias.icc.fx.ControladorTablaEstudiantes;

/**
 * ClientePractica10: Parte del cliente para práctica 10: Hilos de ejecución y
 * enchufes.
 */
public class ClientePractica10 extends Application {

    /* Vista de la interfaz estudiantes. */
    private static final String INTERFAZ_ESTUDIANTES_FXML =
        "fxml/interfaz-estudiantes.fxml";
    /* Vista de la tabla de estudiantes. */
    private static final String TABLA_ESTUDIANTES_FXML =
        "fxml/tabla-estudiantes.fxml";
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
        escenario.setTitle("Administrador de Estudiantes");

        FXMLLoader cargador =
            new FXMLLoader(cl.getResource(TABLA_ESTUDIANTES_FXML));
        GridPane tablaEstudiante = (GridPane)cargador.load();
        ControladorTablaEstudiantes controladorTablaEstudiantes =
            cargador.getController();

        cargador = new FXMLLoader(cl.getResource(INTERFAZ_ESTUDIANTES_FXML));
        BorderPane interfazEstudiantes = (BorderPane)cargador.load();
        interfazEstudiantes.setCenter(tablaEstudiante);
        ControladorInterfazEstudiantes controladorInterfazEstudiantes =
            cargador.getController();
        controladorInterfazEstudiantes.setEscenario(escenario);
        controladorInterfazEstudiantes.setControladorTablaEstudiantes(
            controladorTablaEstudiantes);

        Scene escena = new Scene(interfazEstudiantes);
        escenario.setScene(escena);
        escenario.setOnCloseRequest(e -> controladorInterfazEstudiantes.salir(null));
        escenario.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
