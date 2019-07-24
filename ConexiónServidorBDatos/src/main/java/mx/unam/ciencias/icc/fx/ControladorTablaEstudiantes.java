package mx.unam.ciencias.icc.fx;

import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.Lista;

/**
 * Clase para el controlador de la tabla para mostrar la base de datos.
 */
public class ControladorTablaEstudiantes {

    /* La tabla. */
    @FXML private TableView<Estudiante> tabla;

    /* La columna del nombre. */
    @FXML private TableColumn<Estudiante, String> columnaNombre;
    /* La columna del número de cuenta. */
    @FXML private TableColumn<Estudiante, Number> columnaCuenta;
    /* La columna del promedio. */
    @FXML private TableColumn<Estudiante, Number> columnaPromedio;
    /* La columna de la edad. */
    @FXML private TableColumn<Estudiante, Number> columnaEdad;

    /* El modelo de la selección. */
    TableView.TableViewSelectionModel<Estudiante> modeloSeleccion;
    /* La selección. */
    private ObservableList<TablePosition> seleccion;
    /* Lista de escuchas de selección. */
    private Lista<EscuchaSeleccion> escuchas;
    /* Los renglones en la tabla. */
    private ObservableList<Estudiante> renglones;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE);
        seleccion = modeloSeleccion.getSelectedCells();
        ListChangeListener<TablePosition> lcl = c -> cambioEnSeleccion();
        seleccion.addListener(lcl);
        columnaNombre.setCellValueFactory(c -> c.getValue().nombreProperty());
        columnaCuenta.setCellValueFactory(c -> c.getValue().cuentaProperty());
        columnaPromedio.setCellValueFactory(
            c -> c.getValue().promedioProperty());
        columnaEdad.setCellValueFactory(c -> c.getValue().edadProperty());
        escuchas = new Lista<EscuchaSeleccion>();
    }

    /* Notifica a los escuchas que hubo un cambio en la selección. */
    private void cambioEnSeleccion() {
        for (EscuchaSeleccion escucha : escuchas)
            escucha.renglonesSeleccionados(seleccion.size());
    }

    /**
     * Limpia la tabla.
     */
    public void limpiaTabla() {
        renglones.clear();
    }

    /**
     * Agrega un renglón a la tabla.
     * @param estudiante el renglón a agregar.
     */
    public void agregaRenglon(Estudiante estudiante) {
        renglones.add(estudiante);
        tabla.sort();
    }

    /**
     * Elimina un renglón de la tabla.
     * @param estudiante el renglón a eliminar.
     */
    public void eliminaRenglon(Estudiante estudiante) {
        renglones.remove(estudiante);
        tabla.sort();
    }

    /**
     * Selecciona renglones de la tabla.
     * @param estudiantes los renglones a seleccionar.
     */
    public void seleccionaRenglones(Lista<Estudiante> estudiantes) {
        modeloSeleccion.clearSelection();
        for (Estudiante estudiante : estudiantes)
            modeloSeleccion.select(estudiante);
    }

    /**
     * Regresa una lista con los registros seleccionados en la tabla.
     * @return una lista con los registros seleccionados en la tabla.
     */
    public Lista<Estudiante> getSeleccion() {
        Lista<Estudiante> seleccionados = new Lista<Estudiante>();
        for (TablePosition tp : seleccion) {
            int r = tp.getRow();
            seleccionados.agregaFinal(renglones.get(r));
        }
        return seleccionados;
    }

    /**
     * Regresa el estudiante seleccionado en la tabla.
     * @return el estudiante seleccionado en la tabla.
     */
    public Estudiante getRenglonSeleccionado() {
        int r = seleccion.get(0).getRow();
        return renglones.get(r);
    }

    /**
     * Agrega un escucha de selección.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscuchaSeleccion(EscuchaSeleccion escucha) {
        escuchas.agregaFinal(escucha);
    }

    /**
     * Fuerza un reordenamiento de la tabla.
     */
    public void reordena() {
        tabla.sort();
    }

    /**
     * Enfoca la tabla.
     */
    public void enfocaTabla() {
        tabla.requestFocus();
    }
}
