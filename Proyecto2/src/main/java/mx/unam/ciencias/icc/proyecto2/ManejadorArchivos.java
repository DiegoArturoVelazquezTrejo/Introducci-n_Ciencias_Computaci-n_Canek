package mx.unam.ciencias.icc.proyecto2;
import mx.unam.ciencias.icc.proyecto2.Oracion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import mx.unam.ciencias.icc.proyecto2.Lista;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;



public class ManejadorArchivos{

    private ManejadorArchivos() {}
      /**
      * guarda es un método que guardará las cadenas de la lista en el archivo.
      * @param nombreArchivo: nombre del archivo en el cual se guardará.
      * @param lista: la lista donde se sacarán las líneas de texto.
      */
    public static <T> void guarda(String nombreArchivo, Lista<T> lista) throws IOException {
      FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
      OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
      BufferedWriter out = new BufferedWriter(osOut);
      for(T s : lista){
        out.write(s.toString() + "\n");
      }
      out.close();
    }
    /**
    * CargarArchivo es un método que guardará las líneas de texto en una lista específica.
    * @param nombreArchivo: nombre del archivo del cual se leerá.
    * @param lista: la lista donde se guardarán las líneas de texto.
    */
    public static void carga(String nombreArchivo, Lista<Oracion> lista) throws IOException {
        FileInputStream fileIn = new FileInputStream(nombreArchivo);
        InputStreamReader isIn = new InputStreamReader(fileIn);
        BufferedReader in = new BufferedReader(isIn);
        String l = in.readLine();
        while(l != null){
          lista.agregaFinal(new Oracion(l));
          l = in.readLine();
        }
   }

   /**
   * Imprimirá todos los elementos de una lista en consola.
   * @param lista: la lista que imprimirá.
   */
   public static <T> void imprimeConsola(Lista<T> lista){
     for(T r : lista){
       System.out.print(r.toString() + "\n");
     }
   }

}
