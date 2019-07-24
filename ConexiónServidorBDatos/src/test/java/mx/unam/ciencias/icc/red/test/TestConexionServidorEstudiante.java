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
import mx.unam.ciencias.icc.red.ConexionServidor;
import mx.unam.ciencias.icc.red.EventoConexion;
import mx.unam.ciencias.icc.red.Mensaje;
import mx.unam.ciencias.icc.test.TestEstudiante;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link ConexionServidor}, instanciada
 * con {@link Estudiante}.
 */
public class TestConexionServidorEstudiante {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Clase auxiliar para los clientes. */
    private class Cliente {

        /* El enchufe. */
        private Socket enchufe;
        /* La entrada. */
        private BufferedReader in;
        /* La salida. */
        private BufferedWriter out;

        /* Se conecta al servidor y saca la entrada y salida. */
        private Cliente() {
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

        /* Envía un estudiante para agregar/eliminar. */
        private Estudiante enviaEstudianteAlterado(Mensaje mensaje) {
            Estudiante e = null;
            if (mensaje == Mensaje.REGISTRO_AGREGADO) {
                e = UtilRed.estudianteAleatorio(total);
            } else if (mensaje == Mensaje.REGISTRO_ELIMINADO) {
                Estudiante f = bddEstudianteAleatorio();
                e = bdd.creaRegistro();
                e.actualiza(f);
            } else {
                Assert.fail();
            }
            enviaMensaje(mensaje);
            enviaEstudiante(e);
            UtilRed.espera(10);
            return e;
        }

        /* Envía un estudiante para modificar. */
        private Estudiante[] enviaEstudianteModificado() {
            Estudiante e, f;
            f = bddEstudianteAleatorio();
            e = bdd.creaRegistro();
            e.actualiza(f);
            f = bdd.creaRegistro();
            f.setNombre(e.getNombre() + " X");
            enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
            enviaEstudiante(e);
            enviaEstudiante(f);
            UtilRed.espera(10);
            return new Estudiante[] { e, f };
        }

        /* Envía una cadena. */
        private void enviaCadena(String string) {
            try {
                out.write(string);
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía un mensaje. */
        private void enviaMensaje(Mensaje mensaje) {
            enviaCadena(mensaje.toString());
        }

        /* Envía un estudiante. */
        private void enviaEstudiante(Estudiante estudiante) {
            try {
                estudiante.guarda(out);
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Recibe una base de datos. */
        private BaseDeDatosEstudiantes recibeBaseDeDatos() {
            BaseDeDatosEstudiantes b = new BaseDeDatosEstudiantes();
            try {
                b.carga(in);
                return b;
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Recibe un estudiante. */
        private Estudiante recibeEstudiante() {
            Estudiante e = bdd.creaRegistro();
            try {
                if (!e.carga(in))
                    Assert.fail();
                return e;
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Recibe un mensaje. */
        private Mensaje recibeMensaje() {
            try {
                String linea = in.readLine();
                if (linea == null)
                    return null;
                return Mensaje.getMensaje(linea);
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Nos dice si la conexión está lista para recibir entrada. */
        private boolean listo() {
            try {
                return in.ready();
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return false;
        }
    }

    /* El total de estudiantes. */
    private int total;
    /* La base de datos. */
    private BaseDeDatosEstudiantes bdd;
    /* El puerto. */
    private int puerto;
    /* El servidor. */
    private ServerSocket servidor;
    /* Cola de conexiones de servidor. */
    private Lista<ConexionServidor<Estudiante>> servidores;
    /* El último evento recibido. */
    private EventoConexion ultimoEvento;
    /* Generador de números aleatorios. */
    private Random random;

    /**
     * Crea un generador de números aleatorios, define un total aleatorio de
     * estudiantes, define un puerto aleatorio, llena la base de datos e inicia
     * el servidor.
     */
    public TestConexionServidorEstudiante() {
        random = new Random();
        total = 5 + random.nextInt(10);
        servidores = new Lista<ConexionServidor<Estudiante>>();
        bdd = new BaseDeDatosEstudiantes();
        UtilRed.llenaBaseDeDatos(bdd, total);
        creaServidor();
        puerto = servidor.getLocalPort();
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

    /* Acepta una nueva conexión. */
    private void aceptaConexion() {
        new Thread(() -> {
                try {
                    Socket e = servidor.accept();
                    ConexionServidor<Estudiante> s =
                        new ConexionServidor<Estudiante>(bdd, e);
                    new Thread(() -> s.manejaMensajes()).start();
                    synchronized (servidores) {
                        servidores.agregaFinal(s);
                    }
                } catch (IOException ioe) {
                    Assert.fail();
                }
        }).start();
        UtilRed.espera(10);
    }

    /* Obtiene una conexión. */
    private ConexionServidor<Estudiante> getConexion() {
        ConexionServidor<Estudiante> s = null;
        boolean listo = false;
        while (!listo) {
            synchronized (servidores) {
                if (!servidores.esVacia()) {
                    s = servidores.eliminaUltimo();
                    s.agregaEscucha((c, e, m) -> ultimoEvento = e);
                    listo = true;
                }
            }
            UtilRed.espera(10);
        }
        return s;
    }

    /* Obtiene un estudiante aleatorio de la base de datos. */
    private Estudiante bddEstudianteAleatorio() {
        int n = random.nextInt(bdd.getNumRegistros());
        return bdd.getRegistros().get(n);
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de base de datos.
     */
    @Test public void testRecibeBaseDeDatos() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        c.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(s.getMensaje() == Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosEstudiantes b = c.recibeBaseDeDatos();
        Assert.assertTrue(bdd.getRegistros().equals(b.getRegistros()));
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de registro agregado.
     */
    @Test public void testRecibeRegistroAgregado() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();

        Estudiante e = c.enviaEstudianteAlterado(Mensaje.REGISTRO_AGREGADO);
        Assert.assertTrue(s.getMensaje() == Mensaje.REGISTRO_AGREGADO);
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertTrue(l.contiene(e));
        Assert.assertTrue(ultimoEvento == EventoConexion.MODIFICACION);
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de registro eliminado.
     */
    @Test public void testRecibeRegistroEliminado() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Estudiante e = c.enviaEstudianteAlterado(Mensaje.REGISTRO_ELIMINADO);
        Assert.assertTrue(s.getMensaje() == Mensaje.REGISTRO_ELIMINADO);
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertFalse(l.contiene(e));
        Assert.assertTrue(ultimoEvento == EventoConexion.MODIFICACION);
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de registro modificado.
     */
    @Test public void testRecibeRegistroModificado() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Estudiante[] es = c.enviaEstudianteModificado();
        Assert.assertTrue(s.getMensaje() == Mensaje.REGISTRO_MODIFICADO);
        Lista<Estudiante> l = bdd.getRegistros();
        Assert.assertFalse(l.contiene(es[0]));
        Assert.assertTrue(l.contiene(es[1]));
        Assert.assertTrue(ultimoEvento == EventoConexion.MODIFICACION);
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de desconexión.
     */
    @Test public void testRecibeDesconectar() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        c.enviaMensaje(Mensaje.DESCONECTAR);
        Assert.assertTrue(s.getMensaje() == Mensaje.DESCONECTAR);
        Assert.assertFalse(s.activa());
        Assert.assertTrue(ultimoEvento == EventoConexion.DESCONEXION);
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de detener servicio.
     */
    @Test public void testRecibeDetenerServicio() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        c.enviaMensaje(Mensaje.DETENER_SERVICIO);
        Assert.assertTrue(s.getMensaje() == Mensaje.DETENER_SERVICIO);
        Assert.assertFalse(c.listo());
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento de eco.
     */
    @Test public void testRecibeEco() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(s.getMensaje() == Mensaje.ECO);
        Assert.assertTrue(s.activa());
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * recibe correctamente un evento inválido.
     */
    @Test public void testRecibeInvalido() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();

        c.enviaMensaje(Mensaje.INVALIDO);
        Assert.assertTrue(s.getMensaje() == Mensaje.INVALIDO);
        Assert.assertTrue(s.activa());

        c.enviaCadena("UNA CADENA INVÁLIDA");
        Assert.assertTrue(s.getMensaje() == Mensaje.INVALIDO);
        Assert.assertTrue(s.activa());
    }

    /**
     * Prueba unitaria para {@link Conexion#getSerial}.
     */
    @Test public void testGetSerial() {
        aceptaConexion();
        Cliente c1 = new Cliente();
        ConexionServidor<Estudiante> s1 = getConexion();
        Assert.assertTrue(s1.activa());
        aceptaConexion();
        Cliente c2 = new Cliente();
        ConexionServidor<Estudiante> s2 = getConexion();
        Assert.assertTrue(s2.activa());
        Assert.assertTrue(s1.getSerial() < s2.getSerial());
    }

    /**
     * Prueba unitaria para {@link Conexion#activa}.
     */
    @Test public void testActiva() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());
        s.desconecta();
        Assert.assertFalse(s.activa());
    }

    /**
     * Prueba unitaria para {@link Conexion#desconecta}.
     */
    @Test public void testDesconecta() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());
        s.desconecta();
        Mensaje m = c.recibeMensaje();
        Assert.assertTrue(m == null);
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * envía correctamente el evento {@link EventoConexion#DESCONEXION}.
     */
    @Test public void testAgregaEscuchaDesconexion() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());

        final int LLAMADO = 0;
        final int DESCONECTADO = 1;
        boolean[] f = { false, false };
        s.agregaEscucha((k, e, m) -> {
                f[LLAMADO] = true;
                if (e == EventoConexion.DESCONEXION)
                    f[DESCONECTADO] = true;
            });
        c.enviaMensaje(Mensaje.DESCONECTAR);
        Assert.assertTrue(f[LLAMADO]);
        Assert.assertTrue(f[DESCONECTADO]);
        Assert.assertFalse(s.activa());
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * envía correctamente el evento {@link EventoConexion#MODIFICACION}.
     */
    @Test public void testAgregaEscuchaModificacion() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());

        final int LLAMADO = 0;
        final int MODIFICADO = 1;
        boolean[] f = { false, false };
        s.agregaEscucha((k, e, m) -> {
                f[LLAMADO] = true;
                if (e == EventoConexion.MODIFICACION)
                    f[MODIFICADO] = true;
            });
        Lista<Estudiante> l = bdd.getRegistros();
        Estudiante e = c.enviaEstudianteAlterado(Mensaje.REGISTRO_AGREGADO);
        Assert.assertFalse(l.contiene(e));
        Assert.assertTrue(f[LLAMADO]);
        Assert.assertTrue(f[MODIFICADO]);
        Assert.assertTrue(bdd.getRegistros().contiene(e));

        f[LLAMADO] = f[MODIFICADO] = true;
        l = bdd.getRegistros();
        e = c.enviaEstudianteAlterado(Mensaje.REGISTRO_ELIMINADO);
        Assert.assertTrue(l.contiene(e));
        Assert.assertTrue(f[LLAMADO]);
        Assert.assertTrue(f[MODIFICADO]);
        Assert.assertFalse(bdd.getRegistros().contiene(e));
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * envía correctamente el evento {@link EventoConexion#TERMINACION}.
     */
    @Test public void testAgregaEscuchaTerminacion() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());

        final int LLAMADO = 0;
        final int TERMINADO = 1;
        boolean[] f = { false, false };
        s.agregaEscucha((k, e, m) -> {
                f[LLAMADO] = true;
                if (e == EventoConexion.TERMINACION)
                    f[TERMINADO] = true;
            });
        c.enviaMensaje(Mensaje.DETENER_SERVICIO);
        Assert.assertTrue(f[LLAMADO]);
        Assert.assertTrue(f[TERMINADO]);
        Assert.assertFalse(c.listo());
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * envía correctamente el evento {@link EventoConexion#ADVERTENCIA}.
     */
    @Test public void testAgregaEscuchaAdvertencia() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());

        final int LLAMADO = 0;
        final int ADVERTIDO = 1;
        boolean[] f = { false, false };
        s.agregaEscucha((k, e, m) -> {
                f[LLAMADO] = true;
                if (e == EventoConexion.ADVERTENCIA)
                    f[ADVERTIDO] = true;
            });
        c.enviaMensaje(Mensaje.INVALIDO);
        Assert.assertTrue(f[LLAMADO]);
        Assert.assertTrue(f[ADVERTIDO]);
        Assert.assertTrue(s.activa());
    }

    /**
     * Prueba unitaria para probar que una instancia de {@link ConexionServidor}
     * envía correctamente el evento {@link EventoConexion#ERROR}.
     */
    @Test public void testAgregaEscuchaError() {
        aceptaConexion();
        Cliente c = new Cliente();
        ConexionServidor<Estudiante> s = getConexion();
        Assert.assertTrue(s.activa());

        final int LLAMADO = 0;
        final int ENTERADO = 1;
        boolean[] f = { false, false };
        s.agregaEscucha((k, e, m) -> {
                f[LLAMADO] = true;
                if (e == EventoConexion.ERROR)
                    f[ENTERADO] = true;
            });
        c.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
        c.enviaCadena("ESTE ES UN REGISTRO INVÁLIDO");
        Assert.assertTrue(f[LLAMADO]);
        Assert.assertTrue(f[ENTERADO]);
    }
}
