package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Interfaz para registros genéricos. Los registros deben de poder guardarse
 * utilizando una instancia de {@link BufferedWriter}, cargarse utilizando una
 * instancia de {@link BufferedReader}, determinar si sus campos cazan valores
 * arbitrarios y actualizarse con los valores de otro registro.
 *
 * @param <R> El tipo de los registros, para poder actualizar registros del
 * mismo tipo.
 * @param <C> El tipo de los campos de los registros, que debe ser una
 * enumeración {@link Enum}.
 */
public interface Registro<R, C extends Enum> {

    /**
     * Guarda el registro en la salida recibida.
     * @param out la salida donde hay que guardar el registro.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public void guarda(BufferedWriter out) throws IOException;

    /**
     * Carga el registro de la entrada recibida.
     * @param in la entrada de donde hay que cargar el registro.
     * @return <tt>true</tt> si un registro válido fue leído; <tt>false</tt> en
     *         otro caso.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public boolean carga(BufferedReader in) throws IOException;

    /**
     * Nos dice si el registro caza el valor dado en el campo especificado.
     * @param campo el campo que hay que cazar.
     * @param valor el valor con el que debe cazar el campo del registro.
     * @return <tt>true</tt> si el campo especificado del registro caza el valor
     *         dado, <tt>false</tt> en otro caso.
     */
    public boolean caza(C campo, Object valor);
 
    /**
     * Actualiza los valores del registro con los del registro recibido.
     * @param registro el registro con el cual actualizar los valores.
     */
    public void actualiza(R registro);
}
