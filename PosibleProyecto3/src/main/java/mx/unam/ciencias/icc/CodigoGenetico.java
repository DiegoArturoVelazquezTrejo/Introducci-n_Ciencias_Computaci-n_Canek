package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Clase para representar códigos genéticos. Un código genético se compone
 * por una cadena de proteínas. Tiene un boolean para determinar si es esencial
 * el amoniacido o no. Y tiene un arreglo de cadenas de posibles combinaciones que pueden
 * conectarse al núcleo que tenga cada objeto. Finalmente tendrá una etiqueta numérica que especifica
 * sobre ese tipo de aminoacido.
 */
public class CodigoGenetico implements Registro<CodigoGenetico, CampoCodigoGenetico> {

     /*Código genético inicial del ADN. */
     private StringProperty nombre;
     /*El nombre de la cadena genética que se ingresó. */
     private StringProperty adn;
     /* Coeficiente de adaptacion de la cadena genetica. */
     private DoubleProperty valorNumerico;
     /*  Traducción del código genético */
     private StringProperty traduccion;
     /* Categoria del código genético */
     private IntegerProperty categoria;
     /* Número de cadenas del código genético. */
     private IntegerProperty numeroCadenas;


     /**
      * Define el estado inicial de un codigo genetico.
      * @param nombre una string que represta el nombre del aminoacido .
      * @param adn el adn correspondiente.
      * @param valorNumerico es el coeficiente numérico
      * @param categoria es la categoría del codigo genetico.
      * @param traduccion es la traducción del código genético.
      */
     public CodigoGenetico(String nombre,
                           String adn,
                           double valorNumerico,
                           int categoria,
                           String traduccion) {

        this.nombre        =  new SimpleStringProperty(nombre);
        this.adn           =  new SimpleStringProperty(adn);
        this.valorNumerico =  new SimpleDoubleProperty(valorNumerico);
        this.categoria     =  new SimpleIntegerProperty(categoria);
        this.traduccion    =  new SimpleStringProperty(traduccion);
        if(adn != null)
          this.numeroCadenas =  new SimpleIntegerProperty(adn.split("-").length);
        else
          this.numeroCadenas  =new SimpleIntegerProperty(0);
     }

     /**
      * Define el estado incial de un código genetico.
      * Constructor por omisión.
      * */
    public CodigoGenetico(){}
    /**
     * Regresa el nombre del código genético .
     * @return el nombre del código genético .
     */
    public String getNombre(){
      return nombre.get();
    }
    /**
     * Regresa la propiedad del nombre.
     * @return la propiedad del nombre.
     */
    public StringProperty nombreProperty(){
      return this.nombre;
    }
    /**
     * Define el nombre del código genético .
     * @param nombre el nuevo nombre del código genético .
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

  /**
   * Regresa el adn del código genético .
   * @return el adn del código genético .
   */
  public String getAdn(){
    return adn.get();
  }
  /**
   * Regresa la propiedad del adn.
   * @return la propiedad del adn.
   */
  public StringProperty adnProperty(){
    return this.adn;
  }
  /**
   * Define el adn del código genético .
   * @param adn el nuevo adn del código genético .
   */
  public void setAdn(String adn) {
      this.adn.set(adn);
      if(adn != null)
        this.numeroCadenas.set(adn.split("-").length);
  }

  /**
   * Regresa la traducción del código genético .
   * @return la traducción del código genético .
   */
  public String getTraduccion(){
    return traduccion.get();
  }
  /**
   * Regresa la propiedad de la traducción.
   * @return la propiedad de la traducción.
   */
  public StringProperty traduccionProperty(){
    return this.traduccion;
  }
  /**
   * Define la traduccion del código genético .
   * @param traduccion nueva traducción adn del código genético .
   */
  public void setTraduccion(String traduccion) {
      this.traduccion.set(traduccion);
  }

  /**
   * Regresa la categoría del código genético .
   * @return la categoría del código genético .
   */
  public int getCategoria(){
    return categoria.get();
  }
  /**
   * Regresa la propiedad de la categoría.
   * @return la propiedad de la categoría.
   */
  public IntegerProperty categoriaProperty(){
    return this.categoria;
  }
  /**
   * Define la categoría del código genético .
   * @param categoria nueva categoría adn del código genético .
   */
  public void setCategoria(int categoria) {
      this.categoria.set(categoria);
  }

  /**
   * Regresa el numero de cadenas del código genético .
   * @return el numero de cadenas del código genético .
   */
  public int getNumeroCadenas(){
    return numeroCadenas.get();
  }
  /**
   * Regresa la propiedad de la categoría.
   * @return la propiedad de la categoría.
   */
  public IntegerProperty numeroCadenasProperty(){
    return this.numeroCadenas;
  }

  /**
   * Regresa el valor numérico del código genético .
   * @return el valor numérico del código genético .
   */
  public double getValorNumerico(){
    return valorNumerico.get();
  }
  /**
   * Regresa la propiedad del valor numerico.
   * @return la propiedad del valor numerico.
   */
  public DoubleProperty valorNumericoProperty(){
    return this.valorNumerico;
  }
  /**
   * Define el valor numérico del código genético .
   * @param valorNumerico nuevo valor numérico del código genético .
   */
  public void setValorNumerico(double valorNumerico) {
      this.valorNumerico.set(valorNumerico);
  }

