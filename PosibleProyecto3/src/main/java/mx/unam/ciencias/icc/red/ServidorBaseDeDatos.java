package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para servidores de bases de datos genéricas.
 */
public abstract class ServidorBaseDeDatos<R extends Registro<R, ?>> {

    /* La base de datos. */
    private BaseDeDatos<R, ? extends Enum> bdd;
    /* El servidor de enchufes. */
    private ServerSocket servidor;
    /* El puerto. */
    private int puerto;
    /* El archivo donde cargar/guardar la base de datos. */
    private String archivo;
    /* Lista con las conexiones. */
    private Lista<ConexionServidor<R>> conexiones;
    /* Bandera de continuación. */
    private Boolean continuaEjecucion;
    /* Escuchas del servidor. */
    private Lista<EscuchaServidor> escuchas;

    /**
     * Crea un nuevo servidor usando el archivo recibido para poblar la base de
     * datos.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param archivo el archivo en el disco del cual cargar/guardar la base de
     *                datos. Puede ser <code>null</code>, en cuyo caso se usará
     *                el nombre por omisión <code>base-de-datos.bd</code>.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatos(int puerto, String archivo)
        throws IOException {
        try{
          this.puerto = puerto;
          this.archivo = (archivo!= null) ? archivo: "base-de-datos.bd";
          this.servidor = new ServerSocket(puerto);
          this.continuaEjecucion = true;
          this.bdd = this.creaBaseDeDatos();
          this.conexiones = new Lista<ConexionServidor<R>>();
          this.escuchas = new Lista<EscuchaServidor>();
          carga();
        }catch(Exception e){
          throw new IOException();
        }
    }

    /**
     * Comienza a escuchar por conexiones de clientes.
     */
    public void sirve() {
        continuaEjecucion = true;
        imprimeMensaje(String.format("Escuchando en el puerto: %d", this.puerto));
        while(continuaEjecucion){
          try{
            Socket s = this.servidor.accept();
            ConexionServidor<R> conexion = new ConexionServidor<R>(this.bdd, s);
            String hostName = s.getInetAddress().getCanonicalHostName();
            imprimeMensaje(String.format("Conexión recibida de: %s.", hostName));
            imprimeMensaje(String.format("Serial de conexión: %d", conexion.getSerial()));
            conexion.agregaEscucha( (c, e, m) -> manejaEvento(c, e, m));
            new Thread( () -> conexion.manejaMensajes()).start();
            synchronized(this.conexiones){
              this.conexiones.agregaFinal(conexion);
            }
          }catch(IOException e){
            if(continuaEjecucion){
              imprimeMensaje("Error al recibir una conexión.");
            }
          }
        }
        imprimeMensaje("Ejecución del servidor ha terminado.");
    }

    /**
     * Agrega un escucha de servidor.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaServidor escucha) {
        escuchas.agregaFinal(escucha);
    }

    /**
     * Limpia todos los escuchas del servidor.
     */
    public void limpiaEscuchas() {
        this.escuchas.limpia();
    }

    /* Carga la base de datos del disco duro. */
    private void carga() {
        try{
          imprimeMensaje("Cargando base de datos de %s.\n",archivo);
          BufferedReader in =
            new BufferedReader(
              new InputStreamReader(
                new FileInputStream(archivo)));
          this.bdd.carga(in);
          in.close();
          imprimeMensaje("Base de datos cargada exitosamente de %s.\n", archivo);
        }catch(IOException ioe){
          imprimeMensaje("Ocurrió un error al tratar de cargar %s.\n", archivo);
          imprimeMensaje("La base de datos estará inicialmente vacía.\n");
        }
    }

    /* Guarda la base de datos en el disco duro. */
    private void guarda() {
      try{
        imprimeMensaje("Guardando base de datos en %s.\n",archivo);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo)));
        this.bdd.guarda(out);
        out.close();
        imprimeMensaje("Base de datos guardada.");
      }catch(IOException ioe){
        imprimeMensaje("Ocurrió un error al guardar la base de datos.\n");
      }
    }

    /* Maneja los eventos de la conexión. */
    private void manejaEvento(Conexion<R> conexion, EventoConexion evento,
                              String mensaje) {
        ConexionServidor<R> cs = null;
        switch (evento) {
        case DESCONEXION:
            imprimeMensaje("La conexión %d solicitó desconectarse.", conexion.getSerial());
            conexion.desconecta();
            imprimeMensaje("La conexión %d ha sido desconectada.", conexion.getSerial());
            break;
        case MODIFICACION:
            this.registroAlterado(conexion, mensaje);
            break;
        case TERMINACION:
            this.detenerServicio(conexion);
            break;
        case ADVERTENCIA:
            imprimeMensaje("La conexión %d envió una advertencia: "+mensaje+".", conexion.getSerial());
            break;
        case ERROR:
            imprimeMensaje("La conexión %d solicitó desconectarse.", conexion.getSerial());
            conexion.desconecta();
            imprimeMensaje("La conexión %d ha sido desconectada.", conexion.getSerial());
            break;
        }
    }

    /* Procesa los mensajes de todos los escuchas. */
    private void imprimeMensaje(String formato, Object ... argumentos) {
        for (EscuchaServidor escucha : escuchas){
            escucha.procesaMensaje(formato, argumentos);
          }
    }

    /* Regresa la conexión como conexión servidor. */
    private ConexionServidor<R> getConexionServidor(Conexion<R> conexion) {
        @SuppressWarnings("unchecked")
        ConexionServidor<R> cs = (ConexionServidor<R>)conexion;
        return cs;
    }

    private void registroAlterado(Conexion<R> c, String m){
      for(ConexionServidor<R> conexion:conexiones){
        conexion.informa(c);
      }
      imprimeMensaje("La conexión %d modificó la base de datos: "+m +".", c.getSerial());
      guarda();
    }
    private void detenerServicio(Conexion<R> c){
      synchronized(this.continuaEjecucion){
        this.continuaEjecucion = false;
      }
      imprimeMensaje("La conexión %d solicitó desconectarse.", c.getSerial());
      synchronized(this.conexiones){
        for(ConexionServidor<R> cs: this.conexiones){
          cs.desconecta();
        }
      }
      this.conexiones.limpia();
      try{
        servidor.close();
      }catch(IOException e){}
    }
    /**
     * Crea la base de datos concreta.
     * @return la base de datos concreta.
     */
    public abstract BaseDeDatos<R, ? extends Enum> creaBaseDeDatos();

    private void desconectaConexion(Conexion<R> c){
      c.desconecta();
    }
}
