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
import mx.unam.ciencias.icc.CodigoGenetico;
import mx.unam.ciencias.icc.Lista;

/**
 * Clase para el controlador de la tabla para mostrar la base de datos.
 */
public class ControladorTablaCodigoGenetico {

    /* La tabla. */
    @FXML private TableView<CodigoGenetico> tabla;

    /* La columna del nombre. */
    @FXML private TableColumn<CodigoGenetico, String> columnaNombre;
    /* La columna del número del adn . */
    @FXML private TableColumn<CodigoGenetico, String> columnaAdn;
    /* La columna del valor numérico. */
    @FXML private TableColumn<CodigoGenetico, Number> columnaValorNumerico;
    /* La columna de la categoría. */
    @FXML private TableColumn<CodigoGenetico, Number> columnaCategoria;
    /* La columna del número de la traducción. */
    @FXML private TableColumn<CodigoGenetico, String> columnaTraduccion;
    /* La columna del número de cadenas. */
    @FXML private TableColumn<CodigoGenetico, Number> columnaNumeroCadenas;


    /* El modelo de la selección. */
    TableView.TableViewSelectionModel<CodigoGenetico> modeloSeleccion;
    /* La selección. */
    private ObservableList<TablePosition> seleccion;
    /* Lista de escuchas de selección. */
    private Lista<EscuchaSeleccion> escuchas;
    /* Los renglones en la tabla. */
    private ObservableList<CodigoGenetico> renglones;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel();
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE);
        seleccion = modeloSeleccion.getSelectedCells();
        ListChangeListener<TablePosition> lcl = c -> cambioEnSeleccion();
        seleccion.addListener(lcl);
        columnaNombre.setCellValueFactory(c -> c.getValue().nombreProperty());
        columnaAdn.setCellValueFactory(c -> c.getValue().adnProperty());
        columnaValorNumerico.setCellValueFactory(c -> c.getValue().valorNumericoProperty());
        columnaCategoria.setCellValueFactory(c -> c.getValue().categoriaProperty());
        columnaTraduccion.setCellValueFactory(c -> c.getValue().traduccionProperty());
        columnaNumeroCadenas.setCellValueFactory(c -> c.getValue().numeroCadenasProperty());
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
     * @param codigo el renglón a agregar.
     */
    public void agregaRenglon(CodigoGenetico codigo) {
        renglones.add(codigo);
        tabla.sort();
    }

    /**
     * Elimina un renglón de la tabla.
     * @param codigo el renglón a eliminar.
     */
    public void eliminaRenglon(CodigoGenetico codigo) {
        renglones.remove(codigo);
        tabla.sort();
    }

    /**
     * Selecciona renglones de la tabla.
     * @param codigos los renglones a seleccionar.
     */
    public void seleccionaRenglones(Lista<CodigoGenetico> codigos) {
        modeloSeleccion.clearSelection();
        for (CodigoGenetico codigo : codigos)
            modeloSeleccion.select(codigo);
    }

    /**
     * Regresa una lista con los registros seleccionados en la tabla.
     * @return una lista con los registros seleccionados en la tabla.
     */
    public Lista<CodigoGenetico> getSeleccion() {
        Lista<CodigoGenetico> seleccionados = new Lista<CodigoGenetico>();
        for (TablePosition tp : seleccion) {
            int r = tp.getRow();
            seleccionados.agregaFinal(renglones.get(r));
        }
        return seleccionados;
    }

    /**
     * Regresa el CodigoGenetico seleccionado en la tabla.
     * @return el CodigoGenetico seleccionado en la tabla.
     */
    public CodigoGenetico getRenglonSeleccionado() {
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