  /**
  * Regresa una representación en cadena del codigo genetico.
  * @return una representación en cadena del codigo genetico.
  */
  public String toString() {
       String cadena = String.format("Nombre   : %s\n" +
                       "ADN   : %s\n" +
                       "valorNumerico   : %09f\n" +
                       "Categoría  :  %d\n" +
                       "Traducción  : %s\n"+
                       "Numero de cadenas: %d\n",
               this.getNombre(), this.getAdn(), this.getValorNumerico(),this.getCategoria(),this.getTraduccion(), this.getNumeroCadenas());
       return cadena;
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
         if(this.getNombre().equals(sample.getNombre()) && this.getValorNumerico() == sample.getValorNumerico() &&
              this.getAdn().equals(sample.getAdn()) && this.getCategoria() == sample.getCategoria() &&
              this.getTraduccion() == sample.getTraduccion() && this.getNumeroCadenas() == sample.getNumeroCadenas())
            return true;
         return false;
  }

  /**
  * Guarda toda la información del código genético en la salida recibida.
  * @param out la salida dónde hay que guardar al código genético.
  * @throws IOException si un error de entrada/salida ocurre.
  */

  @Override public void guarda(BufferedWriter out) throws IOException {
        String cadena = String.format("%s\t%s\t%2.2f\t%d\t%s\t%d\n",this.getNombre(),this.getAdn(),this.getValorNumerico(),this.getCategoria(),this.getTraduccion(), this.getNumeroCadenas());
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
     if(t.length != 6){
        System.out.print(t.length);
        throw new IOException("Numero de campos inválido");
     }
     setNombre(t[0]);
     setAdn(t[1]);
     try{
       setValorNumerico(Double.parseDouble(t[2]));
       setCategoria(Integer.parseInt(t[3]));
       setTraduccion(t[4]);
     }catch(NumberFormatException nfe){
        throw new IOException("Datos inválidos");
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
      *              código genético .</li>
      *           <li><tt>campo</tt> es {@link CampoCodigoGenetico#ADN} y
      *              <tt>valor</tt> es instancia de {@link String} y su
      *              valor doble es mayor o igual al promedio del
      *              código genético .</li>
      *           <li><tt>campo</tt> es {@link CampoCodigoGenetico#VALORNUMERICO} y
      *              <tt>valor</tt> es instancia de {@link Double} y su
      *              valor entero es mayor o igual a la edad del
      *              código genético .</li>
      *         </ul>
      *         <tt>false</tt> en otro caso.
      * @throws IllegalArgumentException si el campo no es instancia de
      *         {@link CampoCodigoGenetico}.
      */
  @Override  public boolean caza(CampoCodigoGenetico campo, Object valor) {
         if (!(campo instanceof CampoCodigoGenetico))
             throw new IllegalArgumentException("El campo debe ser CampoCodigoGenetico");
         CampoCodigoGenetico c = (CampoCodigoGenetico)campo;
         switch(c){
           case NOMBRE:
             return cazaNombre(valor);
           case ADN:
             return cazaAdn(valor);
           case VALORNUMERICO:
             return cazaValorNumerico(valor);
           case CATEGORIA:
             return cazaCategoria(valor);
           case TRADUCCION:
             return cazaTraduccion(valor);
           case NUMEROCADENAS:
             return cazaNumeroCadenas(valor);
           default:
             return false;
         }
  }
     private boolean cazaAdn(Object o){
       if(!(o instanceof String))return false;
       String v = (String) o;
       if(v.isEmpty()) return false;
       return this.getAdn().indexOf(v) !=-1;
     }
     private boolean cazaNombre(Object o){
       if(!(o instanceof String)) return false;
       String v = (String) o;
       return this.getNombre().indexOf(v) !=-1;
     }
     private boolean cazaTraduccion(Object o){
       if(!(o instanceof String)) return false;
       String v = (String) o;
       return this.getTraduccion().indexOf(v) !=-1;
     }
     private boolean cazaValorNumerico(Object o){
       if(!(o instanceof Double)) return false;
       Double v = (Double) o;
       return this.getValorNumerico() >= v.doubleValue();
     }
     private boolean cazaCategoria(Object o){
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return this.getCategoria() == v.intValue();
     }
     private boolean cazaNumeroCadenas(Object o){
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return this.getNumeroCadenas() == v.intValue();
     }

  /**
  * Actualiza los valores del código genético  con los del registro recibido.
  * @param codigoGenetico el código genético con el cual actualizar los valores.
  */
 @Override public void actualiza(CodigoGenetico codigoGenetico) {
     setNombre(codigoGenetico.getNombre());
     setAdn(codigoGenetico.getAdn());
     setValorNumerico(codigoGenetico.getValorNumerico());
     setCategoria(codigoGenetico.getCategoria());
     setTraduccion(codigoGenetico.getTraduccion());
  }
}
