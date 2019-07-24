package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.CampoEstudiante;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatosEstudiantes}.
 */
public class TestBaseDeDatosEstudiantes {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Base de datos de estudiantes. */
    private BaseDeDatosEstudiantes bdd;
    /* Número total de estudiantes. */
    private int total;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de estudiantes.
     */
    public TestBaseDeDatosEstudiantes() {
        random = new Random();
        bdd = new BaseDeDatosEstudiantes();
        total = 1 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosEstudiantes#BaseDeDatosEstudiantes}.
     */
    @Test public void testConstructor() {
        Lista<Estudiante> estudiantes = bdd.getRegistros();
        Assert.assertFalse(estudiantes == null);
        Assert.assertTrue(estudiantes.getLongitud() == 0);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testGetNumRegistros() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Lista<Estudiante> l = bdd.getRegistros();
        Lista<Estudiante> r = bdd.getRegistros();
        Assert.assertTrue(l.equals(r));
        Assert.assertFalse(l == r);
        Estudiante[] estudiantes = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            estudiantes[i] = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(estudiantes[i]);
        }
        l = bdd.getRegistros();
        int c = 0;
        for (Estudiante e : l)
            Assert.assertTrue(estudiantes[c++].equals(e));
        l.elimina(estudiantes[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        int ini = random.nextInt(1000000);
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio(ini + i);
            bdd.agregaRegistro(e);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Estudiante e = bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            bdd.eliminaRegistro(e);
            Assert.assertFalse(bdd.getRegistros().contiene(e));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(e);
        }
        Lista<Estudiante> registros = bdd.getRegistros();
        Assert.assertFalse(registros.esVacia());
        Assert.assertFalse(registros.getLongitud() == 0);
        bdd.limpia();
        registros = bdd.getRegistros();
        Assert.assertTrue(registros.esVacia());
        Assert.assertTrue(registros.getLongitud() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            Estudiante e = TestEstudiante.estudianteAleatorio();
            bdd.agregaRegistro(e);
        }
        String guardado = "";
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            bdd.guarda(out);
            out.close();
            guardado = swOut.toString();
        } catch (IOException ioe) {
            Assert.fail();
        }
        String[] lineas = guardado.split("\n");
        Assert.assertTrue(lineas.length == total);
        Lista<Estudiante> l = bdd.getRegistros();
        int c = 0;
        for (Estudiante e : l) {
            String el = String.format("%s\t%d\t%2.2f\t%d",
                                      e.getNombre(),
                                      e.getCuenta(),
                                      e.getPromedio(),
                                      e.getEdad());
            Assert.assertTrue(lineas[c++].equals(el));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(1000000);
        String entrada = "";
        Estudiante[] estudiantes = new Estudiante[total];
        for (int i = 0; i < total; i++) {
            estudiantes[i] = TestEstudiante.estudianteAleatorio(ini + i);
            entrada += String.format("%s\t%d\t%2.2f\t%d\n",
                                     estudiantes[i].getNombre(),
                                     estudiantes[i].getCuenta(),
                                     estudiantes[i].getPromedio(),
                                     estudiantes[i].getEdad());
            bdd.agregaRegistro(estudiantes[i]);
        }
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertTrue(l.getLongitud() == total);
        int c = 0;
        for (Estudiante e : l)
            Assert.assertTrue(estudiantes[c++].equals(e));
        entrada = "";
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosEstudiantes#creaRegistro}.
     */
    @Test public void testCreaRegistro() {
        Estudiante e = bdd.creaRegistro();
        Assert.assertTrue(e.getNombre() == null);
        Assert.assertTrue(e.getCuenta() == 0);
        Assert.assertTrue(e.getPromedio() == 0.0);
        Assert.assertTrue(e.getEdad() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosEstudiantes#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        Estudiante[] estudiantes = new Estudiante[total];
        int ini = 1000000 + random.nextInt(999999);
        for (int i = 0; i < total; i++) {
            Estudiante e =  new Estudiante(String.valueOf(ini+i),
                                           ini+i, (i * 10.0) / total, i);
            estudiantes[i] = e;
            bdd.agregaRegistro(e);
        }

        Estudiante estudiante;
        Lista<Estudiante> l;
        int i;

        for (int k = 0; k < total/10 + 3; k++) {
            i = random.nextInt(total);
            estudiante = estudiantes[i];

            String nombre = estudiante.getNombre();
            l = bdd.buscaRegistros(CampoEstudiante.NOMBRE, nombre);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getNombre().indexOf(nombre) > -1);
            int n = nombre.length();
            String bn = nombre.substring(random.nextInt(2),
                                         2 + random.nextInt(n-2));
            l = bdd.buscaRegistros(CampoEstudiante.NOMBRE, bn);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getNombre().indexOf(bn) > -1);

            int cuenta = estudiante.getCuenta();
            l = bdd.buscaRegistros(CampoEstudiante.CUENTA, cuenta);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getCuenta() >= cuenta);
            int bc = cuenta - 10;
            l = bdd.buscaRegistros(CampoEstudiante.CUENTA, bc);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getCuenta() >= bc);

            double promedio = estudiante.getPromedio();
            l = bdd.buscaRegistros(CampoEstudiante.PROMEDIO, promedio);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getPromedio() >= promedio);
            double bp = promedio - 5.0;
            l = bdd.buscaRegistros(CampoEstudiante.PROMEDIO, bp);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getPromedio() >= bp);

            int edad = estudiante.getEdad();
            l = bdd.buscaRegistros(CampoEstudiante.EDAD, edad);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getEdad() >= edad);
            int be = edad - 10;
            l = bdd.buscaRegistros(CampoEstudiante.EDAD, be);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(estudiante));
            for (Estudiante e : l)
                Assert.assertTrue(e.getEdad() >= be);
        }

        l = bdd.buscaRegistros(CampoEstudiante.NOMBRE,
                               "xxx-nombre");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.CUENTA, 9123456);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.PROMEDIO, 97.12);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.EDAD, 127);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoEstudiante.NOMBRE, "");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.CUENTA, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.PROMEDIO, Double.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.EDAD, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoEstudiante.NOMBRE, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.CUENTA, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.PROMEDIO, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoEstudiante.EDAD, null);
        Assert.assertTrue(l.esVacia());

        try {
            l = bdd.buscaRegistros(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }
}
