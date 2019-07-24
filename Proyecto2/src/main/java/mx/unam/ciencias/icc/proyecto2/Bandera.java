package mx.unam.ciencias.icc.proyecto2;
import mx.unam.ciencias.icc.proyecto2.Oracion;
import mx.unam.ciencias.icc.proyecto2.ManejadorArchivos;
import mx.unam.ciencias.icc.proyecto2.Lista;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Bandera{

  String[] bandera;
  /*
  * Constructor de la clase, que inicializará al arreglo bandera
  * que será el que contenga todos los elementos de los argumentos del programa inicial
  */
  public Bandera(String[] bandera){
    this.bandera = bandera;
  }

  /* Método Inicio
  * Dependiendo de los elementos de los elementos en el arreglo bandera, corroborará si se tiene
  * que ejecutar el programa por la entrada estándar o si se tiene que leer uno o más archivos.
  */

  public void inicio() throws IOException{
    if(bandera.length == 0){
      entradaEstandar  ();
      return;
    }
    boolean banderaReversa = false;
    boolean banderaDestructiva = false;
    String identificador = ".txt";
    String archivoDestructivo = null;
    Lista<Oracion> lista = new Lista<>();
    for(int i = 0; i < bandera.length; i++){
      if(bandera[i].contains(identificador))
          ManejadorArchivos.carga(bandera[i], lista);
      if(bandera[i].equals("-r"))
          banderaReversa = true;
      if(bandera[i].equals("-o")){
          banderaDestructiva = true;
        try{
          archivoDestructivo = bandera[i + 1];
          i = i + 1;
        }catch(Exception ex){
          System.out.print("No ingresaste archivo para guardar\n");
          banderaDestructiva = false;
        }
      }
    }
    realizaOperaciones(lista, banderaReversa, banderaDestructiva, archivoDestructivo);
  }
  /*
  * realizarOperaciones se mandará a llamar únicamente si se encuentra la bandera de bandera de reversa
  * o la bandera autodestructiva. Dependiendo cuales sean las que estén es la forma en la que se ejecutará este método.
  */
  public void realizaOperaciones(Lista<Oracion> lista, boolean banderaReversa, boolean banderaDestructiva, String archivoDestructivo)throws IOException{
    if(banderaReversa && banderaDestructiva){
      Lista<Oracion> listaM = lista.mergeSort(lista).reversa();
      ManejadorArchivos.imprimeConsola(listaM);
      ManejadorArchivos.guarda(archivoDestructivo, listaM);
    }
    else if(banderaReversa){
      Lista<Oracion> listaM = lista.mergeSort(lista).reversa();
      ManejadorArchivos.imprimeConsola(listaM);
    }
    else if(banderaDestructiva){
      Lista<Oracion> listaM = lista.mergeSort(lista);
      ManejadorArchivos.guarda(archivoDestructivo, listaM);
    }
    else{
      ManejadorArchivos.imprimeConsola(lista.mergeSort(lista));
    }
  }
  /*
  * entradaEstandar se mandará a llamar cuando el usuario lo especifique desde el inicio de
  * la ejecución del programa. Y leerá todo aquello que le ingrese el usuario por la línea de comandos
  * para después hacer la ordenación. 
  */
  public void entradaEstandar() throws IOException{
      Lista<Oracion> lista = new Lista<>();
      try(BufferedReader br = new BufferedReader(new InputStreamReader((System.in), "UTF-8"))){
        String linea = br.readLine();
        while(linea != null){
          lista.agregaFinal(new Oracion(linea));
          linea = br.readLine();
        }
      }
      catch(IOException e){
        System.out.print("Sucedió un error en la entrada estandar");
        System.exit(1);
      }
      System.out.print("\n");
      ManejadorArchivos.imprimeConsola(lista.mergeSort(lista));
  }

}
