package mx.unam.ciencias.icc;

/**
 * Práctica 2: Estructuras de control y listas.
 */
public class Practica2 {

    public static void main(String[] args) {
        ListaEstudiante lista = new ListaEstudiante();
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

        int m = 0;
        ListaEstudiante.Nodo nodo = lista.getCabeza();
        while (nodo != null) {
            Estudiante e = nodo.get();
            if (e.getNombre().length() > m)
                m = e.getNombre().length();
            nodo = nodo.getSiguiente();
        }

        m += 2;
        String formato = "%" + m + "s\t%09d\t%2.2f\t%d\n";

        nodo = lista.getCabeza();
        while (nodo != null) {
            Estudiante e = nodo.get();
            System.out.printf(formato, e.getNombre(), e.getCuenta(),
                              e.getPromedio(), e.getEdad());
            nodo = nodo.getSiguiente();
        }
    }
}
