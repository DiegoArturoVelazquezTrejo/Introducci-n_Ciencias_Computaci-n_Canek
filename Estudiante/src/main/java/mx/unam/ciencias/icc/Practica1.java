package mx.unam.ciencias.icc;

/**
 * Práctica 1: Orientación a Objetos y sintaxis de Java.
 */
public class Practica1 {

    public static void main(String[] args) {
        int m = 0;

        Estudiante e1 =
            new Estudiante("José Arcadio Buendía", 10355684, 9.58, 48);
        int n1 = e1.getNombre().length();
        m = n1 > m ? n1 : m;

        Estudiante e2 =
            new Estudiante("Úrsula Iguarán", 2254662, 9.56, 45);
        int n2 = e2.getNombre().length();
        m = n2 > m ? n2 : m;

        Estudiante e3 =
            new Estudiante("Aureliano Buendía", 10118878, 9.41, 26);
        int n3 = e3.getNombre().length();
        m = n3 > m ? n3 : m;

        Estudiante e4 =
            new Estudiante("Rebeca Buendía", 6934954, 8.61, 44);
        int n4 = e4.getNombre().length();
        m = n4 > m ? n4 : m;

        Estudiante e5 =
            new Estudiante("Amaranta Buendía", 2274546, 9.68, 41);
        int n5 = e5.getNombre().length();
        m = n5 > m ? n5 : m;

        m += 2;

        String formato = "%" + m + "s\t%09d\t%2.2f\t%d\n";
        System.out.printf(formato, e1.getNombre(), e1.getCuenta(),
                          e1.getPromedio(), e1.getEdad());
        System.out.printf(formato, e2.getNombre(), e2.getCuenta(),
                          e2.getPromedio(), e2.getEdad());
        System.out.printf(formato, e3.getNombre(), e3.getCuenta(),
                          e3.getPromedio(), e3.getEdad());
        System.out.printf(formato, e4.getNombre(), e4.getCuenta(),
                          e4.getPromedio(), e4.getEdad());
        System.out.printf(formato, e5.getNombre(), e5.getCuenta(),
                          e5.getPromedio(), e5.getEdad());
    }
}
