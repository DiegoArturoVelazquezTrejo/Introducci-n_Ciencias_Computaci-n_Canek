package main.java.mx.unam.ciencias.icc;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;


public class Proyecto1 {

    /* Hace búsquedas por adn y por valorNumerico en la base de datos. */
    private static void busquedas(BaseDeDatosCodigoGenetico bdd) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        System.out.printf("Entra un codigo genetico para buscar: ");
        String adn = sc.next();

        Lista r = bdd.buscaRegistros(CampoCodigoGenetico.ADN, adn);
        if (r.esVacia()) {
            System.out.printf("\nNo se hallaron pruebas geneticas " +
                              "con el adn \"%s\".\n",
                              adn);
        } else {
            System.out.printf("\nSe hallaron las siguientes " +
                              "pruebas con el adn \"%s\":\n\n",
                              adn);
            Lista.Nodo nodo = r.getCabeza();
            while (nodo != null) {
                System.out.println(nodo.get().toString() + "\n");
                nodo = nodo.getSiguiente();
            }
        }

        System.out.printf("Entra un valor numerico para buscar: ");
        double valor = 0;
        try {
            valor = sc.nextDouble();
        } catch (InputMismatchException ime) {
            System.out.printf("Valor numerico no registrado en la base de datos.\n");
        }

        r = bdd.buscaRegistros(CampoCodigoGenetico.VALORNUMERICO, new Double(valor));
        if (r.esVacia()) {
            System.out.printf("\nNo se encontraron codigos geneticos con el valor numérico: %09f que ingresaste.\n", valor);
        } else {
            System.out.printf("\nSe han encontrado los siguientes codigos geneticos " +
                              "con el valor numerico mayor o igual a %09f:\n\n",
                              valor);
            Lista.Nodo nodo = r.getCabeza();
            while (nodo != null) {
                System.out.println(nodo.get().toString() + "\n");
                nodo = nodo.getSiguiente();
            }
        }

        System.out.printf("Entra una categoría para buscar: ");
        int val = 0;
        try {
            val = sc.nextInt();
        } catch (InputMismatchException ime) {
            System.out.printf("La categoría no ha sido registrada en la base de datos.\n");
        }

        r = bdd.buscaRegistros(CampoCodigoGenetico.CATEGORIA, new Integer(val));
        if (r.esVacia()) {
            System.out.printf("\nNo se encontraron categorías con el valor numérico: %d que ingresaste.\n", val);
        } else {
            System.out.printf("\nSe han encontrado los siguientes codigos geneticos " +
                            "con la categoría %d:\n\n",
                    val);
            Lista.Nodo nodo = r.getCabeza();
            while (nodo != null) {
                System.out.println(nodo.get().toString() + "\n");
                nodo = nodo.getSiguiente();
            }
        }
    }

    /* Crea una base de datos y la llena a partir de los datos que el usuario
       escriba a través del teclado. Después la guarda en disco duro y la
       regresa. */
    private static BaseDeDatosCodigoGenetico escritura(String nombreArchivo) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");

        File archivo = new File(nombreArchivo);

        if (archivo.exists()) {
            System.out.printf("El archivo \"%s\" ya existe.\n" +
                              "Presiona Ctrl-C si no quieres reescribirlo, " +
                              "o Enter para continuar...\n", nombreArchivo);
            sc.nextLine();
        }

        System.out.println("Ingresa codigos geneticos a la base de datos.\n" +
                           "Cuando desees terminar, deja el nombre en blanco.\n");

        BaseDeDatosCodigoGenetico bdd = new BaseDeDatosCodigoGenetico();

        do {
            String adn;
            String arn;
            double valorNumerico = 0.0;
            int categoria = 0;
            boolean proteina;

            System.out.printf("ADN   : ");
            adn = sc.next();
            System.out.printf("ARN   : ");
            arn = sc.next();
            if (adn.equals("") || arn.equals(""))
                break;
            try {
                System.out.printf("Valor numerico   : ");
                valorNumerico = sc.nextDouble();
                System.out.printf("Categoría numérica  : ");
                categoria = sc.nextInt();
                System.out.printf("Indica si tiene proteina (true or false) : ");
                proteina = sc.nextBoolean();

            } catch (InputMismatchException ime) {
                System.out.println("\nValor numérico inválido: se descartará " +
                                   "este registro de código genético.\n");
                continue;
            }
            CodigoGenetico e = new CodigoGenetico(adn,
                                          arn,
                                          valorNumerico,
                                          categoria,
                                          proteina)
                    ;
            bdd.agregaRegistro(e);
            System.out.println();
        } while (true);

        int n = bdd.getNumRegistros();
        if (n == 1)
            System.out.printf("\nSe ha agregado un código genético.\n");
        else
            System.out.printf("\nSe agregaron %d codigos geneticos.\n", n);

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
    private static BaseDeDatosCodigoGenetico lectura(String nombreArchivo) {
        BaseDeDatosCodigoGenetico bdd = new BaseDeDatosCodigoGenetico();

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

        Lista r = bdd.getRegistros();
        Lista.Nodo nodo = r.getCabeza();
        while (nodo != null) {
            System.out.println(nodo.get().toString() + "\n");
            nodo = nodo.getSiguiente();
        }

        return bdd;
    }

    /* Imprime en pantalla cómo debe usarse el programa y lo termina. */
    private static void uso() {
        System.out.println("Uso: java -jar practica5.jar [-g|-c] <archivo>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 2)
            uso();

        String bandera = args[0];
        String nombreArchivo = args[1];

        if (!bandera.equals("-g") && !bandera.equals("-c"))
            uso();

        BaseDeDatosCodigoGenetico bdd;

        if (bandera.equals("-g"))
            bdd = escritura(nombreArchivo);
        else
            bdd = lectura(nombreArchivo);

        busquedas(bdd);
    }
}
