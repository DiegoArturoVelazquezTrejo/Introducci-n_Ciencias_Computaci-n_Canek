package mx.unam.ciencias.icc;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Práctica 7: Iteradores.
 */
public class Practica7 {

    /* Hace búsquedas por nombre y número de cuenta en la base de datos. */
    private static void busquedas(BaseDeDatosEstudiantes bdd) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        System.out.printf("Entra un nombre para buscar: ");
        String nombre = sc.next();

        Lista<Estudiante> r = bdd.buscaRegistros(CampoEstudiante.NOMBRE, nombre);
        if (r.esVacia()) {
            System.out.printf("\nNo se hallaron estudiantes " +
                              "con el nombre \"%s\".\n",
                              nombre);
        } else {
            System.out.printf("\nSe hallaron los siguientes " +
                              "estudiantes con el nombre \"%s\":\n\n",
                              nombre);
            for (Estudiante e : r)
                System.out.println(e + "\n");
        }

        System.out.printf("Entra una cuenta para buscar: ");
        int cuenta = 0;
        try {
            cuenta = sc.nextInt();
        } catch (InputMismatchException ime) {
            System.out.printf("Se entró una cuenta inválida. " +
                              "Se interpretará como cero.\n");
        }

        r = bdd.buscaRegistros(CampoEstudiante.CUENTA, cuenta);
        if (r.esVacia()) {
            System.out.printf("\nNo se hallaron estudiantes " +
                              "con la cuenta mayor o igual a %09d.\n",
                              cuenta);
        } else {
            System.out.printf("\nSe hallaron los siguientes estudiantes " +
                              "con la cuenta mayor o igual a %09d:\n\n",
                              cuenta);
            for (Estudiante e : r)
                System.out.println(e + "\n");
        }
    }

    /* Crea una base de datos y la llena a partir de los datos que el usuario
       escriba a través del teclado. Después la guarda en disco duro y la
       regresa. */
    private static BaseDeDatosEstudiantes escritura(String nombreArchivo) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        File archivo = new File(nombreArchivo);

        if (archivo.exists()) {
            System.out.printf("El archivo \"%s\" ya existe.\n" +
                              "Presiona Ctrl-C si no quieres reescribirlo, " +
                              "o Enter para continuar...\n", nombreArchivo);
            sc.nextLine();
        }

        System.out.println("Entra estudiantes a la base de datos.\n" +
                           "Cuando desees terminar, deja el nombre en blanco.\n");

        BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();

        do {
            String nombre;
            int cuenta = 0;
            double promedio = 0.0;
            int edad = 0;

            System.out.printf("Nombre   : ");
            nombre = sc.next();
            if (nombre.equals(""))
                break;
            try {
                System.out.printf("Cuenta   : ");
                cuenta = sc.nextInt();
                System.out.printf("Promedio : ");
                promedio = sc.nextDouble();
                System.out.printf("Edad     : ");
                edad = sc.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("\nNúmero inválido: se descartará " +
                                   "este estudiante.\n");
                continue;
            }
            Estudiante e = new Estudiante(nombre,
                                          cuenta,
                                          promedio,
                                          edad);
            bdd.agregaRegistro(e);
            System.out.println();
        } while (true);

        int n = bdd.getNumRegistros();
        if (n == 1)
            System.out.printf("\nSe agregó 1 estudiante.\n");
        else
            System.out.printf("\nSe agregaron %d estudiantes.\n", n);

        try {
            FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
            OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            BufferedWriter out = new BufferedWriter(osOut);
            bdd.guarda(out);
            out.close();
        } catch (IOException ioe) {
            System.out.printf("No pude guardar en el archivo \"%s\".\n",
                              nombreArchivo);
            System.exit(1);
        }

        System.out.printf("\nBase de datos guardada exitosamente en \"%s\".\n",
                          nombreArchivo);

        return bdd;
    }

    /* Crea una base de datos y la llena cargándola del disco duro. Después la
       regresa. */
    private static BaseDeDatosEstudiantes lectura(String nombreArchivo) {
        BaseDeDatosEstudiantes bdd = new BaseDeDatosEstudiantes();

        try {
            FileInputStream fileIn = new FileInputStream(nombreArchivo);
            InputStreamReader isIn = new InputStreamReader(fileIn);
            BufferedReader in = new BufferedReader(isIn);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            System.out.printf("No pude cargar del archivo \"%s\".\n",
                              nombreArchivo);
            System.exit(1);
        }

        System.out.printf("Base de datos cargada exitosamente de \"%s\".\n\n",
                          nombreArchivo);

        Lista<Estudiante> r = bdd.getRegistros();
        for (Estudiante e : r)
            System.out.println(e + "\n");

        return bdd;
    }

    /* Imprime en pantalla cómo debe usarse el programa y lo termina. */
    private static void uso() {
        System.out.println("Uso: java -jar practica7.jar [-g|-c] <archivo>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 2)
            uso();

        String bandera = args[0];
        String nombreArchivo = args[1];

        if (!bandera.equals("-g") && !bandera.equals("-c"))
            uso();

        BaseDeDatosEstudiantes bdd;

        if (bandera.equals("-g"))
            bdd = escritura(nombreArchivo);
        else
            bdd = lectura(nombreArchivo);

        busquedas(bdd);
    }
}
