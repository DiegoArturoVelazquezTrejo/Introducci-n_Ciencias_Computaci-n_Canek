package main.java.mx.unam.ciencias.icc;
import main.java.mx.unam.ciencias.icc.Registro;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase para representar códigos genéticos. Un código genético se compone
 * por una cadena de proteínas. Tiene un boolean para determinar si es esencial
 * el amoniacido o no. Y tiene un arreglo de cadenas de posibles combinaciones que pueden
 * conectarse al núcleo que tenga cada objeto. Finalmente tendrá una etiqueta numérica que especifica
 * sobre ese tipo de aminoacido.
 */
public class CodigoGenetico extends Object implements Registro {

     /*Código genético inicial del ADN. */
     private String adn;
     /*El arn de la cadena principal. */
     private String arn;
     /* Coeficiente de adaptacion de la cadena genetica. */
     private double valorNumerico;
     /* Posibles permitaciones que se pueden hacer con otros codigos genéticos.*/
     private String[] permutaciones;
     /* Categoria del código genético */
     private int categoria;
     /* Generador de números aleatorios. */
     private static Random random;
     /* Si contiene una cadena de proteína*/
     private static boolean proteina;

     /**
      * Define el estado inicial de un codigo genetico.
      * @param adn una string que represente al aminoacido .
      * @param arn el arn correspondiente.
      * @param valorNumerico es el coeficiente numérico
      */
     public CodigoGenetico(String adn,
                           String arn,
                           double valorNumerico,
                           int categoria,
                           boolean proteina) {
         this.adn = adn;
         this.arn = arn;
         this.valorNumerico = valorNumerico;
         this.categoria = categoria;
         this.proteina = proteina;
         construirPermutaciones();
     }

     /**
      * Define el estado incial de un código genetico.
      * Constructor por omisión.
      * */
     public CodigoGenetico(){}
     /**
      * Regresa la estructura de la cadena genética principal.
      * @return la cadena genetica principal.
      */

    static {
          random = new Random();
      }

     public String getAdn() {
         return adn;
     }

     /**
      * Define la cadena principal.
      * @param adn la nueva cadena genetica.
      */
     public void setAdn(String adn) {
         this.adn = adn;
         construirPermutaciones();
     }

     public double getValorNumerico() {
         return valorNumerico;
     }

     /**
      * Define el número valorNumerico del estudiante.
      * @param valorNumerico el nuevo número de valorNumerico del estudiante.
      */
     public void setValorNumerico(double valorNumerico) {
         this.valorNumerico = valorNumerico;
     }

     /**
      * Regresa el arn que se genera de la cadena principal.
      * @return el string del arn que se genera de la cadena principal.
      */
     public String getArn() {
         return arn;
     }

     /**
      * Define el arn que se genera de la cadena principal.
      * @param arn el nuevo arn que se genera de la cadena principal.
      */
     public void setArn(String arn) {
        this.arn = arn;
     }

     /**
      * Regresa las posibles permutaciones con otro material genético que se pueden generar a partir de la cadena principal.
      * @return el arreglo de posibles permitaciones genéticas que se pueden generar a partir de la cadena principal.
      */
     public String[] getPermutaciones() {
         return permutaciones;
     }

     /**
      * Define las permutaciones que se  generan a partir de la cadena principal.
      */
     public void setPermutaciones(String[] permutaciones) {
        this.permutaciones = permutaciones;
     }

    /**
     * Define la categoria del adn que se genera de la cadena principal.
     * @param categoria la nueva categoria del adn que se genera de la cadena principal.
     */
     public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    /**
     * Regresa la categoria del código genético que se pueden generar a partir de la cadena principal.
     * @return la categoría del adn pueden generar a partir de la cadena principal.
     */
     public int getCategoria(){return categoria; }

    /**
     * Define si el código genético tiene proteína en su adn principal.
     * @param proteina el nuevo valor booleano si es que llega a tenerla.
     */
    public void setProteina(boolean proteina) {
        this.proteina = proteina;
    }

    /**
     * Regresa el booleano indicando si el código genético tiene proteína.
     * @return booleano indicando si el adn tiene una proteina en su cadena principal.
     */
    public boolean getProteina(){return proteina; }

     /**
      * Regresa una representación en cadena del codigo genetico.
      * @return una representación en cadena del codigo genetico.
      */
     public String toString() {
       String cadena = String.format("ADN   : %s\n" +
                       "ARN   : %s\n" +
                       "valorNumerico   : %09f\n" +
                       "Categoría  :  %d\n" +
                       "Con proteina  : %b\n",
               adn, arn, valorNumerico,categoria,proteina);

       for(int i = 0; i < permutaciones.length; i++){
          cadena += String.format("P%d     : %s\n",i+1, permutaciones[i]);
       }
       return cadena;
     }

