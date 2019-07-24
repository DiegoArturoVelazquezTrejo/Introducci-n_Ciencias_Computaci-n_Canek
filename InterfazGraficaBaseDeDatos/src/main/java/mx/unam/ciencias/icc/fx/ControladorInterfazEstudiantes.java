package mx.unam.ciencias.icc.fx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.Lista;

/**
 * Clase para el controlador de la ventana principal de la aplicación.
 */
public class ControladorInterfazEstudiantes {

    /* Vista de la forma para realizar búsquedas de estudiantes. */
    private static final String BUSQUEDA_ESTUDIANTES_FXML =
        "fxml/forma-busqueda-estudiantes.fxml";
    /* Vista de la forma para agregar/editar estudiantes. */
    private static final String ESTUDIANTE_FXML =
        "fxml/forma-estudiante.fxml";

    /* Opción de menu para guardar. */
    @FXML private MenuItem menuGuardar;
    /* Opción de menu para agregar. */
    @FXML private MenuItem menuAgregar;
    /* Opción de menu para editar. */
    @FXML private MenuItem menuEditar;
    /* Opción de menu para eliminar. */
    @FXML private MenuItem menuEliminar;
    /* Opción de menu para buscar. */
    @FXML private MenuItem menuBuscar;
    /* El botón de agregar. */
    @FXML private Button botonAgregar;
    /* El botón de editar. */
    @FXML private Button botonEditar;
    /* El botón de eliminar. */
    @FXML private Button botonEliminar;
    /* El botón de buscar. */
    @FXML private Button botonBuscar;

