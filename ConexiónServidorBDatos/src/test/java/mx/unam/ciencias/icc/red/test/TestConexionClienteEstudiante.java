package mx.unam.ciencias.icc.red.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatosEstudiantes;
import mx.unam.ciencias.icc.CampoEstudiante;
import mx.unam.ciencias.icc.Estudiante;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.red.Conexion;
import mx.unam.ciencias.icc.red.ConexionCliente;
import mx.unam.ciencias.icc.red.EventoConexion;
import mx.unam.ciencias.icc.red.Mensaje;
import mx.unam.ciencias.icc.test.TestEstudiante;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link ConexionCliente}, instanciada
 * con {@link Estudiante}.
 */
public class TestConexionClienteEstudiante {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* El total de estudiantes. */
    private int total;
    /* La base de datos. */
    private BaseDeDatosEstudiantes bdd;
    /* El puerto. */
    private int puerto;
    /* El enchufe. */
    private Socket enchufe;
    /* La entrada. */
    private BufferedReader in;
    /* La salida. */
    private BufferedWriter out;
    /* El servidor. */
    private ServerSocket servidor;
    /* La conexión. */
    private ConexionCliente<Estudiante> conexion;
    /* El último evento registrado en el escucha estándar. */
    private EventoConexion ultimoEvento;
    /* Generador de números aleatorios. */
    private Random random;

    /**
     * Crea un generador de números aleatorios, define un total aleatorio de
     * estudiantes, define un puerto aleatorio, llena la base de datos e inicia
     * el servidor.
     */
    public TestConexionClienteEstudiante() {
        random = new Random();
        total = 5 + random.nextInt(10);
        bdd = new BaseDeDatosEstudiantes();
        UtilRed.llenaBaseDeDatos(bdd, total);
        creaServidor();
        puerto = servidor.getLocalPort();
        iniciaServidor();
    }

    /* Crea el servidor. */
    private void creaServidor() {
        while (servidor == null) {
            try {
                int p = 1024 + random.nextInt(64500);
                servidor = new ServerSocket(p);
            } catch (BindException be) {
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }
    }

    /* Inicia el servidor. */
    private void iniciaServidor() {
        new Thread(() -> {
                try {
                    enchufe = servidor.accept();
                    in = new BufferedReader(
                        new InputStreamReader(
                            enchufe.getInputStream()));
                    out = new BufferedWriter(
                        new OutputStreamWriter(
                            enchufe.getOutputStream()));
                } catch (IOException ioe) {
                    Assert.fail();
                }
        }).start();
        UtilRed.espera(10);
    }

    /* Dispara el hilo de manejo de mensajes de la conexión. */
    private void dispara() {
        conexion.agregaEscucha((c, e, m) -> ultimoEvento = e);
        new Thread(() -> conexion.manejaMensajes()).start();
        UtilRed.espera(10);
    }

    /* Envía la base de datos por la salida. */
    private void enviaBaseDeDatos() {
        enviaMensaje(Mensaje.BASE_DE_DATOS);
        try {
            bdd.guarda(out);
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /* Envía una cadena por la salida. */
    private void enviaCadena(String cadena) {
        try {
            out.write(cadena);
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /* Envía un mensaje por la salida. */
    private void enviaMensaje(Mensaje mensaje) {
        try {
            out.write(mensaje.toString());
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /* Envía un estudiante por la salida. */
    private void enviaEstudiante(Estudiante estudiante) {
        try {
            estudiante.guarda(out);
            out.flush();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /* Recibe un mensaje por la entrada. */
    private Mensaje recibeMensaje() {
        try {
            return Mensaje.getMensaje(in.readLine());
        } catch (IOException ioe) {
            Assert.fail();
        }
        /* Inalcanzable. */
        return null;
    }

    /* Recibe un estudiante por la entrada. */
    public Estudiante recibeEstudiante() {
        try {
            Estudiante e = bdd.creaRegistro();
            if (e.carga(in))
                return e;
            Assert.fail();
        } catch (IOException ioe) {
            Assert.fail();
        }
        /* Inalcanzable. */
        return null;
    }

    /**
     * Prueba unitaria para {@link ConexionCliente#ConexionCliente}.
     */
    @Test public void testConstructor() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Assert.assertTrue(conexion.getMensaje() == Mensaje.BASE_DE_DATOS);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));
            Assert.assertFalse(in.ready());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * avisa automáticamente que un registro fue agregado a la base de datos.
     */
    @Test public void testEventoRegistroAgregado() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));

            Estudiante e = UtilRed.estudianteAleatorio(total);
            b.agregaRegistro(e);
            UtilRed.espera(10);
            m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_AGREGADO);
            Estudiante r = recibeEstudiante();
            Assert.assertTrue(e.equals(r));
            Assert.assertTrue(e != r);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * avisa automáticamente que un registro fue eliminado de la base de datos.
     */
    @Test public void testEventoRegistroEliminado() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            int i = random.nextInt(total);
            Estudiante e = l.get(i);

            b.eliminaRegistro(e);
            UtilRed.espera(10);
            m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_ELIMINADO);
            Estudiante r = recibeEstudiante();
            Assert.assertTrue(e.equals(r));
            Assert.assertTrue(e != r);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * avisa automáticamente que un registro fue modificado en la base de datos.
     */
    @Test public void testEventoRegistroModificado() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            Estudiante e = l.get(random.nextInt(total));
            Estudiante f = new Estudiante(e.getNombre() + " X",
                                          e.getCuenta(),
                                          e.getPromedio(),
                                          e.getEdad());

            b.modificaRegistro(e, f);
            UtilRed.espera(10);
            m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_MODIFICADO);
            Estudiante re = recibeEstudiante();
            Estudiante rf = recibeEstudiante();
            Assert.assertTrue(e.equals(re));
            Assert.assertTrue(e != re);
            Assert.assertTrue(f.equals(rf));
            Assert.assertTrue(f != rf);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de base de datos.
     */
    @Test public void testRecibeBaseDeDatos() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));