     private void construirPermutaciones(){
        if(adn != null){
          String[] partes = adn.split("-");
          permutaciones = new String[partes.length * 2];
          int alelo1, alelo2, alelo3;
          for(int i = 0; i < permutaciones.length; i++){
            alelo1 = random.nextInt(partes.length-1);
            alelo2 = random.nextInt(partes.length-1);
            alelo3 = random.nextInt(partes.length-1);
            permutaciones[i] = partes[alelo1] + "-" + partes[alelo2] + "-" + partes[alelo3];
          }
        }
     }
     /**
      * Nos dice si el codigo genetico recibido es igual al que manda llamar el
      * método.
      * @param objeto: CodigoGenetico con el cual comparar.
      * @return <tt>true</tt> si el codigo genetico recibido tiene las mismas
      *         propiedades que el codigo genetico que manda llamar al método,
      *         <tt>false</tt> en otro caso.
      */
     @Override
     public boolean equals(Object objeto) {
         if(!(objeto instanceof CodigoGenetico)) return false;
         CodigoGenetico sample = (CodigoGenetico) objeto;
         if(sample == null) return false;
         if(this.adn.equals(sample.adn) && this.valorNumerico == sample.valorNumerico && this.arn.equals(sample.arn) && this.categoria == sample.categoria && this.proteina == sample.proteina){
            for(int i = 0; i < permutaciones.length; i++){
              if(!permutaciones[i].equals(sample.permutaciones[i]))
                return false;
            }
            return true;
         }
         return false;
     }

     /**
      * Guarda toda la información del código genético en la salida recibida.
      * @param out la salida dónde hay que guardar al código genético.
      * @throws IOException si un error de entrada/salida ocurre.
      */
     @Override public void guarda(BufferedWriter out) throws IOException {
        String cadena = String.format("%s\t%s\t%2.2f\t%d\t%b\t",adn,arn,valorNumerico,categoria,proteina);
        for(int i = 0; i < permutaciones.length-1; i++){
          cadena = cadena + permutaciones[i]+"\t";
        }
        cadena = cadena + permutaciones[permutaciones.length - 1] + "\n";
        out.write(cadena);
     }
     /*
      * Carga al código genético de la entrada recibida.
      * @param in la entrada de dónde hay que cargar al código genético.
      * @return <tt>true</tt> si el método carga un código genético fue válido,
      *         <tt>false</tt> en otro caso.
      * @throws IOException si un error de entrada/salida ocurre, o si la entrada
      *         recibida no contiene la informacion basica de un código genético.
      */
     @Override public boolean carga(BufferedReader in) throws IOException {
         String l = in.readLine();
         if(l == null) return false;
         l = l.trim();
         if(l.equals(""))return false;
         String [] t = l.split("\t");
         if(t.length < 5) throw new IOException("Numero de campos inválido");
         adn = t[0];
         arn = t[1];
         try{
           valorNumerico = Double.parseDouble(t[2]);
           categoria = Integer.parseInt(t[3]);
           proteina = Boolean.parseBoolean(t[4]);
         }catch(NumberFormatException nfe){
           throw new IOException("Datos inválidos");
         }
         int a = 0;
         permutaciones = new String[t.length-5];
         for(int i = 5; i < t.length; i++){
           permutaciones[a] = t[i];
           a+=1;
         }
         return true;
     }


    /**
      * Nos dice si el código caza el valor dado en el campo especificado.
      * @param campo el campo que hay que cazar.
      * @param valor el valor con el que debe cazar el campo del registro.
      * @return <tt>true</tt> si:
      *         <ul>
      *           <li><tt>campo</tt> es {@link CampoCodigoGenetico#ADN} y
      *              <tt>valor</tt> es instancia de {@link String} y es una
      *              subcadena del adn del codigo.</li>
      *           <li><tt>campo</tt> es {@link CampoCodigoGenetico#ADN} y
      *              <tt>valor</tt> es instancia de {@link String} y su
      *              valor entero es mayor o igual a la cuenta del
      *              estudiante.</li>
      *           <li><tt>campo</tt> es {@link CampoCodigoGenetico#ARN} y
      *              <tt>valor</tt> es instancia de {@link String} y su
      *              valor doble es mayor o igual al promedio del
      *              estudiante.</li>
      *           <li><tt>campo</tt> es {@link CampoCodigoGenetico#VALORNUMERICO} y
      *              <tt>valor</tt> es instancia de {@link Double} y su
      *              valor entero es mayor o igual a la edad del
      *              estudiante.</li>
      *         </ul>
      *         <tt>false</tt> en otro caso.
      * @throws IllegalArgumentException si el campo no es instancia de
      *         {@link CampoCodigoGenetico}.
      */
      @Override  public boolean caza(Enum campo, Object valor) {
         if (!(campo instanceof CampoCodigoGenetico))
             throw new IllegalArgumentException("El campo debe ser " +
                                                "CampoCodigoGenetico");
         CampoCodigoGenetico c = (CampoCodigoGenetico)campo;
         switch(c){
           case ADN:
             return cazaAdn(valor);
           case ARN:
             return cazaArn(valor);
           case VALORNUMERICO:
             return cazaValorNumerico(valor);
           case CATEGORIA:
             return cazaCategoria(valor);
           case PROTEINA:
             return cazaProteina(valor);
           default:
             return false;
         }
     }
     private boolean cazaAdn(Object o){
       if(!(o instanceof String))return false;
       String v = (String) o;
       if(v.isEmpty()) return false;
       return adn.indexOf(v) !=-1;
     }
     private boolean cazaArn(Object o){
       if(!(o instanceof String)) return false;
       String v = (String) o;
       return arn.indexOf(v) !=-1;
     }
     private boolean cazaValorNumerico(Object o){
       if(!(o instanceof Double)) return false;
       Double v = (Double) o;
       return valorNumerico>= v.doubleValue();
     }
     private boolean cazaCategoria(Object o){
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return categoria== v.intValue();
     }
     private boolean cazaProteina(Object o){
        if(!(o instanceof Boolean)) return false;
        Boolean v = (Boolean) o;
        return proteina == v.booleanValue();
     }

}
