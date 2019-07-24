package mx.unam.ciencias.icc.proyecto2;
import mx.unam.ciencias.icc.proyecto2.Lista;
import mx.unam.ciencias.icc.proyecto2.Bandera;
import java.io.IOException;

public class Proyecto2{

  public static void main(String[] args) throws IOException {
        String[] bandera = args;
        Bandera banderaProyecto = new Bandera(bandera);
        banderaProyecto.inicio();
    }
}
