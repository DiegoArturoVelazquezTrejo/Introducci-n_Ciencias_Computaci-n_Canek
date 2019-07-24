package mx.unam.ciencias.icc;
/**
 * <p>Clase para listas de estudiantes doblemente ligadas.</p>
 *
 * <p>Las listas de estudiantes nos permiten agregar elementos al inicio o final
 * de la lista, eliminar elementos de la lista, comprobar si un elemento está o
 * no en la lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas de estudiantes son iterables utilizando sus nodos. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * <p>Los elementos en una lista de estudiantes siempre son instancias de la
 * clase {@link Estudiante}.</p>
 */
public class ListaEstudiante {

    /**
     * Clase interna para nodos.
     */
     public class Nodo {

         /* El elemento del nodo. */
         private Estudiante elemento;
         /* El nodo anterior. */
         private Nodo anterior;
         /* El nodo siguiente. */
         private Nodo siguiente;

         /* Construye un nodo con un elemento. */
         private Nodo(Estudiante elemento) {
             this.elemento = elemento;
         }

         /**
          * Regresa el nodo anterior del nodo.
          * @return el nodo anterior del nodo.
          */
         public Nodo getAnterior() {
             return anterior;
         }

         /**
          * Regresa el nodo siguiente del nodo.
          * @return el nodo siguiente del nodo.
          */
         public Nodo getSiguiente() {
             return siguiente;
         }

         /**
          * Regresa el elemento del nodo.
          * @return el elemento del nodo.
          */
         public Estudiante get() {
             return elemento;
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
     * @param elemento el elemento a agregar. El elemento se agrega únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void agregaFinal(Estudiante elemento) {
        if(elemento == null) return;
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
     * @param elemento el elemento a agregar. El elemento se agrega únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void agregaInicio(Estudiante elemento) {
        if(elemento == null) return;
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
     * @param elemento el elemento a insertar. El elemento se inserta únicamente
     *                 si es distinto de <code>null</code>.
     */
    public void inserta(int i, Estudiante elemento) {
      if(elemento == null) return;
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
    public void elimina(Estudiante elemento) {
        eliminaNodo(buscaNodo(elemento));
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo, o
     *         <code>null</code> si la lista es vacía.
     */
    public Estudiante eliminaPrimero() {
        if(esVacia()) return null;
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
     * @return el último elemento de la lista antes de eliminarlo, o
     *         <code>null</code> si la lista es vacía.
     */
    public Estudiante eliminaUltimo() {
        if(esVacia()) return null;
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
    public boolean contiene(Estudiante elemento) {
        return buscaNodo(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public ListaEstudiante reversa() {
        ListaEstudiante lista = new ListaEstudiante();
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
    public ListaEstudiante copia() {
        Nodo nodo = cabeza;
        ListaEstudiante copiaLista = new ListaEstudiante();
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
     * @return el primer elemento de la lista, o <code>null</code> si la lista
     *         es vacía.
     */
    public Estudiante getPrimero() {
        return (longitud != 0) ? cabeza.elemento : null;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el último elemento de la lista, o <code>null</code> si la lista
     *         es vacía.
     */
    public Estudiante getUltimo() {
        return (longitud != 0) ? rabo.elemento : null;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista, o <code>null</code> si
     *         <em>i</em> es menor que cero o mayor o igual que el número de
     *         elementos en la lista.
     */
    public Estudiante get(int i) {
        if(i < 0 || i >= longitud) return null;
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
    public int indiceDe(Estudiante elemento) {
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
    public String toString() {
      if(esVacia()) return "[]";
      String s = "[";
      for (int i = 0; i < longitud-1; i++)
          s += String.format("%s, ", get(i));
      s += String.format("%s]", get(longitud-1));
      return s;
    }

    /**
     * Nos dice si la lista es igual a la lista recibida.
     * @param lista la lista con la que hay que comparar.
     * @return <tt>true</tt> si la lista es igual a la recibida;
     *         <tt>false</tt> en otro caso.
     */
    public boolean equals(ListaEstudiante lista) {
        if(lista == null) return false;
        else if(lista.getLongitud() != longitud) return false;
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
    private Nodo buscaNodo(Estudiante e){
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
}