            bdd.limpia();
            bdd.agregaRegistro(new Estudiante("A", 1, 1, 1));
            bdd.agregaRegistro(new Estudiante("B", 2, 2, 2));
            bdd.agregaRegistro(new Estudiante("C", 3, 3, 3));
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));
            Assert.assertFalse(in.ready());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de registro agregado y no avisa
     * automáticamente del evento cuando lo recibe del servidor.
     */
    @Test public void testRecibeRegistroAgregado() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));

            Estudiante e = UtilRed.estudianteAleatorio(total);
            bdd.agregaRegistro(e);
            enviaMensaje(Mensaje.REGISTRO_AGREGADO);
            enviaEstudiante(e);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_AGREGADO);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));
            Assert.assertFalse(in.ready());
            Assert.assertTrue(ultimoEvento == EventoConexion.MODIFICACION);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de registro eliminado y no avisa
     * automáticamente del evento cuando lo recibe del servidor.
     */
    @Test public void testRecibeRegistroEliminado() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = Mensaje.getMensaje(in.readLine());
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));
            Estudiante e = l.get(random.nextInt(total));
            bdd.eliminaRegistro(e);
            enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
            enviaEstudiante(e);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_ELIMINADO);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));
            Assert.assertFalse(in.ready());
            Assert.assertTrue(ultimoEvento == EventoConexion.MODIFICACION);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de registro modificado y no avisa
     * automáticamente del evento cuando lo recibe del servidor.
     */
    @Test public void testRecibeRegistroModificado() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            Estudiante e, f;
            e = l.get(random.nextInt(total));
            e = new Estudiante(e.getNombre(),
                               e.getCuenta(),
                               e.getPromedio(),
                               e.getEdad());
            f = new Estudiante(e.getNombre() + " X",
                               e.getCuenta(),
                               e.getPromedio(),
                               e.getEdad());
            bdd.modificaRegistro(e, f);
            enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
            enviaEstudiante(e);
            enviaEstudiante(f);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_MODIFICADO);
            Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));
            Assert.assertFalse(in.ready());
            Assert.assertTrue(ultimoEvento == EventoConexion.MODIFICACION);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de desconexión.
     */
    @Test public void testRecibeDesconectar() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            Assert.assertTrue(conexion.activa());
            enviaMensaje(Mensaje.DESCONECTAR);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.DESCONECTAR);
            Assert.assertFalse(conexion.activa());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de detener servicio.
     */
    @Test public void testRecibeDetenerServicio() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            enviaMensaje(Mensaje.DETENER_SERVICIO);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.DETENER_SERVICIO);
            Assert.assertTrue(conexion.activa());
            Assert.assertFalse(in.ready());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento de eco.
     */
    @Test public void testRecibeEco() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            enviaMensaje(Mensaje.ECO);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.ECO);
            Assert.assertTrue(conexion.activa());
            Assert.assertFalse(in.ready());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * recibe correctamente un evento inválido.
     */
    @Test public void testRecibeInvalido() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            enviaMensaje(Mensaje.INVALIDO);
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.INVALIDO);
            Assert.assertTrue(conexion.activa());
            Assert.assertFalse(in.ready());
            Assert.assertTrue(ultimoEvento == EventoConexion.ADVERTENCIA);

            enviaCadena("UNA CADENA INVÁLIDA");
            UtilRed.espera(10);
            m = conexion.getMensaje();
            Assert.assertTrue(m == Mensaje.INVALIDO);
            Assert.assertTrue(conexion.activa());
            Assert.assertFalse(in.ready());
            Assert.assertTrue(ultimoEvento == EventoConexion.ADVERTENCIA);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link Conexion#getSerial}.
     */
    @Test public void testGetSerial() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        ConexionCliente<Estudiante> c1, c2, c3;
        Socket s;
        try {
            s = new Socket("localhost", puerto);
            c1 = new ConexionCliente<Estudiante>(b, s);
            Assert.assertTrue(c1.getSerial() == -1);
            UtilRed.espera(10);

            servidor.close();
            UtilRed.espera(10);

            servidor = new ServerSocket(puerto);
            iniciaServidor();
            s = new Socket("localhost", puerto);
            c2 = new ConexionCliente<Estudiante>(b, s);
            Assert.assertTrue(c2.getSerial() == -1);
            UtilRed.espera(10);

            servidor.close();
            UtilRed.espera(10);

            servidor = new ServerSocket(puerto);
            iniciaServidor();
            s = new Socket("localhost", puerto);
            c3 = new ConexionCliente<Estudiante>(b, s);
            Assert.assertTrue(c3.getSerial() == -1);

            Assert.assertTrue(c1.getSerial() == -1);
            Assert.assertTrue(c2.getSerial() == -1);
            Assert.assertTrue(c3.getSerial() == -1);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link Conexion#activa}.
     */
    @Test public void testActiva() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            Assert.assertTrue(conexion.activa());
            enchufe.close();
            UtilRed.espera(10);
            Assert.assertFalse(conexion.activa());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para {@link Conexion#desconecta}.
     */
    @Test public void testDesconecta() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            Assert.assertTrue(conexion.activa());
            conexion.desconecta();
            UtilRed.espera(10);
            Assert.assertFalse(conexion.activa());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * envía correctamente el evento {@link EventoConexion#DESCONEXION}.
     */
    @Test public void testAgregaEscuchaDesconexion() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            final int LLAMADO = 0;
            final int DESCONECTADO = 1;
            boolean[] f = { false, false };
            conexion.agregaEscucha((c, e, j) -> {
                    synchronized (f) {
                        f[LLAMADO] = true;
                        if (e == EventoConexion.DESCONEXION)
                            f[DESCONECTADO] = true;
                    }
                });
            Assert.assertFalse(f[LLAMADO]);
            conexion.desconecta();
            while (!f[LLAMADO])
                UtilRed.espera(10);
            Assert.assertTrue(f[DESCONECTADO]);
            Assert.assertFalse(conexion.activa());
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * envía correctamente el evento {@link EventoConexion#MODIFICACION}.
     */
    @Test public void testAgregaEscuchaModificacion() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            final int LLAMADO = 0;
            final int MODIFICADO = 1;
            boolean[] f = { false, false };
            conexion.agregaEscucha((c, e, j) -> {
                    synchronized (f) {
                        f[LLAMADO] = true;
                        if (e == EventoConexion.MODIFICACION)
                            f[MODIFICADO] = true;
                    }
                });
            b.agregaRegistro(new Estudiante("A", 1, 1, 1));
            UtilRed.espera(10);
            m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.REGISTRO_AGREGADO);
            Estudiante e = recibeEstudiante();
            Assert.assertTrue(e.equals(new Estudiante("A", 1, 1, 1)));
            Assert.assertFalse(f[LLAMADO]);
            enviaMensaje(Mensaje.REGISTRO_AGREGADO);
            enviaEstudiante(new Estudiante("B", 2, 2, 2));
            while (!f[LLAMADO])
                UtilRed.espera(10);
            Assert.assertTrue(f[MODIFICADO]);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * ignora correctamente el evento {@link EventoConexion#TERMINACION}.
     */
    @Test public void testAgregaEscuchaTerminacion() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            final int LLAMADO = 0;
            final int TERMINADO = 1;
            boolean[] f = { false, false };
            conexion.agregaEscucha((c, e, j) -> {
                    synchronized (f) {
                        f[LLAMADO] = true;
                        if (e == EventoConexion.TERMINACION)
                            f[TERMINADO] = true;
                    }
                });
            enviaMensaje(Mensaje.DETENER_SERVICIO);
            UtilRed.espera(50); // Más largo que lo normal.
            Assert.assertFalse(f[LLAMADO]);
            Assert.assertFalse(f[TERMINADO]);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * envía correctamente el evento {@link EventoConexion#ADVERTENCIA}.
     */
    @Test public void testAgregaEscuchaAdvertencia() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            final int LLAMADO = 0;
            final int ADVERTIDO = 1;
            boolean[] f = { false, false };
            conexion.agregaEscucha((c, e, j) -> {
                    synchronized (f) {
                        f[LLAMADO] = true;
                        if (e == EventoConexion.ADVERTENCIA)
                            f[ADVERTIDO] = true;
                    }
                });
            out.write("ESTA ES UNA CADENA INVÁLIDA");
            out.newLine();
            out.flush();
            while (!f[LLAMADO])
                UtilRed.espera(10);
            Assert.assertTrue(f[LLAMADO]);
            Assert.assertTrue(f[ADVERTIDO]);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionCliente}
     * envía correctamente el evento {@link EventoConexion#ERROR}.
     */
    @Test public void testAgregaEscuchaError() {
        BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
        try {
            Socket s = new Socket("localhost", puerto);
            conexion = new ConexionCliente<Estudiante>(b, s);
            UtilRed.espera(10);
            Mensaje m = recibeMensaje();
            Assert.assertTrue(m == Mensaje.BASE_DE_DATOS);
            dispara();
            enviaBaseDeDatos();
            UtilRed.espera(10);
            Lista<Estudiante> l = bdd.getRegistros();
            Assert.assertTrue(l.equals(b.getRegistros()));

            final int LLAMADO = 0;
            final int ENTERADO = 1;
            boolean[] f = { false, false };
            conexion.agregaEscucha((c, e, j) -> {
                    synchronized (f) {
                        f[LLAMADO] = true;
                        if (e == EventoConexion.ERROR)
                            f[ENTERADO] = true;
                    }
                });
            enviaMensaje(Mensaje.REGISTRO_AGREGADO);
            out.write("ESTE ES UN REGISTRO INVÁLIDO");
            out.newLine();
            out.flush();
            UtilRed.espera(10);
            while (!f[LLAMADO])
                UtilRed.espera(10);
            Assert.assertTrue(f[ENTERADO]);
        } catch (IOException ioe) {
            Assert.fail();
        }
    }
}
