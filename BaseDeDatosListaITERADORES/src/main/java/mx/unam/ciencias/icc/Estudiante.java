package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Clase para representar estudiantes. Un estudiante tiene nombre, número de
 * cuenta, promedio y edad. La clase implementa {@link Registro}, por lo que
 * puede cargarse y guardarse utilizando objetos de las clases {@link
 * BufferedReader} y {@link BufferedWriter} como entrada y salida
 * respectivamente, además de determinar si sus campos cazan valores
 * arbitrarios.
 */
public class Estudiante implements Registro<CampoEstudiante>{

     /* Nombre del estudiante. */
     private String nombre;
     /* Número de cuenta. */
     private int cuenta;
     /* Pormedio del estudiante. */
     private double promedio;
     /* Edad del estudiante.*/
     private int edad;

     /**
      * Define el estado inicial de un estudiante.
      * @param nombre el nombre del estudiante.
      * @param cuenta el número de cuenta del estudiante.
      * @param promedio el promedio del estudiante.
      * @param edad la edad del estudiante.
      */
     public Estudiante(String nombre,
                       int    cuenta,
                       double promedio,
                       int    edad) {
         this.nombre    = nombre;
         this.cuenta = cuenta;
         this.promedio = promedio;
         this.edad = edad;
     }
     public Estudiante(){}
     /**
      * Regresa el nombre del estudiante.
      * @return el nombre del estudiante.
      */
     public String getNombre() {
         return nombre;
     }

     /**
      * Define el nombre del estudiante.
      * @param nombre el nuevo nombre del estudiante.
      */
     public void setNombre(String nombre) {
         this.nombre = nombre;
     }

     /**
      * Regresa el número de cuenta del estudiante.
      * @return el número de cuenta del estudiante.
      */
     public int getCuenta() {
         return cuenta;
     }

     /**
      * Define el número cuenta del estudiante.
      * @param cuenta el nuevo número de cuenta del estudiante.
      */
     public void setCuenta(int cuenta) {
         this.cuenta = cuenta;
     }

     /**
      * Regresa el promedio del estudiante.
      * @return el promedio del estudiante.
      */
     public double getPromedio() {
         return promedio;
     }

     /**
      * Define el promedio del estudiante.
      * @param promedio el nuevo promedio del estudiante.
      */
     public void setPromedio(double promedio) {
        this.promedio = promedio;
     }

     /**
      * Regresa la edad del estudiante.
      * @return la edad del estudiante.
      */
     public int getEdad() {
         return edad;
     }

     /**
      * Define la edad del estudiante.
      * @param edad la nueva edad del estudiante.
      */
     public void setEdad(int edad) {
         this.edad = edad;
     }

     /**
      * Regresa una representación en cadena del estudiante.
      * @return una representación en cadena del estudiante.
      */
     public String toString() {
       String cadena = String.format("Nombre   : %s\n" +
                       "Cuenta   : %09d\n" +
                       "Promedio : %2.2f\n" +
                       "Edad     : %d",
               nombre, cuenta, promedio, edad);
       return cadena;
     }

     /**
      * Nos dice si el estudiante recibido es igual al que manda llamar el
      * método.
      * @param estudiante el estudiante con el cual comparar.
      * @return <tt>true</tt> si el estudiante recibido tiene las mismas
      *         propiedades que el estudiante que manda llamar al método,
      *         <tt>false</tt> en otro caso.
      */
     @Override
     public boolean equals(Object objeto) {
         if(!(objeto instanceof Estudiante)) return false;
         Estudiante estudiante = (Estudiante) objeto;
         if(estudiante == null) return false;
         if(this.nombre.equals(estudiante.nombre) && this.cuenta == estudiante.cuenta && this.promedio == estudiante.promedio && this.edad == estudiante.edad)
            return true;
         return false;
     }

    /**
     * Guarda al estudiante en la salida recibida.
     * @param out la salida dónde hay que guardar al estudiante.
     * @throws IOException si un error de entrada/salida ocurre.
     */
    @Override public void guarda(BufferedWriter out) throws IOException {
        out.write(String.format("%s\t%d\t%2.2f\t%d\n",nombre,cuenta,promedio,edad));
    }

    /**
     * Carga al estudiante de la entrada recibida.
     * @param in la entrada de dónde hay que cargar al estudiante.
     * @return <tt>true</tt> si el método carga un estudiante válido,
     *         <tt>false</tt> en otro caso.
     * @throws IOException si un error de entrada/salida ocurre, o si la entrada
     *         recibida no contiene a un estudiante.
     */
    @Override public boolean carga(BufferedReader in) throws IOException {
        String l = in.readLine();
        if(l == null) return false;
        l = l.trim();
        if(l.equals(""))return false;
        String [] t = l.split("\t");
        if(t.length != 4) throw new IOException("Numero de campos inválido");
        nombre = t[0];
        try{
          cuenta = Integer.parseInt(t[1]);
          promedio = Double.parseDouble(t[2]);
          edad = Integer.parseInt(t[3]);
        }catch(NumberFormatException nfe){
          throw new IOException("Datos inválidos");
        }
        return true;
    }

    /**
     * Nos dice si el estudiante caza el valor dado en el campo especificado.
     * @param campo el campo que hay que cazar.
     * @param valor el valor con el que debe cazar el campo del registro.
     * @return <tt>true</tt> si:
     *         <ul>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#NOMBRE} y
     *              <tt>valor</tt> es instancia de {@link String} y es una
     *              subcadena del nombre del estudiante.</li>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#CUENTA} y
     *              <tt>valor</tt> es instancia de {@link Integer} y su
     *              valor entero es mayor o igual a la cuenta del
     *              estudiante.</li>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#PROMEDIO} y
     *              <tt>valor</tt> es instancia de {@link Double} y su
     *              valor doble es mayor o igual al promedio del
     *              estudiante.</li>
     *           <li><tt>campo</tt> es {@link CampoEstudiante#EDAD} y
     *              <tt>valor</tt> es instancia de {@link Integer} y su
     *              valor entero es mayor o igual a la edad del
     *              estudiante.</li>
     *         </ul>
     *         <tt>false</tt> en otro caso.
     * @throws IllegalArgumentException si el campo no es instancia de
     *         {@link CampoEstudiante}.
     */
    @Override public boolean caza(CampoEstudiante campo, Object valor) {
        if (!(campo instanceof CampoEstudiante))
            throw new IllegalArgumentException("El campo debe ser " +
                                               "CampoEstudiante");
        CampoEstudiante c = (CampoEstudiante)campo;
        switch(c){
          case NOMBRE:
            return cazaNombre(valor);
          case CUENTA:
            return cazaCuenta(valor);
          case EDAD:
            return cazaEdad(valor);
          case PROMEDIO:
            return cazaPromedio(valor);
          default:
            return false;
        }
    }
    private boolean cazaNombre(Object o){
      if(!(o instanceof String))return false;
      String v = (String) o;
      if(v.isEmpty()) return false;
      return nombre.indexOf(v) !=-1;
    }
    private boolean cazaCuenta(Object o){
      if(!(o instanceof Integer)) return false;
      Integer v = (Integer) o;
      return cuenta>= v.intValue();
    }
    private boolean cazaEdad(Object o){
      if(!(o instanceof Integer)) return false;
      Integer v = (Integer) o;
      return edad>= v.intValue();
    }
    private boolean cazaPromedio(Object o){
      if(!(o instanceof Double)) return false;
      Double v = (Double) o;
      return promedio>= v.doubleValue();
    }
}