    /* La ventana. */
    private Stage escenario;
    /* El controlador de tabla. */
    private ControladorTablaEstudiantes controladorTablaEstudiantes;
    /* La base de datos. */
    private BaseDeDatosEstudiantes bdd;
    /* El archivo. */
    private File archivo;
    /* Si la base de datos actual ha sido modificada. */
    private boolean modificado;
    /* Si la operación de guardar fue exitosa. */
    private boolean guardadoExitoso;
    /* Número de estudiantes seleccionados. */
    private int seleccionados;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        setSeleccionados(0);
        setModificado(false);
        setBaseDeDatos(new BaseDeDatosEstudiantes());
    }

    /* Crea una nueva base de datos. */
    @FXML private void nuevaBaseDeDatos(ActionEvent evento) {
        if (!verificaInformacion("¿Desea guardarla antes de crear una nueva?"))
            return;
        archivo = null;
        setBaseDeDatos(new BaseDeDatosEstudiantes());
        setModificado(false);
    }

    /* Carga una base de datos. */
    @FXML private void cargaBaseDeDatos(ActionEvent evento) {
        if (!verificaInformacion("¿Desea guardarla antes de cargar otra?"))
            return;
        FileChooser fc = new FileChooser();
        fc.setTitle("Cargar Base de Datos");
        fc.getExtensionFilters().addAll(
            new ExtensionFilter("Bases de datos", "*.bd"),
            new ExtensionFilter("Todos los archivos", "*.*"));
        File archivo = fc.showOpenDialog(escenario);
        if (archivo != null)
            cargaBaseDeDatosDeArchivo(archivo);
    }

    /* Guarda la base de datos. */
    @FXML private void guardaBaseDeDatos(ActionEvent evento) {
        if (archivo == null)
            guardaBaseDeDatosComo(evento);
        else
            guardaBaseDeDatosEnArchivo();
    }

    /* Carga la base de datos de un archivo. */
    private void cargaBaseDeDatosDeArchivo(File archivo) {
        BaseDeDatosEstudiantes nbdd = new BaseDeDatosEstudiantes();
        try {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(archivo)));
            nbdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar la base de datos en '%s'.",
                              archivo.getName());
            dialogoError("Error al cargar base de datos", mensaje);
            return;
        }
        setBaseDeDatos(nbdd);
        this.archivo = archivo;
        setModificado(false);
    }

    /* Guarda la base de datos en un archivo. */
    private void guardaBaseDeDatosEnArchivo() {
        try {
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(archivo)));
            bdd.guarda(out);
            out.close();
            setModificado(false);
            guardadoExitoso = true;
        } catch (IOException ioe) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "guardar la base de datos en '%s'.",
                              archivo.getPath());
            dialogoError("Error al guardar base de datos", mensaje);
            archivo = null;
            guardadoExitoso = false;
        }
    }

    /* Guarda la base de datos con un nombre distinto. */
    @FXML private void guardaBaseDeDatosComo(ActionEvent evento) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar Base de Datos como...");
        fc.getExtensionFilters().addAll(
            new ExtensionFilter("Bases de datos", "*.bd"),
            new ExtensionFilter("Todos los archivos", "*.*"));
        archivo = fc.showSaveDialog(escenario);
        if (archivo != null)
            guardaBaseDeDatosEnArchivo();
    }

    /* Si la base de datos ha sido modificada, muestra un diálogo preguntando al
     * usuario si quiere guardarla. */
    private boolean verificaInformacion(String pregunta) {
        guardadoExitoso = true;
        if (modificado) {
            if (dialogoDeConfirmacion("Base de datos modificada",
                                      "La base de datos ha sido modificada.",
                                      pregunta,
                                      "Guardar cambios", "Ignorar cambios"))
                guardaBaseDeDatos(null);
        }
        return guardadoExitoso;
    }

    /* Actualiza la interfaz dependiendo de si la base de datos ha sido o no
     * modificada. */
    private void setModificado(boolean modificado) {
        this.modificado = modificado;
        menuGuardar.setDisable(!modificado);
    }

    /**
     * Termina el programa.
     * @param evento el evento que generó la acción.
     */
    @FXML public void salir(ActionEvent evento) {
        if (!verificaInformacion("¿Desea guardarla antes de salir?"))
            return;
        Platform.exit();
    }

    /* Agrega un nuevo estudiante. */
    @FXML private void agregaEstudiante(ActionEvent evento) {
        ControladorFormaEstudiante controlador =
            construyeDialogoEstudiante("Agregar estudiante", null);
        if (controlador == null)
            return;
        controlador.setVerbo("Agregar");
        controlador.getEscenario().showAndWait();
        controladorTablaEstudiantes.enfocaTabla();
        if (!controlador.isAceptado())
            return;
        bdd.agregaRegistro(controlador.getEstudiante());
        setModificado(true);
    }

    /* Edita un estudiante. */
    @FXML private void editaEstudiante(ActionEvent evento) {
        Estudiante estudiante =
            controladorTablaEstudiantes.getRenglonSeleccionado();
        ControladorFormaEstudiante controlador =
            construyeDialogoEstudiante("Editar estudiante", estudiante);
        if (controlador == null)
            return;
        controlador.setVerbo("Actualizar");
        controlador.getEscenario().showAndWait();
        controladorTablaEstudiantes.enfocaTabla();
        if (!controlador.isAceptado())
            return;
        bdd.modificaRegistro(estudiante, controlador.getEstudiante());
        setModificado(true);
    }

    /* Elimina uno o varios estudiantes. */
    @FXML private void eliminaEstudiantes(ActionEvent evento) {
        String sujeto = (seleccionados == 1) ? "estudiante" : "estudiantes";
        String titulo = "Eliminar " + sujeto;
        String mensaje = (seleccionados == 1) ?
            "Esto eliminará al estudiante seleccionado" :
            "Esto eliminará a los estudiantes seleccionados";
        if (!dialogoDeConfirmacion(titulo, mensaje, "¿Está seguro?",
                                   "Eliminar " + sujeto,
                                   "Conservar " + sujeto))
            return;
        for (Estudiante estudiante : controladorTablaEstudiantes.getSeleccion())
            bdd.eliminaRegistro(estudiante);
        setModificado(true);
    }

    /* Busca estudiantes. */
    @FXML private void buscaEstudiantes(ActionEvent evento) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador =
                new FXMLLoader(cl.getResource(BUSQUEDA_ESTUDIANTES_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.setTitle("Buscar estudiantes");
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaBusquedaEstudiantes controlador;
            controlador = cargador.getController();
            controlador.setEscenario(escenario);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            escenario.showAndWait();
            controladorTablaEstudiantes.enfocaTabla();
            if (!controlador.isAceptado())
                return;

            Lista<Estudiante> resultados =
                bdd.buscaRegistros(controlador.getCampo(),
                                   controlador.getValor());

            controladorTablaEstudiantes.seleccionaRenglones(resultados);
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de " +
                              "cargar el diálogo '%s'.",
                              BUSQUEDA_ESTUDIANTES_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
        }
    }

    /* Muestra un diálogo con información del programa. */
    @FXML private void acercaDe(ActionEvent evento) {
        Alert dialogo = new Alert(AlertType.INFORMATION);
        dialogo.initOwner(escenario);
        dialogo.initModality(Modality.WINDOW_MODAL);
        dialogo.setTitle("Acerca de Administrador de Estudiantes.");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Aplicación para administrar estudiantes.\n"  +
                               "Copyright © 2019 Facultad de Ciencias, UNAM.");
        dialogo.showAndWait();
        controladorTablaEstudiantes.enfocaTabla();
    }

    /**
     * Define el controlador de tabla.
     * @param controladorTablaEstudiantes el controlador de tabla.
     */
    public void setControladorTablaEstudiantes(ControladorTablaEstudiantes
                                               controladorTablaEstudiantes) {
        this.controladorTablaEstudiantes = controladorTablaEstudiantes;
        controladorTablaEstudiantes.agregaEscuchaSeleccion(
            n -> setSeleccionados(n));
    }

    /**
     * Define el escenario.
     * @param escenario el escenario.
     */
    public void setEscenario(Stage escenario) {
        this.escenario = escenario;
    }

    /* Actualiza la interfaz con una nueva base de datos. */
    private void setBaseDeDatos(BaseDeDatosEstudiantes bdd) {
        if (this.bdd != null)
            this.bdd.limpia();
        this.bdd = bdd;
        for (Estudiante e : bdd.getRegistros())
            controladorTablaEstudiantes.agregaRenglon(e);
        bdd.agregaEscucha((e, r1, r2) -> manejaEventoBaseDeDatos(e, r1, r2));
    }

    /* Maneja un evento de cambio en la base de datos. */
    private void manejaEventoBaseDeDatos(EventoBaseDeDatos evento,
                                         Estudiante estudiante1,
                                         Estudiante estudiante2) {
        switch (evento) {
        case BASE_LIMPIADA:
            controladorTablaEstudiantes.limpiaTabla();
            break;
        case REGISTRO_AGREGADO:
            controladorTablaEstudiantes.agregaRenglon(estudiante1);
            break;
        case REGISTRO_ELIMINADO:
            controladorTablaEstudiantes.eliminaRenglon(estudiante1);
            break;
        case REGISTRO_MODIFICADO:
            /* Los escuchas de Estudiante se hacen cargo. */
            break;
        }
    }

    /* Actualiza la interfaz dependiendo del número de renglones
     * seleccionados. */
    private void setSeleccionados(int seleccionados) {
        this.seleccionados = seleccionados;
        menuEliminar.setDisable(seleccionados == 0);
        menuEditar.setDisable(seleccionados != 1);
        botonEliminar.setDisable(seleccionados == 0);
        botonEditar.setDisable(seleccionados != 1);
    }

    /* Crea un diálogo con una pregunta que hay que confirmar. */
    private boolean dialogoDeConfirmacion(String titulo,
                                          String mensaje, String pregunta,
                                          String aceptar, String cancelar) {
        Alert dialogo = new Alert(AlertType.CONFIRMATION);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(mensaje);
        dialogo.setContentText(pregunta);

        ButtonType si = new ButtonType(aceptar);
        ButtonType no = new ButtonType(cancelar, ButtonData.CANCEL_CLOSE);
        dialogo.getButtonTypes().setAll(si, no);

        Optional<ButtonType> resultado = dialogo.showAndWait();
        controladorTablaEstudiantes.enfocaTabla();
        return resultado.get() == si;
    }

    /* Construye un diálogo para crear o editar un estudiante. */
    private ControladorFormaEstudiante
    construyeDialogoEstudiante(String titulo,
                               Estudiante estudiante) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            FXMLLoader cargador =
                new FXMLLoader(cl.getResource(ESTUDIANTE_FXML));
            AnchorPane pagina = (AnchorPane)cargador.load();

            Stage escenario = new Stage();
            escenario.setTitle(titulo);
            escenario.initOwner(this.escenario);
            escenario.initModality(Modality.WINDOW_MODAL);
            Scene escena = new Scene(pagina);
            escenario.setScene(escena);

            ControladorFormaEstudiante controlador = cargador.getController();
            controlador.setEscenario(escenario);
            controlador.setEstudiante(estudiante);

            escenario.setOnShown(w -> controlador.defineFoco());
            escenario.setResizable(false);
            return controlador;
        } catch (IOException | IllegalStateException e) {
            String mensaje =
                String.format("Ocurrió un error al tratar de cargar " +
                              "el diálogo '%s'.", ESTUDIANTE_FXML);
            dialogoError("Error al cargar interfaz", mensaje);
            return null;
        }
    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        Alert dialogo = new Alert(AlertType.ERROR);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null);
        dialogo.setContentText(mensaje);
        dialogo.showAndWait();
        controladorTablaEstudiantes.enfocaTabla();
    }
}
