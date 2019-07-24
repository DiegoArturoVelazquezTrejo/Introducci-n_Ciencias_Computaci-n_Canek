package mx.unam.ciencias.icc;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para listas genéricas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Iterable<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(!hasNext()) throw new NoSuchElementException();
            else{
              T elem = siguiente.elemento;
              anterior = siguiente;
              siguiente = siguiente.siguiente;
              return elem;
            }
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(!hasPrevious()) throw new NoSuchElementException();
            else{
              T elem = anterior.elemento;
              siguiente = anterior;
              anterior = anterior.anterior;
              return elem;
            }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            siguiente = null;
            anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        return longitud == 0;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
      if(elemento == null) throw new IllegalArgumentException("Elemento null");
      Nodo nodo = new Nodo(elemento);
      if(esVacia()){
          cabeza = nodo;
          rabo = cabeza;
          cabeza.anterior = null;
          rabo.siguiente = null;
        }
        else{
          nodo.anterior = rabo;
          rabo.siguiente = nodo;
          rabo = nodo;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
      if(elemento == null) throw new IllegalArgumentException("Elemento null");
      Nodo nodo = new Nodo(elemento);
      if(esVacia()) cabeza = rabo = nodo;
      else{
        nodo.siguiente = cabeza;
        cabeza.anterior = nodo;
        cabeza = nodo;
      }
      longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento null");
        else if(i <= 0) agregaInicio(elemento);
        else if(longitud <= i) agregaFinal(elemento);
        else{

          Nodo nodo = buscaNodo(get(i));
          Nodo nuevoNodo = new Nodo(elemento);

          nodo.anterior.siguiente = nuevoNodo;
          nuevoNodo.anterior = nodo.anterior;
          nodo.anterior = nuevoNodo;
          nuevoNodo.siguiente = nodo;
          longitud++;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        eliminaNodo(buscaNodo(elemento));
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if(esVacia())  throw new NoSuchElementException("Lista Vacía");
        Nodo eliminado = cabeza;
        if(longitud == 1) limpia();
        else{
          cabeza = cabeza.siguiente;
          cabeza.anterior = null;
          longitud--;
        }
        return eliminado.elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if(esVacia()) throw new NoSuchElementException("Lista Vacía");
        Nodo eliminado = rabo;
        if(longitud == 1) limpia();
        else{
          rabo = rabo.anterior;
          rabo.siguiente = null;
          longitud--;
        }
        return eliminado.elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        return buscaNodo(elemento) != null;

    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
      Lista<T> lista = new Lista<T>();
      Nodo nodo = rabo;
      while(nodo != null){
        lista.agregaFinal(nodo.elemento);
        nodo = nodo.anterior;
      }
      return lista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Nodo nodo = cabeza;
        Lista<T> copiaLista = new Lista<T>();
        if(esVacia()) return copiaLista;
        while(nodo != null){
          copiaLista.agregaFinal(nodo.elemento);
          nodo = nodo.siguiente;
        }
        return copiaLista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        cabeza = rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(longitud != 0) return cabeza.elemento;
        else throw new NoSuchElementException("Lista vacía");
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if(longitud != 0) return rabo.elemento;
        else throw new NoSuchElementException("Lista vacía");
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if(i < 0 || i >= longitud) throw new ExcepcionIndiceInvalido("Índice inválido");
        Nodo n = cabeza;
        while(i-- > 0){
          n = n.siguiente;
        }
        return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo n = cabeza;
        int indice = 0;
        while( n != null){
          if(n.elemento.equals(elemento)) return indice;
          n = n.siguiente;
          indice++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if(esVacia()) return "[]";
        String s = "[";
        for (int i = 0; i < longitud-1; i++)
            s += String.format("%s, ", get(i));
        s += String.format("%s]", get(longitud-1));
        return s;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(lista == null) return false;
        else if( lista.getLongitud() != longitud) return false;
        else if(lista.getLongitud() == 0 && longitud == 0) return true;
        Nodo nodo = cabeza;
        int i = 0;
        while(nodo != null){
          if(nodo.elemento.equals(lista.get(i)) == false) return false;
          nodo = nodo.siguiente;
          i++;
        }
        return true;
    }

    /**
     * Regresa el nodo cabeza de la lista.
     * @return el nodo cabeza de la lista.
     */
    public Nodo getCabeza() {
        return cabeza;
    }

    /**
     * Regresa el nodo rabo de la lista.
     * @return el nodo rabo de la lista.
     */
    public Nodo getRabo() {
        return rabo;
    }

    /**
    * Método de bucaNodo que te regresa el nodo en donde se ubica el estudiante.
    *
    **/
    private Nodo buscaNodo(T e){
      Nodo n = cabeza;
      if(e == null) return null;
      while(n != null){
        if(n.elemento.equals(e)) return n;
        n = n.siguiente;
      }
      return null;
    }
    /**
    * Este metodo eliminará el nodo de acuerdo con elemento que se ingrese.
    **/
    private void eliminaNodo(Nodo n){
       if(n == null) return;
       else if(longitud == 1 && cabeza.elemento.equals(n.elemento))limpia();
       else if(buscaNodo(n.elemento) == null) return;
       else if(n.equals(rabo)) eliminaUltimo();
       else if(n.equals(cabeza)) eliminaPrimero();
       else{
         n.anterior.siguiente = n.siguiente;
         n.siguiente.anterior = n.anterior;
         longitud--;
       }
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        return mergeSort(copia(), comparador);
    }
    private Lista<T> mergeSort(Lista<T> l, Comparator<T> comparador){
      if(l.esVacia() || l.getLongitud() == 1) return l;
      int mitad = l.getLongitud() / 2 ;
      Lista<T> l1 = new Lista<T>();
      Lista<T> l2;
      while(l.getLongitud() != mitad){
        l1.agregaFinal(l.getPrimero());
        if(l.getLongitud() != 0)
          l.eliminaPrimero();
      }
      l2 = l.copia();
      return mezcla(mergeSort(l1, comparador), mergeSort(l2, comparador), comparador);
    }

    private Lista<T> mezcla(Lista<T> a, Lista<T> b, Comparator<T> comparador){
        Lista<T> listaOrdenada = new Lista<T>();
        while(a.cabeza != null && b.cabeza != null){
            int i = comparador.compare(a.cabeza.elemento, b.cabeza.elemento);
            if(i <= 0){
              listaOrdenada.agregaFinal(a.getPrimero());
              a.eliminaPrimero();
            }else{
              listaOrdenada.agregaFinal(b.getPrimero());
              b.eliminaPrimero();
            }
        }
        while(a.cabeza != null){
          listaOrdenada.agregaFinal(a.getPrimero());
          a.eliminaPrimero();
        }
        while(b.cabeza != null){
          listaOrdenada.agregaFinal(b.getPrimero());
          b.eliminaPrimero();
        }
        return listaOrdenada;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo n = cabeza;
        while(n != null){
            if(comparador.compare(elemento, n.elemento) == 0) return true;
            n = n.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>> boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }

}
