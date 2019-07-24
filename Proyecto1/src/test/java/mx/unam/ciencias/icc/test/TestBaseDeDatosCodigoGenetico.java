package test.java.mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import main.java.mx.unam.ciencias.icc.BaseDeDatos;
import main.java.mx.unam.ciencias.icc.BaseDeDatosCodigoGenetico;
import main.java.mx.unam.ciencias.icc.CampoCodigoGenetico;
import main.java.mx.unam.ciencias.icc.CodigoGenetico;
import main.java.mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatosCodigoGenetico}.
 */
public class TestBaseDeDatosCodigoGenetico {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Base de datos de Códigos Geneticos . */
    private BaseDeDatosCodigoGenetico bdd;
    /* Número total de De Códigos Geneticos en la Base De Datos. */
    private int total;

    /* Enumeración espuria. */
    private enum X {
        /* Campo espurio. */
        A;
    }

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de Códigos Geneticos.
     */
    public TestBaseDeDatosCodigoGenetico() {
        random = new Random();
        bdd = new BaseDeDatosCodigoGenetico();
        total = 1 + random.nextInt(100);
    }
    /**
     * Prueba unitaria para {@link
     * BaseDeDatosCodigoGenetico#BaseDeDatosCodigoGenetico}.
     */
    @Test public void testConstructor() {
        Lista codigosGeneticos = bdd.getRegistros();
        Assert.assertFalse(codigosGeneticos == null);
        Assert.assertTrue(codigosGeneticos.getLongitud() == 0);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testGetNumRegistros() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        for (int i = 0; i < total; i++) {
            CodigoGenetico e = TestCodigoGenetico.codigoGeneticoAleatorio();
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Lista l = bdd.getRegistros();
        Lista r = bdd.getRegistros();
        Assert.assertTrue(l.equals(r));
        Assert.assertFalse(l == r);
        CodigoGenetico[] codigosGeneticos = new CodigoGenetico[total];
        for (int i = 0; i < total; i++) {
            codigosGeneticos[i] = TestCodigoGenetico.codigoGeneticoAleatorio();
            bdd.agregaRegistro(codigosGeneticos[i]);
        }
        l = bdd.getRegistros();
        int c = 0;
        Lista.Nodo nodo = l.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(codigosGeneticos[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
        l.elimina(codigosGeneticos[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            CodigoGenetico e = TestCodigoGenetico.codigoGeneticoAleatorio();
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        for (int i = 0; i < total; i++) {
            CodigoGenetico e = TestCodigoGenetico.codigoGeneticoAleatorio();
            bdd.agregaRegistro(e);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            CodigoGenetico e = (CodigoGenetico)bdd.getRegistros().get(i);
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
            CodigoGenetico e = TestCodigoGenetico.codigoGeneticoAleatorio();
            bdd.agregaRegistro(e);
        }
        Lista registros = bdd.getRegistros();
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
            CodigoGenetico e = TestCodigoGenetico.codigoGeneticoAleatorio();
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
        Lista l = bdd.getRegistros();
        int c = 0;
        Lista.Nodo nodo = l.getCabeza();
        String cadena;
        String[] permutaciones;
        while (nodo != null) {
            CodigoGenetico e = (CodigoGenetico)nodo.get();
            permutaciones = e.getPermutaciones();
            cadena = String.format("%s\t%s\t%2.2f\t%d\t%b\t",e.getAdn(), e.getArn(), e.getValorNumerico(),e.getCategoria(),e.getProteina());
            for(int i = 0; i < permutaciones.length-1; i++){
              cadena = cadena + permutaciones[i]+"\t";
            }
            cadena = cadena + permutaciones[permutaciones.length - 1];

            Assert.assertTrue(lineas[c++].equals(cadena));
            nodo = nodo.getSiguiente();
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(100);
        String entrada = "";
        String[] permutaciones;
        CodigoGenetico[] codigosGeneticos = new CodigoGenetico[total];
        for (int i = 0; i < total; i++) {
            codigosGeneticos[i] = TestCodigoGenetico.codigoGeneticoAleatorio();
            permutaciones = codigosGeneticos[i].getPermutaciones();
            entrada = entrada + String.format("%s\t%s\t%2.2f\t%d\t%b\t",codigosGeneticos[i].getAdn(), codigosGeneticos[i].getArn(), codigosGeneticos[i].getValorNumerico(),codigosGeneticos[i].getCategoria(),codigosGeneticos[i].getProteina());
            for(int a = 0; a < permutaciones.length-1; a++){
              entrada = entrada + permutaciones[a]+"\t";
            }
            entrada = entrada + permutaciones[permutaciones.length - 1] + "\n";
            bdd.agregaRegistro(codigosGeneticos[i]);
        }
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
        int c = 0;
        Lista l = bdd.getRegistros();
        Lista.Nodo nodo = l.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(codigosGeneticos[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
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
     * Prueba unitaria para {@link BaseDeDatosCodigoGenetico#creaRegistro}.
     */
    @Test public void testCreaRegistro() {
        CodigoGenetico e = (CodigoGenetico)bdd.creaRegistro();
        Assert.assertTrue(e.getAdn() == null);
        Assert.assertTrue(e.getArn() == null);
        Assert.assertTrue(e.getCategoria() == 0);
        Assert.assertTrue(e.getValorNumerico() == 0.0);
        Assert.assertTrue(e.getProteina() == false);
    }
    /**
     * Prueba unitaria para {@link BaseDeDatosCodigoGenetico#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        CodigoGenetico[] codigosGeneticos = new CodigoGenetico[total];
        for (int i = 0; i < total; i++) {
            CodigoGenetico e =  TestCodigoGenetico.codigoGeneticoAleatorio();
            codigosGeneticos[i] = e;
            bdd.agregaRegistro(e);
        }

        CodigoGenetico codigoGenetico;
        Lista l;
        Lista.Nodo nodo;
        int i;

        for (int k = 0; k < total/10 + 3; k++) {
            i = random.nextInt(total);
            codigoGenetico = codigosGeneticos[i];

            String adn = codigoGenetico.getAdn();
            l = bdd.buscaRegistros(CampoCodigoGenetico.ADN, adn);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(codigoGenetico));
            nodo = l.getCabeza();
            while (nodo != null) {
                CodigoGenetico e = (CodigoGenetico)nodo.get();
                Assert.assertTrue(e.getAdn().indexOf(adn) > -1);
                nodo = nodo.getSiguiente();
            }

            Integer categoria = new Integer(codigoGenetico.getCategoria());
            l = bdd.buscaRegistros(CampoCodigoGenetico.CATEGORIA, categoria);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(codigoGenetico));
            nodo = l.getCabeza();
            while (nodo != null) {
                CodigoGenetico e = (CodigoGenetico)nodo.get();
                Assert.assertTrue(e.getCategoria() >= categoria.intValue());
                nodo = nodo.getSiguiente();
            }
            Boolean bc = new Boolean(codigoGenetico.getProteina());
            l = bdd.buscaRegistros(CampoCodigoGenetico.PROTEINA, bc);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(codigoGenetico));
            nodo = l.getCabeza();
            while (nodo != null) {
                CodigoGenetico e = (CodigoGenetico)nodo.get();
                Assert.assertTrue(e.getProteina() == bc);
                nodo = nodo.getSiguiente();
            }

            Double valorNumerico = new Double(codigoGenetico.getValorNumerico());
            l = bdd.buscaRegistros(CampoCodigoGenetico.VALORNUMERICO, valorNumerico);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(codigoGenetico));
            nodo = l.getCabeza();
            while (nodo != null) {
                CodigoGenetico e = (CodigoGenetico)nodo.get();
                Assert.assertTrue(e.getValorNumerico() >= valorNumerico.doubleValue());
                nodo = nodo.getSiguiente();
            }

            String arn = codigoGenetico.getArn();
            l = bdd.buscaRegistros(CampoCodigoGenetico.ARN, arn);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(codigoGenetico));
            nodo = l.getCabeza();
            while (nodo != null) {
                CodigoGenetico e = (CodigoGenetico)nodo.get();
                Assert.assertTrue(e.getArn().indexOf(arn) > -1);
                nodo = nodo.getSiguiente();
            }
        }

        l = bdd.buscaRegistros(CampoCodigoGenetico.ADN,
                               "PRUEBA-NULLA");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.VALORNUMERICO,
                               new Integer(10000000));
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.ARN,
                               "PRUEBA ARN NULLA");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.PROTEINA,
                               null);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoCodigoGenetico.ADN, "");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.CATEGORIA,
                               new Integer(Integer.MAX_VALUE));
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.VALORNUMERICO,
                               new Double(Double.MAX_VALUE));
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoCodigoGenetico.PERMUTACIONES, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.CATEGORIA, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCodigoGenetico.PROTEINA, null);
        Assert.assertTrue(l.esVacia());

        try {
            l = bdd.buscaRegistros(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            l = bdd.buscaRegistros(X.A, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }
}
