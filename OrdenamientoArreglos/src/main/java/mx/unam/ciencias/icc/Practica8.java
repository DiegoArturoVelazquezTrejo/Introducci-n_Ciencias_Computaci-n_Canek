package mx.unam.ciencias.icc;

import java.util.Random;
import java.text.NumberFormat;

/**
 * Práctica 8: Ordenamientos y búsquedas.
 */
public class Practica8 {

    public static void main(String[] args) {
        int N = 100000; /* Cien mil */
        Random random = new Random();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        long tiempoInicial, tiempoTotal;

        int[] arreglo = new int[N];
        for (int i = 0; i < N; i++)
            arreglo[i] = random.nextInt();

        Integer[] is = new Integer[N];
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            is[i] = arreglo[i];
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en llenar un arreglo con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        Arreglos.selectionSort(is);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando SelectionSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Integer[] qs = new Integer[N];
        for (int i = 0; i < N; i++)
            qs[i] = arreglo[i];

        tiempoInicial = System.nanoTime();
        Arreglos.quickSort(qs);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar un arreglo con %s elementos " +
                          "usando QuickSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        int b = qs[random.nextInt(N)];

        tiempoInicial = System.nanoTime();
        int idx = Arreglos.busquedaBinaria(qs, b);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en encontrar un elemento en un arreglo " +
                          "con %s elementos usando búsqueda binaria.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        Lista<Integer> ms = new Lista<Integer>();
        tiempoInicial = System.nanoTime();
        for (int i = 0; i < N; i++)
            ms.agregaFinal(arreglo[i]);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en crear una lista con %s elementos.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        tiempoInicial = System.nanoTime();
        ms = Lista.mergeSort(ms);
        tiempoTotal = System.nanoTime() - tiempoInicial;
        System.out.printf("%2.9f segundos en ordenar una lista con %s elementos " +
                          "usando MergeSort.\n",
                          (tiempoTotal/1000000000.0), nf.format(N));

        System.out.println();

        Lista<Estudiante> estudiantes = new Lista<Estudiante>();
        estudiantes.agregaFinal(new Estudiante("José Arcadio Buendía",
                                               10355684, 9.58, 48));
        estudiantes.agregaFinal(new Estudiante("Úrsula Iguarán",
                                               2254662, 9.56, 45));
        estudiantes.agregaFinal(new Estudiante("Aureliano Buendía",
                                               10118878, 9.41, 26));

        estudiantes =
            estudiantes.mergeSort((e1, e2) -> e1.getNombre().compareTo(e2.getNombre()));
        System.out.println("Ordenamiento por nombre:");
        for (Estudiante estudiante : estudiantes)
            System.out.println(estudiante);

        System.out.println();

        estudiantes =
            estudiantes.mergeSort((e1, e2) -> e1.getCuenta() - e2.getCuenta());
        System.out.println("Ordenamiento por cuenta:");
        for (Estudiante estudiante : estudiantes)
            System.out.println(estudiante);

        System.out.println();

        estudiantes =
            estudiantes.mergeSort((e1, e2) -> {
                    double p1 = e1.getPromedio();
                    double p2 = e2.getPromedio();
                    if (p1 < p2)
                        return -1;
                    if (p2 < p1)
                        return 1;
                    return 0;
                });
        System.out.println("Ordenamiento por promedio:");
        for (Estudiante estudiante : estudiantes)
            System.out.println(estudiante);

        System.out.println();

        estudiantes =
            estudiantes.mergeSort((e1, e2) -> e1.getEdad() - e2.getEdad());
        System.out.println("Ordenamiento por edad:");
        for (Estudiante estudiante : estudiantes)
            System.out.println(estudiante);
    }
}
