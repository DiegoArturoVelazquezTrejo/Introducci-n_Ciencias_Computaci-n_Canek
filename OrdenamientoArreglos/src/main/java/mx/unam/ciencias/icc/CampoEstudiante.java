package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Estudiante}.
 */
public enum CampoEstudiante {

    /** El nombre del estudiante. */
    NOMBRE,
    /** El número de cuenta del estudiante. */
    CUENTA,
    /** El promedio del estudiante. */
    PROMEDIO,
    /** La edad del estudiante. */
    EDAD;

    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
     @Override public String toString() {
         switch(this){
           case NOMBRE: return "Nombre";
           case CUENTA: return "# Cuenta";
           case PROMEDIO : return "Promedio";
           case EDAD: return "Edad";
           default: throw new IllegalArgumentException();
         }
     }
}
