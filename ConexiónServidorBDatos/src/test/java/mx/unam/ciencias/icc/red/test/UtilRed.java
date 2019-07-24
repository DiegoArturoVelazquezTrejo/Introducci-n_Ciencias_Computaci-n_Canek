package mx.unam.ciencias.icc.red.test;

import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.test.TestEstudiante;

/**
 * Clase con métodos estáticos utilitarios para las pruebas unitarias de red.
 */
public class UtilRed {

    /* Evitamos instanciación. */
    private UtilRed() {}

    /* Número de cuenta inicial. */
    public static final int CUENTA_INICIAL = 1000000;

    /* Contador de números de cuenta. */
    private static int contador;

    /**
     * Espera el número de milisegundos de forma segura.
     * @param milisegundos el número de milisegundos a esperar.
     */
    public static void espera(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException ie) {}
    }

    /**
     * Llena una base de datos de estudiantes.
     * @param bdd la base de datos a llenar.
     * @param total el total de estudiantes.
     */
    public static void llenaBaseDeDatos(BaseDeDatosEstudiantes bdd, int total) {
        for (int i = 0; i < total; i++) {
            int c = CUENTA_INICIAL + i;
            bdd.agregaRegistro(TestEstudiante.estudianteAleatorio(c));
        }
    }

    /**
     * Crea un estudiante aleatorio.
     * @param total el total de estudiantes.
     * @return un estudiante aleatorio con un número de cuenta único.
     */
    public static Estudiante estudianteAleatorio(int total) {
        int nc = CUENTA_INICIAL + total*2 + contador++;
        return TestEstudiante.estudianteAleatorio(nc);
    }

}
