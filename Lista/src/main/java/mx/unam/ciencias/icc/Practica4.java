package mx.unam.ciencias.icc;

import java.util.Random;

/**
 * Práctica 4: Herencia.
 */
public class Practica4 {

    /* Recursivamente calcula la máxima longitud en los nombres. */
    private static int nombreMasLargo(Lista.Nodo nodo, int longitud) {
        if (nodo == null)
            return longitud;
        Estudiante e = (Estudiante)nodo.get();
        if (e.getNombre().length() > longitud)
            longitud = e.getNombre().length();
        return nombreMasLargo(nodo.getSiguiente(), longitud);
    }

    /* Recursivamente imprime los estudiantes. */
    private static void imprimeEstudiantes(Lista.Nodo nodo, String formato) {
        if (nodo == null)
            return;
        Estudiante e = (Estudiante)nodo.get();
        System.out.printf(formato, e.getNombre(), e.getCuenta(),
                          e.getPromedio(), e.getEdad());
        imprimeEstudiantes(nodo.getSiguiente(), formato);
    }

    public static void main(String[] args) {
        Lista lista = new Lista();
        lista.agregaFinal(new Estudiante("José Arcadio Buendía",
                                         10355684, 9.58, 48));
        lista.agregaFinal(new Estudiante("Úrsula Iguarán",
                                         2254662, 9.56, 45));
        lista.agregaFinal(new Estudiante("Aureliano Buendía",
                                         10118878, 9.41, 26));
        lista.agregaFinal(new Estudiante("Rebeca Buendía",
                                         6934954, 8.61, 44));
        lista.agregaFinal(new Estudiante("Amaranta Buendía",
                                         2274546, 9.68, 41));
        int m = nombreMasLargo(lista.getCabeza(), 0);
        String formato = "%" + (m+2) + "s\t%09d\t%2.2f\t%d\n";
        imprimeEstudiantes(lista.getCabeza(), formato);
    }
}
