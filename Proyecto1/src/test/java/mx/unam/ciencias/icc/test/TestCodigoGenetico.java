package test.java.mx.unam.ciencias.icc.test;

import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import main.java.mx.unam.ciencias.icc.CodigoGenetico;
import main.java.mx.unam.ciencias.icc.CampoCodigoGenetico;
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


    private static final String[] ARN = {
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
    public static boolean proteinaAleatoria() {
        return random.nextInt(10) > 5 ? true : false;
    }


    public static String arnAleatorio() {
        int n = random.nextInt(ARN.length);
        return ARN[n];
    }

    public String[] aleatoriaConstruirPermutaciones(String adn){
        String[] partes = adn.split("-");
        String[] permutaciones = new String[partes.length * 2];
        int alelo1, alelo2, alelo3;
        for(int i = 0; i < permutaciones.length; i++){
          alelo1 = random.nextInt(partes.length-1);
          alelo2 = random.nextInt(partes.length-1);
          alelo3 = random.nextInt(partes.length-1);
          permutaciones[i] = partes[alelo1] + "-" + partes[alelo2] + "-" + partes[alelo3];
        }
        return permutaciones; 
    }


    public static CodigoGenetico codigoGeneticoAleatorio() {
        return new CodigoGenetico(adnAleatorio(),
                arnAleatorio(),
                valorNumericoAleatorio(),
                categoriaAleatoria(),
                proteinaAleatoria());
    }


    private CodigoGenetico codigoGenetico;

    /* Enumeración espuria. */
    private enum X {
        /* Campo espurio. */
        A;
    }

    /**
     * Prueba unitaria para {@link
     * CodigoGenetico#CodigoGenetico(String,String,double,int, boolean)}.
     */
    @Test public void testConstructor() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
        Assert.assertTrue(codigoGenetico.getArn().equals(arn));
        Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
        Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
        Assert.assertTrue(codigoGenetico.getProteina() == proteina);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getAdn()}.
     */
    @Test public void testGetAdn() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
        Assert.assertFalse(codigoGenetico.getAdn().equals(adn + " X"));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setAdn}.
     */
    @Test public void testSetAdn() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        String nuevoAdn = adn + " X";
        double valorNumerico = valorNumericoAleatorio();
        int caegoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, caegoria, proteina);
        Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
        Assert.assertFalse(codigoGenetico.getAdn().equals(nuevoAdn));
        codigoGenetico.setAdn(nuevoAdn);
        Assert.assertFalse(codigoGenetico.getAdn().equals(adn));
        Assert.assertTrue(codigoGenetico.getAdn().equals(nuevoAdn));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getArn()}.
     */
    @Test public void testGetArn() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getArn().equals(arn));
        Assert.assertFalse(codigoGenetico.getArn().equals(arn + " X"));
    }

    /**
    * Prueba unitaria para {@link codigoGenetico#getPermutaciones}
    */
    @Test public void testSetPermutaciones(){
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        String[] permutaciones = aleatoriaConstruirPermutaciones(adn);
        String[] permutaciones1 = aleatoriaConstruirPermutaciones(adn);
        Assert.assertFalse(codigoGenetico.getPermutaciones().equals(permutaciones));
        codigoGenetico.setPermutaciones(permutaciones);
        Assert.assertTrue(codigoGenetico.getPermutaciones().equals(permutaciones));
        Assert.assertFalse(codigoGenetico.getPermutaciones().equals(permutaciones1));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setArn}.
     */
    @Test public void testSetArn() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        String nuevoArn = arn + " X";
        double valorNumerico = valorNumericoAleatorio();
        int caegoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, caegoria, proteina);
        Assert.assertTrue(codigoGenetico.getArn().equals(arn));
        Assert.assertFalse(codigoGenetico.getArn().equals(nuevoArn));
        codigoGenetico.setArn(nuevoArn);
        Assert.assertFalse(codigoGenetico.getArn().equals(arn));
        Assert.assertTrue(codigoGenetico.getArn().equals(nuevoArn));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getValorNumerico}.
     */
    @Test public void testGetValorNumerico() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
        Assert.assertFalse(codigoGenetico.getValorNumerico() == valorNumerico + 100);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setValorNumerico}.
     */
    @Test public void testSetValorNumerico() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
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
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
        Assert.assertFalse(codigoGenetico.getCategoria() == categoria + 10);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setCategoria}.
     */
    @Test public void testSetCategoria() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        int nuevaCategoria = categoria + 5;
        Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
        Assert.assertFalse(codigoGenetico.getCategoria() == nuevaCategoria);
        codigoGenetico.setCategoria(nuevaCategoria);
        Assert.assertFalse(codigoGenetico.getCategoria() == categoria);
        Assert.assertTrue(codigoGenetico.getCategoria() == nuevaCategoria);
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#getProteina}.
     */
    @Test public void testGetProteina() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getProteina() == proteina);
        Assert.assertFalse(codigoGenetico.getProteina() == !(proteina));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#setProteina}.
     */
    @Test public void testSetProteina() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        Assert.assertTrue(codigoGenetico.getProteina() == proteina);
        Assert.assertFalse(codigoGenetico.getProteina() == !(proteina));
        codigoGenetico.setProteina(!(proteina));
        Assert.assertFalse(codigoGenetico.getProteina() == proteina);
        Assert.assertTrue(codigoGenetico.getProteina() == !(proteina));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#toString}.
     */
    @Test public void testToString() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        String cadena = String.format("ADN   : %s\n" +
                                      "ARN   : %s\n" +
                                      "valorNumerico   : %09f\n" +
                                      "Categoría  :  %d\n" +
                                      "Con proteina  : %b\n",
        adn, arn, valorNumerico,categoria,proteina);
        String[] permutaciones = codigoGenetico.getPermutaciones();
        for(int i = 0 ; i < permutaciones.length; i++){
          cadena+= String.format("P%d     : %s\n",i+1, permutaciones[i]);
        }
        Assert.assertTrue(codigoGenetico.toString().equals(cadena));
        categoria = 5;
        valorNumerico = 9.4;
        codigoGenetico.setCategoria(categoria);
        codigoGenetico.setValorNumerico(valorNumerico);
        cadena = String.format("ADN   : %s\n" +
                                      "ARN   : %s\n" +
                                      "valorNumerico   : %09f\n" +
                                      "Categoría  :  %d\n" +
                                      "Con proteina  : %b\n",
        adn, arn, valorNumerico,categoria,proteina);
        for(int i = 0 ; i < permutaciones.length; i++){
          cadena+= String.format("P%d     : %s\n",i+1, permutaciones[i]);
        }
        Assert.assertTrue(codigoGenetico.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#equals}.
     */
    @Test public void testEquals() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        CodigoGenetico igual = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        igual.setPermutaciones(codigoGenetico.getPermutaciones());
        Assert.assertTrue(codigoGenetico.equals(igual));
        CodigoGenetico distinto = new CodigoGenetico(adn+"X", arn+"Y", valorNumerico+2.0, categoria+1, !proteina);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(adn+"X", arn+"Y", valorNumerico+2.0, categoria, proteina);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(adn+"X", arn, valorNumerico, categoria, !proteina);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(adn, arn, valorNumerico, categoria, !proteina);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        distinto = new CodigoGenetico(adn, arn+"Y", valorNumerico, categoria, proteina);
        Assert.assertFalse(codigoGenetico.equals(distinto));
        Assert.assertFalse(codigoGenetico.equals("Una cadena"));
        Assert.assertFalse(codigoGenetico.equals(null));
    }

    /**
     * Prueba unitaria para {@link CodigoGenetico#guarda}.
     */
    @Test public void testGuarda() {
        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);
        String[] permutaciones = codigoGenetico.getPermutaciones();
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            codigoGenetico.guarda(out);
            out.close();
            String guardado = swOut.toString();

            String s = String.format("%s\t%s\t%2.2f\t%d\t%b\t",adn,arn,valorNumerico,categoria,proteina);
            for(int i = 0; i < permutaciones.length-1; i++){
              s = s + permutaciones[i]+"\t";
            }
            s = s + permutaciones[permutaciones.length - 1] + "\n";
            Assert.assertTrue(guardado.equals(s));
        } catch (IOException ioe) {
            Assert.fail();
        }
    }
    private static String[] permutacionesAleatorias(String adn){
       String[] partes = adn.split("-");
       String[] permutaciones;
       permutaciones = new String[partes.length * 2];
       int alelo1, alelo2, alelo3;
       for(int i = 0; i < permutaciones.length; i++){
         alelo1 = random.nextInt(partes.length-1);
         alelo2 = random.nextInt(partes.length-1);
         alelo3 = random.nextInt(partes.length-1);
         permutaciones[i] = partes[alelo1] + "-" + partes[alelo2] + "-" + partes[alelo3];
       }
       return permutaciones;
    }
    /**
     * Prueba unitaria para {@link CodigoGenetico#carga}.
     */
    @Test public void testCarga() {
        codigoGenetico = new CodigoGenetico(null, null, 0.0, 0, false);

        String adn = adnAleatorio();
        String arn = arnAleatorio();
        double valorNumerico = 12.78;
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        String[] permutaciones = permutacionesAleatorias(adn);

        String entrada = String.format("%s\t%s\t%2.2f\t%d\t%b\t",adn,arn,valorNumerico,categoria,proteina);
        for(int i = 0; i < permutaciones.length-1; i++){
          entrada = entrada + permutaciones[i]+"\t";
        }
        entrada = entrada + permutaciones[permutaciones.length - 1] + "\n";

        try {
            StringReader srIn = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srIn);
            Assert.assertTrue(codigoGenetico.carga(in));
            in.close();
            Assert.assertTrue(codigoGenetico.getAdn().equals(adn));
            Assert.assertTrue(codigoGenetico.getArn().equals(arn));
            Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
            Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
            Assert.assertTrue(codigoGenetico.getProteina() == proteina);
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
            Assert.assertTrue(codigoGenetico.getArn().equals(arn));
            Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
            Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
            Assert.assertTrue(codigoGenetico.getProteina() == proteina);
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
            Assert.assertTrue(codigoGenetico.getArn().equals(arn));
            Assert.assertTrue(codigoGenetico.getValorNumerico() == valorNumerico);
            Assert.assertTrue(codigoGenetico.getCategoria() == categoria);
            Assert.assertTrue(codigoGenetico.getProteina() == proteina);
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
        String arn = arnAleatorio();
        double valorNumerico = valorNumericoAleatorio();
        int categoria = categoriaAleatoria();
        boolean proteina = proteinaAleatoria();
        codigoGenetico = new CodigoGenetico(adn, arn, valorNumerico, categoria, proteina);

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

        n = codigoGenetico.getArn();
        m = codigoGenetico.getArn().length();
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ARN, n));
        n = codigoGenetico.getArn().substring(0, m/2);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ARN, n));
        n = codigoGenetico.getArn().substring(m/2, m);
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ARN, n));
        n = codigoGenetico.getArn().substring(m/3, 2*(m/3));
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.ARN, n));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ARN, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.ARN, new Integer(4)));
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

        Boolean e = new Boolean(codigoGenetico.getProteina());
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.PROTEINA, e));
        e = new Boolean(codigoGenetico.getProteina());
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.PROTEINA, e));
        e = new Boolean(codigoGenetico.getProteina());
        Assert.assertTrue(codigoGenetico.caza(CampoCodigoGenetico.PROTEINA, e));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.PROTEINA, "XXX"));
        Assert.assertFalse(codigoGenetico.caza(CampoCodigoGenetico.PROTEINA, null));

        try {
            codigoGenetico.caza(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            codigoGenetico.caza(X.A, codigoGenetico.getAdn());
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            Object o = new Integer(codigoGenetico.getCategoria());
            codigoGenetico.caza(X.A, o);
        } catch (IllegalArgumentException iae) {}
        try {
            Object o = new Double(codigoGenetico.getValorNumerico());
            codigoGenetico.caza(X.A, o);
        } catch (IllegalArgumentException iae) {}
        try {
            Object o = new Boolean(codigoGenetico.getProteina());
            codigoGenetico.caza(X.A, o);
        } catch (IllegalArgumentException iae) {}
        try {
            Assert.assertFalse(codigoGenetico.caza(X.A, null));
        } catch (IllegalArgumentException iae) {}
    }
    /* Inicializa el generador de números aleatorios. */
    static {
        random = new Random();
    }
}
