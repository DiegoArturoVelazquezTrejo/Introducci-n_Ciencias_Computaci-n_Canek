package mx.unam.ciencias.icc;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void selectionSort(T[] arreglo, Comparator<T> comparador) {
          int m;
          for(int i = 0; i < arreglo.length; i++){
            m = i;
            for(int j = i+1; j < arreglo.length; j++){
              if(comparador.compare(arreglo[j],arreglo[m]) < 0){
                m = j;
              }
            }
            intercambia(arreglo, i, m);
          }
    }
    private static <T> void intercambia(T[] arreglo, int indice1, int indice2){
        T elemento1 = arreglo[indice1];
        T elemento2 = arreglo[indice2];
        arreglo[indice1] = elemento2;
        arreglo[indice2] = elemento1;
    }
    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void quickSort(T[] arreglo, Comparator<T> comparador) {
       quickSort(arreglo, comparador, 0, arreglo.length-1);
    }

    public static <T> void quickSort(T[] arreglo, Comparator<T> comparador, int a, int b){
        if(b <= a) return;
        int i = a + 1;
        int j = b;
        while(i < j){
          if(comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[a], arreglo[j]) >= 0){
            intercambia(arreglo, i, j);
            i = i + 1;
            j = j - 1;
          }else if(comparador.compare(arreglo[a], arreglo[i]) >= 0)
            i = i + 1;
          else
            j = j - 1;
        }
        if(comparador.compare(arreglo[i], arreglo[a]) > 0)
          i = i - 1;
        intercambia(arreglo, a, i);
        quickSort(arreglo, comparador, a, i - 1);
        quickSort(arreglo, comparador, i+1, b);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        int a = 0;
        int b = arreglo.length - 1;
        while(a <= b){
          int mitad = (a + b) / 2;
          if(comparador.compare(arreglo[mitad], elemento) == 0) return mitad;
          else if(comparador.compare(arreglo[mitad], elemento) > 0){
            if(comparador.compare(arreglo[a], elemento) == 0)return a;
            b = mitad -1;
            a = a + 1;
          }
          else{
            if(comparador.compare(arreglo[b], elemento) == 0) return b;
            b = b -1;
            a = mitad + 1;
          }
        }
        return -1;
    }
}
