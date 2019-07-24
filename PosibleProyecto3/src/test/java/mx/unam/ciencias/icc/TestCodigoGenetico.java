package mx.unam.ciencias.icc.test;

import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import mx.unam.ciencias.icc.CodigoGenetico;
import mx.unam.ciencias.icc.CampoCodigoGenetico;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Estudiante}.
 */
public class TestCodigoGenetico {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);


    private static final String[] CODIGOSGENETICOS = {
            "AG-TC-CT","TC-CT-GA","XA-CT-GA","GA-CT-TC","AT-GC-TA",
            "XY-TC-AG","TC-CT-XA","GA-CT-TC","XY-XY-AG","AT-GC-CG"
    };


    private static final String[] ADN = {
            "A-T-C", "C-T-C", "A-X-G", "G-G-A", "C-T-A",
            "T-T-X", "X-A-X", "G-C-T", "X-A-G", "X-G-X"
    };


    private static Random random;


    public static String adnAleatorio() {
        int n = random.nextInt(CODIGOSGENETICOS.length);
        return CODIGOSGENETICOS[n];
    }


    public static double valorNumericoAleatorio() {
        return  random.nextInt() + 0.5;
    }

    public static int categoriaAleatoria() {
        return random.nextInt(20);
    }

    public static String traduccionAleatoria() {
        int n = random.nextInt(CODIGOSGENETICOS.length);
        return CODIGOSGENETICOS[n];
    }

    public static String nombreAleatorio() {
        int n = random.nextInt(CODIGOSGENETICOS.length);
        return CODIGOSGENETICOS[n];
    }

    public static CodigoGenetico codigoGeneticoAleatorio() {
        return new CodigoGenetico(nombreAleatorio(),
                adnAleatorio(),
                valorNumericoAleatorio(),
                categoriaAleatoria(),
                traduccionAleatoria());
    }


    private CodigoGenetico codigoGenetico;

    /**
     * Prueba unitaria para {@link
     * CodigoGenetico#CodigoGenetico(String,String,double,int, boolean)}.
     */
    @Test public void testConstructor() {
        String adn = adnAleatorio();
        String nombre = nombreAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
        Assert.assertTrue(codigoGenetico.getNombre().equals(nombre));
        Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
        Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
        Assert.assertTrue(codigoGenetico.getTraduccion().equals(traduccion));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getAdn()}.
     */
    @Test public void testGetAdn() {
      String adn = adnAleatorio();
      String nombre = nombreAleatorio();
      double valorNumerico = valorNumericoAleatorio();
      int categoria = categoriaAleatoria();
      String traduccion = traduccionAleatoria();
      codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
      Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
      Assert.assertFalse(codigoGenetico.getAdn().equals(adn + " X"));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setAdn}.
     */
    @Test public void testSetAdn() {
        String adn = adnAleatorio();
        String nuevoAdn = adn + " X";
        String nombre = nombreAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
        Assert.assertFalse(codigoGenetico.getAdn().equals(nuevoAdn));
        codigoGenetico.setAdn(nuevoAdn);
        Assert.assertFalse(codigoGenetico.getAdn().equals(adn));
        Assert.assertTrue(codigoGenetico.getAdn().equals(nuevoAdn));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setNOMBRE}.
     */
    @Test public void testSetNombre() {
        String nombre = nombreAleatorio();
        String nuevoNombre = nombre + " X";
        String adn = adnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        Assert.assertTrue(codigoGenetico.getNombre().equals(nombre));
        Assert.assertFalse(codigoGenetico.getNombre().equals(nuevoNombre));
        codigoGenetico.setNombre(nuevoNombre);
        Assert.assertFalse(codigoGenetico.getNombre().equals(nombre));
        Assert.assertTrue(codigoGenetico.getNombre().equals(nuevoNombre));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getValorNumerico}.
     */
    @Test public void testGetValorNumerico() {
      String adn = adnAleatorio();
      String nombre = nombreAleatorio();
      double valorNumerico = valorNumericoAleatorio();
      int categoria = categoriaAleatoria();
      String traduccion = traduccionAleatoria();
      codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
      Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
      Assert.assertFalse(codigoGenetico.getValorNumerico() == valorNumerico + 100);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setValorNumerico}.
     */
    @Test public void testSetValorNumerico() {
      String adn = adnAleatorio();
      String nombre = nombreAleatorio();
      double valorNumerico = valorNumericoAleatorio();
      int categoria = categoriaAleatoria();
      String traduccion = traduccionAleatoria();
      codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
      double nuevoValorNumerico = valorNumerico + 10;
      Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
      Assert.assertFalse(codigoGenetico.getValorNumerico() == valorNumerico + 100);
      codigoGenetico.setValorNumerico(nuevoValorNumerico);
      Assert.assertFalse(codigoGenetico.getValorNumerico() == valorNumerico);
      Assert.assertTrue(codigoGenetico.getValorNumerico() == nuevoValorNumerico);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getCategoria}.
     */
    @Test public void testGetCategoria() {
      String adn = adnAleatorio();
      String nombre = nombreAleatorio();
      double valorNumerico = valorNumericoAleatorio();
      int categoria = categoriaAleatoria();
      String traduccion = traduccionAleatoria();
      codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
      Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
      Assert.assertFalse(codigoGenetico.getCategoria() == categoria + 10);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setCategoria}.
     */
    @Test public void testSetCategoria() {
      String adn = adnAleatorio();
      String nombre = nombreAleatorio();
      double valorNumerico = valorNumericoAleatorio();
      int categoria = categoriaAleatoria();
      String traduccion = traduccionAleatoria();
      codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
      int nuevaCategoria = categoria + 5;
      Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
      Assert.assertFalse(codigoGenetico.getCategoria() == nuevaCategoria);
      codigoGenetico.setCategoria(nuevaCategoria);
      Assert.assertFalse(codigoGenetico.getCategoria() == categoria);
      Assert.assertTrue(codigoGenetico.getCategoria() == nuevaCategoria);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#toString}.
     */
    @Test public void testToString() {
        String adn = adnAleatorio();
        String nombre = nombreAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        String cadena = String.format("Nombre   : %s\n" +
                        "ADN   : %s\n" +
                        "valorNumerico   : %09f\n" +
                        "Categoría  :  %d\n" +
                        "Traducción  : %s\n"+
                        "Numero de cadenas: %d\n",
                nombre, adn, valorNumerico,categoria,traduccion, codigoGenetico.getNumeroCadenas());
        Assert.assertTrue(codigoGenetico.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#equals}.
     */
    @Test public void testEquals() {
        String adn = adnAleatorio();
        String nombre = nombreAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        CodigoGenetico igual = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        Assert.assertTrue(codigoGenetico.equals(igual));
        CodigoGenetico distinto = new CodigoGenetico(nombre+"X", adn+"Y", valorNumerico+2.0, categoria+1, traduccion + "S");
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(nombre+"X", adn+"Y", valorNumerico+2.0, categoria, traduccion);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(nombre+"X", adn, valorNumerico, categoria, "X");
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(nombre, adn, valorNumerico, categoria + 1, "Q");
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(nombre, adn+"Y", valorNumerico, categoria, traduccion);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        Assert.assertFalse(codigoGenetico.equals("Una cadena"));
        Assert.assertFalse(codigoGenetico.equals(null));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#guarda}.
     */

    @Test public void testGuarda() {
        String entrada = "";
        String adn = adnAleatorio();
        String nombre = nombreAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            codigoGenetico.guarda(out);
            out.close();
            String guardado = swOut.toString();

            String s = String.format("%s\t%s\t%2.2f\t%d\t%s\t%d\n",nombre,adn,valorNumerico,categoria,traduccion, codigoGenetico.getNumeroCadenas());
            Assert.assertTrue(guardado.equals(s));
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#carga}.
     */
     @Test public void testCarga() {
         codigoGenetico = new CodigoGenetico(null, null, 0.0, 0, null);
         String adn = adnAleatorio();
         String nombre = nombreAleatorio();
         double valorNumerico = 12.78;
         int categoria = categoriaAleatoria();
         String traduccion = traduccionAleatoria();

         String entrada = String.format("%s\t%s\t%2.2f\t%d\t%s\t%d\n",nombre,adn,valorNumerico,categoria,traduccion, codigoGenetico.getNumeroCadenas());
         System.out.print(entrada);
         try {
             StringReader srIn = new StringReader(entrada);
             BufferedReader in = new BufferedReader(srIn);
             Assert.assertTrue(codigoGenetico.carga(in));
             in.close();
             Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
             Assert.assertTrue(codigoGenetico.getNombre().equals(nombre));
             Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
             Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
             Assert.assertTrue(codigoGenetico.getTraduccion().equals(traduccion));
         } catch (IOException ioe) {
             Assert.fail();
         }

         entrada = "";
         try {
             StringReader srIn = new StringReader(entrada);
             BufferedReader in = new BufferedReader(srIn);
             Assert.assertFalse(codigoGenetico.carga(in));
             in.close();
             Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
             Assert.assertTrue(codigoGenetico.getNombre().equals(nombre));
             Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
             Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
             Assert.assertTrue(codigoGenetico.getTraduccion().equals(traduccion));
         } catch (IOException ioe) {
             Assert.fail();
         }

         entrada = "\n";
         try {
             StringReader srIn = new StringReader(entrada);
             BufferedReader in = new BufferedReader(srIn);
             Assert.assertFalse(codigoGenetico.carga(in));
             in.close();
             Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
             Assert.assertTrue(codigoGenetico.getNombre().equals(nombre));
             Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
             Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
             Assert.assertTrue(codigoGenetico.getTraduccion().equals(traduccion));
         } catch (IOException ioe) {
             Assert.fail();
         }

         entrada = "a\ta\ta\ta";
         try {
             StringReader srIn = new StringReader(entrada);
             BufferedReader in = new BufferedReader(srIn);
             codigoGenetico.carga(in);
             Assert.fail();
         } catch (IOException ioe) {}

         entrada = "a\ta";
         try {
             StringReader srIn = new StringReader(entrada);
             BufferedReader in = new BufferedReader(srIn);
             codigoGenetico.carga(in);
             Assert.fail();
         } catch (IOException ioe) {}
     }

    /**
     * Prueba unitaria para {@link Estudiante#caza}.
     */
    @Test public void testCaza() {

        String adn = adnAleatorio();
        String nombre = nombreAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        String traduccion = traduccionAleatoria();
        codigoGenetico = new CodigoGenetico(nombre, adn, valorNumerico, categoria, traduccion);
        String n = codigoGenetico.getAdn();
        int m = codigoGenetico.getAdn().length();
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ADN, n));
        n = codigoGenetico.getAdn().substring(0, m/2);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ADN, n));
        n = codigoGenetico.getAdn().substring(m/2, m);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ADN, n));
        n = codigoGenetico.getAdn().substring(m/3, 2*(m/3));
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ADN, n));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ADN, ""));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ADN, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ADN, new Integer(1000)));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ADN, null));

        n = codigoGenetico.getNombre();
        m = codigoGenetico.getNombre().length();
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.NOMBRE, n));
        n = codigoGenetico.getNombre().substring(0, m/2);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.NOMBRE, n));
        n = codigoGenetico.getNombre().substring(m/2, m);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.NOMBRE, n));
        n = codigoGenetico.getNombre().substring(m/3, 2*(m/3));
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.NOMBRE, n));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.NOMBRE, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.NOMBRE, new Integer(4)));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ADN, null));

        Integer c = new Integer(codigoGenetico.getCategoria());
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.CATEGORIA, c));
        c = new Integer(codigoGenetico.getCategoria() - 100);
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.CATEGORIA, c));
        c = new Integer(codigoGenetico.getCategoria() + 100);
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.CATEGORIA, c));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.CATEGORIA, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.CATEGORIA, null));

        Double p = new Double(codigoGenetico.getValorNumerico());
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.VALORNUMERICO, p));
        p = new Double(codigoGenetico.getValorNumerico() - 5.0);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.VALORNUMERICO, p));
        p = new Double(codigoGenetico.getValorNumerico() + 5.0);
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.VALORNUMERICO, p));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.VALORNUMERICO, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.VALORNUMERICO, null));

        n = codigoGenetico.getTraduccion();
        m = codigoGenetico.getTraduccion().length();
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.TRADUCCION, n));
        n = codigoGenetico.getTraduccion().substring(0, m/2);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.TRADUCCION, n));
        n = codigoGenetico.getTraduccion().substring(m/2, m);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.TRADUCCION, n));
        n = codigoGenetico.getTraduccion().substring(m/3, 2*(m/3));
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.TRADUCCION, n));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.TRADUCCION, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.TRADUCCION, new Integer(4)));

    }
    /* Inicializa el generador de números aleatorios. */
    static {
        random = new Random();
    }
}
