package mx.unam.ciencias.icc.red.test;

import java.util.Random;
import mx.unam.ciencias.icc.red.Mensaje;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Mensaje}.
 */
public class TestMensaje {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;

    /**
     * Crea un generador de números aleatorios.
     */
    public TestMensaje() {
        random = new Random();
    }

    /**
     * Prueba unitaria para {@link Mensaje#getMensaje}.
     */
    @Test public void testGetMensaje() {
        String p = "|=MENSAJE:";
        for (Mensaje e : Mensaje.values()) {
            String s = p;
            switch (e) {
            case BASE_DE_DATOS:       s += "BASE_DE_DATOS";       break;
            case REGISTRO_AGREGADO:   s += "REGISTRO_AGREGADO";   break;
            case REGISTRO_ELIMINADO:  s += "REGISTRO_ELIMINADO";  break;
            case REGISTRO_MODIFICADO: s += "REGISTRO_MODIFICADO"; break;
            case DESCONECTAR:         s += "DESCONECTAR";         break;
            case DETENER_SERVICIO:    s += "DETENER_SERVICIO";    break;
            case ECO:                 s += "ECO";                 break;
            case INVALIDO:            s += "INVALIDO";            break;
            }
            Assert.assertTrue(e  == Mensaje.getMensaje(s));
        }
        Assert.assertTrue(Mensaje.INVALIDO == Mensaje.getMensaje(""));
    }

    /**
     * Prueba unitaria para {@link Mensaje#toString}.
     */
    @Test public void testToString() {
        String p = "|=MENSAJE:";
        for (Mensaje e : Mensaje.values()) {
            String s = e.toString();
            switch (e) {
            case BASE_DE_DATOS:
                Assert.assertTrue(s.equals(p + "BASE_DE_DATOS"));
                break;
            case REGISTRO_AGREGADO:
                Assert.assertTrue(s.equals(p + "REGISTRO_AGREGADO"));
                break;
            case REGISTRO_ELIMINADO:
                Assert.assertTrue(s.equals(p + "REGISTRO_ELIMINADO"));
                break;
            case REGISTRO_MODIFICADO:
                Assert.assertTrue(s.equals(p + "REGISTRO_MODIFICADO"));
                break;
            case DESCONECTAR:
                Assert.assertTrue(s.equals(p + "DESCONECTAR"));
                break;
            case DETENER_SERVICIO:
                Assert.assertTrue(s.equals(p + "DETENER_SERVICIO"));
                break;
            case ECO:
                Assert.assertTrue(s.equals(p + "ECO"));
                break;
            case INVALIDO:
                Assert.assertTrue(s.equals(p + "INVALIDO"));
                break;
            }
        }
    }
}
