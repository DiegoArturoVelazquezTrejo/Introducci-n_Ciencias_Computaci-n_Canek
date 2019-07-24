package mx.unam.ciencias.icc.red.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.CampoEstudiante;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.red.Mensaje;
import mx.unam.ciencias.icc.red.ServidorBaseDeDatos;
import mx.unam.ciencias.icc.red.ServidorBaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.test.TestEstudiante;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link
 * ServidorBaseDeDatosEstudiantes}.
 */
public class TestServidorBaseDeDatosEstudiantes {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);
    /** Directorio para archivos temporales. */
    @Rule public TemporaryFolder directorio = new TemporaryFolder();

    /* Clase interna para manejar una conexión de pruebas. */
    private class Cliente {

        /* El enchufe. */
        private Socket enchufe;
        /* La entrada. */
        private BufferedReader in;
        /* La salida. */
        private BufferedWriter out;

        /* Construye una nueva conexión en el puerto. */
        private Cliente(int puerto) {
            try {
                enchufe = new Socket("localhost", puerto);
                in =
                    new BufferedReader(
                        new InputStreamReader(
                            enchufe.getInputStream()));
                out =
                    new BufferedWriter(
                        new OutputStreamWriter(
                            enchufe.getOutputStream()));
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía un mensaje por la conexión. */
        private void enviaMensaje(Mensaje mensaje) {
            try {
                out.write(mensaje.toString());
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía un estudiante por la conexión. */
        public void enviaEstudiante(Estudiante estudiante) {
            try {
                estudiante.guarda(out);
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía una cadena por la conexión. */
        public void enviaCadena(String cadena) {
            try {
                out.write(cadena);
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía una nueva línea por la conexión. */
        public void enviaLinea() {
            try {
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Recibe un mensaje por la conexión. */
        public Mensaje recibeMensaje() {
            return Mensaje.getMensaje(recibeCadena());
        }

        /* Recibe una cadena por la conexión. */
        public String recibeCadena() {
            try {
                return in.readLine();
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Recibe una base de datos por la conexión. */
        public BaseDeDatosEstudiantes recibeBaseDeDatos() {
            BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();
            try {
                bdd.carga(in);
                return bdd;
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Recibe un estudiante por la conexión. */
        public Estudiante recibeEstudiante() {
            Estudiante e = new Estudiante(null, 0, 0, 0);
            try {
                e.carga(in);
                return e;
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }
    }

    /* Compara dos estudiantes. */
    private int compara(Estudiante e1, Estudiante e2) {
        if (!e1.getNombre().equals(e2.getNombre()))
            return e1.getNombre().compareTo(e2.getNombre());
        if (e1.getCuenta() != e2.getCuenta())
            return e1.getCuenta() - e2.getCuenta();
        if (e1.getPromedio() != e2.getPromedio())
            return (e1.getPromedio() < e2.getPromedio()) ? -1 : 1;
        return e1.getEdad() - e2.getEdad();
    }

    /* Valida el archivo de la base de datos. */
    private void validaArchivo(BaseDeDatosEstudiantes bdd) {
        BaseDeDatosEstudiantes bdd2 = new BaseDeDatosEstudiantes();
        try {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(archivo)));
            bdd2.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Estudiante> l1 = bdd.getRegistros();
        l1 = l1.mergeSort((e1, e2) -> compara(e1, e2));
        Lista<Estudiante> l2 = bdd2.getRegistros();
        l2 = l2.mergeSort((e1, e2) -> compara(e1, e2));
        Assert.assertTrue(l1.equals(l2));
    }

    /* Generador de números aleatorios. */
    private Random random;
    /* Servidor de base de datos. */
    private ServidorBaseDeDatosEstudiantes sbdd;
    /* El total de estudiantes. */
    private int total;
    /* Los estudiantes. */
    private Estudiante[] estudiantes;
    /* El archivo temporal de la base de datos. */
    private String archivo;
    /* El puerto. */
    private int puerto;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de estudiantes.
     */
    public TestServidorBaseDeDatosEstudiantes() {
        random = new Random();
        total = 10 + random.nextInt(100);
        puerto = obtenPuerto();
    }

    /* Obtiene el puerto. */
    private int obtenPuerto() {
        int p = -1;
        while (p < 1024) {
            try {
                p = 1024 + random.nextInt(64500);
                ServerSocket s = new ServerSocket(p);
                s.close();
            } catch (BindException be) {
                p = -1;
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }
        return p;
    }

    /**
     * Método que se ejecuta antes de cada prueba unitaria; crea el archivo de
     * la base de datos y hace servir el servidor.
     */
    @Before public void arma() {
        try {
            estudiantes = new Estudiante[total];
            BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();
            for (int i = 0; i < total; i++) {
                estudiantes[i] = TestEstudiante.estudianteAleatorio();
                bdd.agregaRegistro(estudiantes[i]);
            }
            File f = null;
            String s = String.format("test-base-de-datos-%04d.db",
                                     random.nextInt(9999));
            f = directorio.newFile(s);
            archivo = f.getAbsolutePath();
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(archivo)));
            bdd.guarda(out);
            out.close();
            sbdd = new ServidorBaseDeDatosEstudiantes(puerto, archivo);
            new Thread(() -> sbdd.sirve()).start();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Método que se ejecuta despué de cada prueba unitaria; elimina el archivo
     * de la base de datos y detiene el servidor. Esto hace un método
     * testSirveDetenerServidor innecesario.
     */
    @After public void desarma() {
        File f = new File(archivo);
        f.delete();
        Cliente c = new Cliente(puerto);
        c.enviaMensaje(Mensaje.DETENER_SERVICIO);
        Assert.assertTrue(c.recibeCadena() == null);
    }

    /* Crea una nueva conexión, enviando y recibiendo un eco para probarla de
     * inmediato. */
    private Cliente nuevoCliente() {
        Cliente c = new Cliente(puerto);
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
        return c;
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#BASE_DE_DATOS} en el
     * método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveBaseDeDatos() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosEstudiantes bdd = c.recibeBaseDeDatos();
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertTrue(l.getLongitud() == total);
        int i = 0;
        for (Estudiante e : l)
            Assert.assertTrue(e.equals(estudiantes[i++]));
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#REGISTRO_AGREGADO}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveRegistroAgregado() {
        Cliente c1 = nuevoCliente();
        Cliente c2 = nuevoCliente();

        Estudiante estudiante = new Estudiante("A", 1, 1, 1);
        c1.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
        c1.enviaEstudiante(estudiante);
        c1.enviaLinea();

        c1.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c1.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosEstudiantes bdd = c1.recibeBaseDeDatos();
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertTrue(l.contiene(estudiante));

        Assert.assertTrue(c2.recibeMensaje() == Mensaje.REGISTRO_AGREGADO);
        Estudiante t = c2.recibeEstudiante();
        Assert.assertTrue(t.equals(estudiante));
        validaArchivo(bdd);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#REGISTRO_ELIMINADO}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveRegistroEliminado() {
        Cliente c1 = nuevoCliente();
        Cliente c2 = nuevoCliente();
        Estudiante estudiante = estudiantes[random.nextInt(total)];
        c1.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
        c1.enviaEstudiante(estudiante);
        c1.enviaLinea();
        c1.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c1.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosEstudiantes bdd = c1.recibeBaseDeDatos();
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertFalse(l.contiene(estudiante));
        Assert.assertTrue(c2.recibeMensaje() == Mensaje.REGISTRO_ELIMINADO);
        Estudiante t = c2.recibeEstudiante();
        Assert.assertTrue(t.equals(estudiante));
        validaArchivo(bdd);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#REGISTRO_MODIFICADO}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveRegistroModificado() {
        Cliente c1 = nuevoCliente();
        Cliente c2 = nuevoCliente();
        Estudiante e = estudiantes[random.nextInt(total)];
        Estudiante m = new Estudiante(null, 0, 0, 0);
        m.actualiza(e);
        m.setNombre("A");
        c1.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
        c1.enviaEstudiante(e);
        c1.enviaEstudiante(m);
        c1.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c1.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosEstudiantes bdd = c1.recibeBaseDeDatos();
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertFalse(l.contiene(e));
        Assert.assertTrue(l.contiene(m));
        Assert.assertTrue(c2.recibeMensaje() == Mensaje.REGISTRO_MODIFICADO);
        Estudiante t = new Estudiante(null, 0, 0, 0);
        t = c2.recibeEstudiante();
        Assert.assertTrue(t.equals(e));
        t = c2.recibeEstudiante();
        Assert.assertTrue(t.equals(m));
        UtilRed.espera(10);
        validaArchivo(bdd);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#DESCONECTAR}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveConexionTerminada() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.DESCONECTAR);
        Assert.assertTrue(c.recibeCadena() == null);
        c = nuevoCliente();
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#ECO} en el método
     * {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveEco() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#INVALIDO} en
     * el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveMensajeInvalido() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.INVALIDO);
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatosEstudiantes#agregaEscucha}
     * al realizar conexiones.
     */
    @Test public void testAgregaEscucha() {
        UtilRed.espera(10);
        Lista<String> mensajes = new Lista<String>();
        sbdd.agregaEscucha((f, a) -> {
                String s = a.length > 0 ? String.format(f, a) : f;
                mensajes.agregaFinal(s);
            });
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.BASE_DE_DATOS);
        c.recibeMensaje();
        BaseDeDatosEstudiantes bd = c.recibeBaseDeDatos();
        c.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
        Estudiante e = UtilRed.estudianteAleatorio(total);
        c.enviaEstudiante(e);
        c.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
        c.enviaEstudiante(e);
        c.enviaMensaje(Mensaje.INVALIDO);
        c.enviaMensaje(Mensaje.DESCONECTAR);
        UtilRed.espera(10);
        Iterator<String> i = mensajes.iterator();
        Assert.assertTrue(i.hasNext());
        String m = i.next();
        Assert.assertTrue(m.equals("Conexión recibida de: localhost."));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.startsWith("Serial de conexión: "));
        m = m.replace("Serial de conexión: ", "").replace(".", "");
        int serial = Integer.parseInt(m);
        Assert.assertTrue(serial > 0);
        Assert.assertTrue(i.hasNext());
        m = i.next();
        String r = String.format("La conexión %d modificó la base de datos: " +
                                 "Registro agregado.", serial);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.startsWith("Guardando base de datos en "));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.equals("Base de datos guardada."));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("La conexión %d modificó la base de datos: " +
                          "Registro eliminado.", serial);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.startsWith("Guardando base de datos en "));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.equals("Base de datos guardada."));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("La conexión %d envió una advertencia: " +
                          "Mensaje inválido.", serial);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("La conexión %d solicitó desconectarse.", serial);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("La conexión %d ha sido desconectada.", serial);
        Assert.assertTrue(m.equals(r));
        sbdd.limpiaEscuchas();
    }

    /**
     * Prueba unitaria para {@link
     * ServidorBaseDeDatosEstudiantes#creaBaseDeDatos}.
     */
    @Test public void testCreaBaseDeDatos() {
        BaseDeDatos<Estudiante, CampoEstudiante> bdd = sbdd.creaBaseDeDatos();
        Assert.assertTrue(bdd instanceof BaseDeDatosEstudiantes);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }
}
