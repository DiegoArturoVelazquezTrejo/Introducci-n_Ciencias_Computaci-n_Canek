package mx.unam.ciencias.icc.proyecto2;
import java.text.Collator;
public class Oracion implements Comparable<Oracion>{

  String oracion;
  /**
  * Constructor de la clase.
  * @param oracion: oracion
  */
  public Oracion(String oracion){
    this.oracion = oracion;
  }
  @Override public String toString(){
    return oracion;
  }
  /**
  * Criterio de comparacion entre dos oraciones.
  * @param oracion: nombre del archivo en el cual se guardará.
  */
  @Override public int compareTo(Oracion c){
      Collator collator = Collator.getInstance();
      collator.setStrength(Collator.PRIMARY);
      return collator.compare(oracion.replaceAll("\\P{L}+",""), c.toString().replaceAll("\\P{L}+",""));
  }
}
