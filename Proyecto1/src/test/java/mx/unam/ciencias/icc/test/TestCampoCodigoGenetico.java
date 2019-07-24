package test.java.mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import main.java.mx.unam.ciencias.icc.CampoCodigoGenetico;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la enumeración {@link CampoEstudiante}.
 */
public class TestCampoCodigoGenetico {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /**
     * Prueba unitaria para {@link CampoEstudiante#toString}.
     */
    @Test public void testToString() {
        String s = CampoCodigoGenetico.ADN.toString();
        Assert.assertTrue(s.equals("Adn"));
        s = CampoCodigoGenetico.ARN.toString();
        Assert.assertTrue(s.equals("Arn"));
        s = CampoCodigoGenetico.CATEGORIA.toString();
        Assert.assertTrue(s.equals("Categoria"));
        s = CampoCodigoGenetico.PROTEINA.toString();
        Assert.assertTrue(s.equals("Proteina"));
        s = CampoCodigoGenetico.VALORNUMERICO.toString();
        Assert.assertTrue(s.equals("Valor numerico"));
        s = CampoCodigoGenetico.PERMUTACIONES.toString();
        Assert.assertTrue(s.equals("Permutaciones"));
    }
}
