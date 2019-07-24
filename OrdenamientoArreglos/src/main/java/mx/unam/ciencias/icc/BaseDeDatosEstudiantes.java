package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de estudiantes.
 */
public class BaseDeDatosEstudiantes extends BaseDeDatos<Estudiante, CampoEstudiante> {
    /**
     * Crea un estudiante en blanco.
     * @return un estudiante en blanco.
     */
     @Override public Estudiante creaRegistro() {
         return  new Estudiante();
     }
}
