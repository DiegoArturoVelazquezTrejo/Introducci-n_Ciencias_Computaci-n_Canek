package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.CampoCodigoGenetico;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la enumeración {@link CampoCodigoGenetico}.
 */
public class TestCampoCodigoGenetico {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /**
     * Prueba unitaria para {@link CampoCodigoGenetico#toString}.
     */
    @Test public void testToString() {
        String s = CampoCodigoGenetico.ADN.toString();
        Assert.assertTrue(s.equals("Adn"));
        s = CampoCodigoGenetico.NOMBRE.toString();
        Assert.assertTrue(s.equals("Nombre"));
        s = CampoCodigoGenetico.CATEGORIA.toString();
        Assert.assertTrue(s.equals("Categoria"));
        s = CampoCodigoGenetico.TRADUCCION.toString();
        Assert.assertTrue(s.equals("Traduccion"));
        s = CampoCodigoGenetico.VALORNUMERICO.toString();
        Assert.assertTrue(s.equals("Valor numerico"));
        s = CampoCodigoGenetico.NUMEROCADENAS.toString();
        Assert.assertTrue(s.equals("Numero Cadenas"));
    }
}
