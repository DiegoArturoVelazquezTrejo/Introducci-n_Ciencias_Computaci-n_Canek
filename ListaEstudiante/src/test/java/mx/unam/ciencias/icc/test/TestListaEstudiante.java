package mx.unam.ciencias.icc.test;

import java.util.Random;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.ListaEstudiante;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link ListaEstudiante}.
 */
public class TestListaEstudiante {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* La lista de estudiantes. */
    private ListaEstudiante lista;

    /* Valida una lista de estudiantes. */
    private void validaListaEstudiante(ListaEstudiante lista) {
        int longitud = lista.getLongitud();
        Estudiante[] arreglo = new Estudiante[longitud];
        int c = 0;
        ListaEstudiante.Nodo nodo = lista.getCabeza();
        while (nodo != null) {
            arreglo[c++] = nodo.get();
            nodo = nodo.getSiguiente();
        }
        Assert.assertTrue(c == longitud);
        c = 0;
        nodo = lista.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(arreglo[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
        Assert.assertTrue(c == longitud);
        c = longitud - 1;
        nodo = lista.getRabo();
        while (nodo != null) {
            Assert.assertTrue(arreglo[c--].equals(nodo.get()));
            nodo = nodo.getAnterior();
        }
    }

    /**
     * Crea un generador de números aleatorios para cada prueba, un número total
     * de elementos para nuestra lista de estudiantes, y una lista de
     * estudiantes.
     */
    public TestListaEstudiante() {
        random = new Random();
        total = 10 + random.nextInt(90);
        lista = new ListaEstudiante();
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#ListaEstudiante}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(lista != null);
        Assert.assertTrue(lista.esVacia());
        Assert.assertTrue(lista.getLongitud() == 0);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#getLongitud}.
     */
    @Test public void testGetLongitud() {
        Assert.assertTrue(lista.getLongitud() == 0);
        for (int i = 0; i < total/2; i++) {
            lista.agregaFinal(TestEstudiante.estudianteAleatorio());
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        for (int i = total/2; i < total; i++) {
            lista.agregaInicio(TestEstudiante.estudianteAleatorio());
            Assert.assertTrue(lista.getLongitud() == i + 1);
        }
        Assert.assertTrue(lista.getLongitud() == total);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#esVacia}.
     */
    @Test public void testEsVacia() {
        Assert.assertTrue(lista.esVacia());
        lista.agregaFinal(TestEstudiante.estudianteAleatorio());
        Assert.assertFalse(lista.esVacia());
        lista.eliminaUltimo();
        Assert.assertTrue(lista.esVacia());
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#agregaFinal}.
     */
    @Test public void testAgregaFinal() {
        lista.agregaFinal(null);
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.esVacia());
        Estudiante e1 = TestEstudiante.estudianteAleatorio(1);
        lista.agregaFinal(e1);
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.getUltimo().equals(e1));
        Estudiante e2 = TestEstudiante.estudianteAleatorio(2);
        lista.agregaInicio(e2);
        validaListaEstudiante(lista);
        Assert.assertFalse(lista.getUltimo().equals(e2));
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaFinal(e);
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.getUltimo().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#agregaInicio}.
     */
    @Test public void testAgregaInicio() {
        lista.agregaInicio(null);
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.esVacia());
        Estudiante e1 = TestEstudiante.estudianteAleatorio();
        lista.agregaInicio(e1);
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.getPrimero().equals(e1));
        Estudiante e2 = TestEstudiante.estudianteAleatorio();
        while (e1.equals(e2))
            e2 = TestEstudiante.estudianteAleatorio();
        lista.agregaFinal(e2);
        validaListaEstudiante(lista);
        Assert.assertFalse(lista.getPrimero().equals(e2));
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaInicio(e);
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.getPrimero().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#inserta}.
     */
    @Test public void testInserta() {
        lista.inserta(0, null);
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.esVacia());
        ListaEstudiante otra = new ListaEstudiante();
        int ini = random.nextInt(total) * 1000000;
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio(ini + 1);
            otra.agregaInicio(e);
            lista.inserta(-1, e);
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.equals(otra));
            Assert.assertTrue(lista.getPrimero().equals(e));
        }
        lista = new ListaEstudiante();
        otra = new ListaEstudiante();
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio(ini + 1);
            otra.agregaInicio(e);
            lista.inserta(0, e);
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.equals(otra));
            Assert.assertTrue(lista.getPrimero().equals(e));
        }
        lista = new ListaEstudiante();
        otra = new ListaEstudiante();
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio(ini + 1);
            otra.agregaFinal(e);
            lista.inserta(lista.getLongitud(), e);
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.equals(otra));
            Assert.assertTrue(lista.getUltimo().equals(e));
        }
        for (int i = 0; i < total; i++) {
            lista = new ListaEstudiante();
            otra = new ListaEstudiante();
            Estudiante ei = null;
            for (int j = 0; j < total; j++) {
                Estudiante e = TestEstudiante.estudianteAleatorio(ini + j);
                otra.agregaFinal(e);
                if (j != i)
                    lista.agregaFinal(e);
                else
                    ei = e;
                validaListaEstudiante(lista);
                validaListaEstudiante(otra);
            }
            Assert.assertFalse(lista.equals(otra));
            Assert.assertTrue(otra.getLongitud() == lista.getLongitud() + 1);
            lista.inserta(i, ei);
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.equals(otra));
        }
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#elimina}.
     */
    @Test public void testElimina() {
        lista.elimina(null);
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.esVacia());
        lista.elimina(TestEstudiante.estudianteAleatorio());
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.esVacia());
        lista.agregaFinal(TestEstudiante.estudianteAleatorio(1));
        Assert.assertFalse(lista.esVacia());
        lista.eliminaUltimo();
        Assert.assertTrue(lista.esVacia());
        int d = random.nextInt(total);
        Estudiante m = null;
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio(i);
            lista.agregaInicio(e);
            if (i == total / 2)
                m = e;
        }
        Estudiante p = lista.getPrimero();
        Estudiante u = lista.getUltimo();
        Assert.assertTrue(lista.contiene(p));
        Assert.assertTrue(lista.contiene(m));
        Assert.assertTrue(lista.contiene(u));
        lista.elimina(p);
        validaListaEstudiante(lista);
        Assert.assertFalse(lista.contiene(p));
        Assert.assertTrue(lista.getLongitud() == --total);
        lista.elimina(m);
        validaListaEstudiante(lista);
        Assert.assertFalse(lista.contiene(m));
        Assert.assertTrue(lista.getLongitud() == --total);
        lista.elimina(u);
        validaListaEstudiante(lista);
        Assert.assertFalse(lista.contiene(u));
        Assert.assertTrue(lista.getLongitud() == --total);
        while (!lista.esVacia()) {
            lista.elimina(lista.getPrimero());
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.getLongitud() == --total);
            if (lista.esVacia())
                continue;
            lista.elimina(lista.getUltimo());
            validaListaEstudiante(lista);
            Assert.assertTrue(lista.getLongitud() == --total);
        }
        Assert.assertTrue(lista.getPrimero() == null);
        Assert.assertTrue(lista.getUltimo() == null);
        Estudiante e1 = TestEstudiante.estudianteAleatorio(1);
        Estudiante e2 = TestEstudiante.estudianteAleatorio(2);
        Estudiante e3 = TestEstudiante.estudianteAleatorio(3);
        lista.agregaFinal(e1);
        lista.agregaFinal(e2);
        lista.agregaFinal(e3);
        lista.agregaFinal(e2);
        lista.elimina(e2);
        Assert.assertTrue(lista.get(0).equals(e1));
        Assert.assertTrue(lista.get(1).equals(e3));
        Assert.assertTrue(lista.get(2).equals(e2));
        lista.limpia();
        lista.agregaFinal(e1);
        lista.agregaFinal(e2);
        lista.agregaFinal(e1);
        lista.agregaFinal(e3);
        lista.elimina(e1);
        Assert.assertTrue(lista.get(0).equals(e2));
        Assert.assertTrue(lista.get(1).equals(e1));
        Assert.assertTrue(lista.get(2).equals(e3));
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#eliminaPrimero}.
     */
    @Test public void testEliminaPrimero() {
        Assert.assertTrue(lista.eliminaPrimero() == null);
        Estudiante[] a = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            a[i] = TestEstudiante.estudianteAleatorio(i);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int n = total;
        while (!lista.esVacia()) {
            Assert.assertTrue(n-- == lista.getLongitud());
            Estudiante e = lista.eliminaPrimero();
            validaListaEstudiante(lista);
            Assert.assertTrue(e.equals(a[i++]));
        }
        Assert.assertTrue(lista.eliminaPrimero() == null);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#eliminaUltimo}.
     */
    @Test public void testEliminaUltimo() {
        Assert.assertTrue(lista.eliminaUltimo() == null);
        Estudiante[] a = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            a[i] = TestEstudiante.estudianteAleatorio(i);
            lista.agregaFinal(a[i]);
        }
        int i = 0;
        int n = total;
        while (!lista.esVacia()) {
            Assert.assertTrue(n-- == lista.getLongitud());
            Estudiante e = lista.eliminaUltimo();
            validaListaEstudiante(lista);
            Assert.assertTrue(e.equals(a[total - ++i]));
        }
        Assert.assertTrue(lista.eliminaUltimo() == null);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#contiene}.
     */
    @Test public void testContiene() {
        Estudiante e = TestEstudiante.estudianteAleatorio();
        Assert.assertFalse(lista.contiene(e));
        int d = random.nextInt(total);
        Estudiante m = null;
        Estudiante n = TestEstudiante.estudianteAleatorio(d - 1);
        for (int i = 0; i < total; i++) {
            e = TestEstudiante.estudianteAleatorio(d++);
            lista.agregaFinal(e);
            if (i == total/2)
                m = e;
        }
        Assert.assertTrue(lista.contiene(m));
        Estudiante o = new Estudiante(m.getNombre(), m.getCuenta(),
                                      m.getPromedio(), m.getEdad());
        Assert.assertTrue(lista.contiene(o));
        Assert.assertFalse(lista.contiene(n));
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#reversa}.
     */
    @Test public void testReversa() {
        ListaEstudiante reversa = lista.reversa();
        Assert.assertTrue(reversa.esVacia());
        Assert.assertFalse(reversa == lista);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(TestEstudiante.estudianteAleatorio());
        reversa = lista.reversa();
        Assert.assertFalse(lista == reversa);
        Assert.assertTrue(reversa.getLongitud() == lista.getLongitud());
        ListaEstudiante.Nodo n1 = lista.getCabeza();
        ListaEstudiante.Nodo n2 = reversa.getRabo();
        while (n1 != null && n2 != null) {
            Assert.assertTrue(n1.get().equals(n2.get()));
            n1 = n1.getSiguiente();
            n2 = n2.getAnterior();
        }
        Assert.assertTrue(n1 == null);
        Assert.assertTrue(n2 == null);
        validaListaEstudiante(reversa);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#copia}.
     */
    @Test public void testCopia() {
        ListaEstudiante copia = lista.copia();
        Assert.assertTrue(copia.esVacia());
        Assert.assertFalse(copia == lista);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(TestEstudiante.estudianteAleatorio());
        copia = lista.copia();
        Assert.assertFalse(lista == copia);
        Assert.assertTrue(copia.getLongitud() == lista.getLongitud());
        ListaEstudiante.Nodo n1 = lista.getCabeza();
        ListaEstudiante.Nodo n2 = copia.getCabeza();
        while (n1 != null && n2 != null) {
            Assert.assertTrue(n1.get().equals(n2.get()));
            n1 = n1.getSiguiente();
            n2 = n2.getSiguiente();
        }
        Assert.assertTrue(n1 == null);
        Assert.assertTrue(n2 == null);
        validaListaEstudiante(copia);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#limpia}.
     */
    @Test public void testLimpia() {
        Estudiante primero = TestEstudiante.estudianteAleatorio();
        lista.agregaFinal(primero);
        for (int i = 0; i < total; i++)
            lista.agregaFinal(TestEstudiante.estudianteAleatorio());
        Estudiante ultimo = TestEstudiante.estudianteAleatorio();
        lista.agregaFinal(ultimo);
        Assert.assertFalse(lista.esVacia());
        Assert.assertTrue(primero.equals(lista.getPrimero()));
        Assert.assertTrue(ultimo.equals(lista.getUltimo()));
        Assert.assertFalse(lista.esVacia());
        Assert.assertFalse(lista.getLongitud() == 0);
        lista.limpia();
        validaListaEstudiante(lista);
        Assert.assertTrue(lista.esVacia());
        Assert.assertTrue(lista.getLongitud() == 0);
        int c = 0;
        ListaEstudiante.Nodo n = lista.getCabeza();
        while (n != null) {
            c++;
            n = n.getSiguiente();
        }
        Assert.assertTrue(c == 0);
        Assert.assertTrue(lista.getPrimero() == null);
        Assert.assertTrue(lista.getUltimo() == null);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#getPrimero}.
     */
    @Test public void testGetPrimero() {
        Assert.assertTrue(lista.getPrimero() == null);
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaInicio(e);
            Assert.assertTrue(lista.getPrimero().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#getUltimo}.
     */
    @Test public void testGetUltimo() {
        Assert.assertTrue(lista.getUltimo() == null);
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaFinal(e);
            Assert.assertTrue(lista.getUltimo().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#get(int)}.
     */
    @Test public void testGet() {
        Estudiante[] a = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            a[i] = TestEstudiante.estudianteAleatorio();
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i++)
            Assert.assertTrue(lista.get(i).equals(a[i]));
        Assert.assertTrue(lista.get(-1) == null);
        Assert.assertTrue(lista.get(-2) == null);
        Assert.assertTrue(lista.get(total) == null);
        Assert.assertTrue(lista.get(total*2) == null);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#indiceDe}.
     */
    @Test public void testIndiceDe() {
        Estudiante e = TestEstudiante.estudianteAleatorio();
        Assert.assertTrue(lista.indiceDe(e) == -1);
        int ini = random.nextInt(total);
        Estudiante[] a = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            a[i] = TestEstudiante.estudianteAleatorio(ini + i);
            lista.agregaFinal(a[i]);
        }
        for (int i = 0; i < total; i ++)
            Assert.assertTrue(i == lista.indiceDe(a[i]));
        e = TestEstudiante.estudianteAleatorio(ini - 10);
        Assert.assertTrue(lista.indiceDe(e) == -1);
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#toString}.
     */
    @Test public void testToString() {
        Assert.assertTrue(lista.toString().equals("[]"));
        Estudiante[] a = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            a[i] = TestEstudiante.estudianteAleatorio(i);
            lista.agregaFinal(a[i]);
        }
        String s = "[";
        for (int i = 0; i < total-1; i++)
            s += String.format("%s, ", a[i]);
        s += String.format("%s]", a[total-1]);
        Assert.assertTrue(s.equals(lista.toString()));
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#equals}.
     */
    @Test public void testEquals() {
        Assert.assertFalse(lista.equals(null));
        ListaEstudiante otra = new ListaEstudiante();
        Assert.assertTrue(lista.equals(otra));
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaFinal(e);
            Estudiante o = new Estudiante(e.getNombre(), e.getCuenta(),
                                          e.getPromedio(), e.getEdad());
            otra.agregaFinal(o);
        }
        Assert.assertTrue(lista.equals(otra));
        Estudiante e = lista.eliminaUltimo();
        Assert.assertFalse(lista.equals(otra));
        lista.agregaFinal(TestEstudiante.estudianteAleatorio(e.getCuenta()+1));
        Assert.assertFalse(lista.equals(otra));
        Assert.assertFalse(lista.equals(""));
        Assert.assertFalse(lista.equals(null));
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#getCabeza}.
     */
    @Test public void testGetCabeza() {
        Assert.assertTrue(lista.getCabeza() == null);
        Estudiante e2 = TestEstudiante.estudianteAleatorio(2);
        Estudiante ce2 = new Estudiante(e2.getNombre(), e2.getCuenta(),
                                        e2.getPromedio(), e2.getEdad());
        lista.agregaInicio(e2);
        Assert.assertTrue(lista.getCabeza() != null);
        Assert.assertTrue(lista.getRabo() != null);
        Assert.assertTrue(lista.getCabeza().get().equals(ce2));
        Estudiante e1 = TestEstudiante.estudianteAleatorio(1);
        Estudiante ce1 = new Estudiante(e1.getNombre(), e1.getCuenta(),
                                        e1.getPromedio(), e1.getEdad());
        lista.agregaInicio(e1);
        Assert.assertTrue(lista.getCabeza() != null);
        Assert.assertTrue(lista.getCabeza().get().equals(ce1));
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaInicio(e);
            Assert.assertTrue(lista.getCabeza().get().equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link ListaEstudiante#getRabo}.
     */
    @Test public void testGetRabo() {
        Assert.assertTrue(lista.getRabo() == null);
        Estudiante e1 = TestEstudiante.estudianteAleatorio(1);
        Estudiante ce1 = new Estudiante(e1.getNombre(), e1.getCuenta(),
                                        e1.getPromedio(), e1.getEdad());
        lista.agregaInicio(e1);
        Assert.assertTrue(lista.getCabeza() != null);
        Assert.assertTrue(lista.getRabo() != null);
        Assert.assertTrue(lista.getRabo().get().equals(ce1));
        Estudiante e2 = TestEstudiante.estudianteAleatorio(2);
        Estudiante ce2 = new Estudiante(e2.getNombre(), e2.getCuenta(),
                                        e2.getPromedio(), e2.getEdad());
        lista.agregaFinal(e2);
        Assert.assertTrue(lista.getRabo() != null);
        Assert.assertTrue(lista.getRabo().get().equals(e2));
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            lista.agregaFinal(e);
            Assert.assertTrue(lista.getRabo().get().equals(e));
        }
    }
}
