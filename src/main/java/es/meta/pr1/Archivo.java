/**
 * @file    Archivo.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 27/09/2020
 */
package es.meta.pr1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @brief Clase que almacena toda la información que se encuentra dentro de un
 * archivo
 * @class Archivo
 * @author Andrés Roja Ortega
 * @author David Díaz Jiménez
 * @date 27/09/2020
 */
public class Archivo {

    ///Atributos de la clase:
    String _nombre;///<Nombre del objeto                                        //No se utiliza
    String _ruta;///<Ruta completa del archivo de datos
    Integer _tama_Matriz;///<Tamaño de la matriz de datos
    Integer _tama_Solucion;///<Tamaño de la solución
    Random _aleatorioSemilla;///<Número aleatorio                               //¿Borrar? Se utiliza la de Configurador
    long _semilla;///<Semilla para que sea repetible el algoritmo               //¿Borrar? Se utiliza la de Configurador
    float[][] _matriz;///<Matriz que almacena los datos del archivo

    ArrayList<ArrayList<Integer>> _memorias;                                    //No se utiliza, por ahora
    Map<Integer, Double> _valores;                                              //No se utiliza, por ahora
    int _num_Mejoras;                                                           //No se utiliza, por ahora

    /**
     * @brief Constructor parametrizado de la clase Archivo
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @param nombre String Nombre del objeto de la clase Archivo
     * @param ruta String Ruta completa del archivo que contiene los datos
     * @param semilla long Semilla para generar números aleatorios
     * @throws FileNotFoundException
     * @throws IOException
     */
    Archivo(String nombre, String ruta, long semilla)
            throws FileNotFoundException, IOException {

        _nombre = nombre;
        _ruta = ruta;
        _semilla = semilla;
        _aleatorioSemilla = new Random(_semilla);
        _tama_Matriz = 0;
        _tama_Solucion = 0;

        /*                                                                      //Comentado hasta hacer uso de ello
        _memorias = new ArrayList<>();
        _valores = new HashMap<>();
        _num_Mejoras = 0;
         */
        try {
            BufferedReader br = new BufferedReader(new FileReader(_ruta));
            String currentRecord = br.readLine();
            int num_linea = 0;

            while (currentRecord != null) {
                if (!currentRecord.isEmpty()) {

                    String[] linea = currentRecord.split(" ");

                    if (num_linea == 0) {
                        _tama_Matriz = Integer.parseInt(linea[0]);
                        _tama_Solucion = Integer.parseInt(linea[1]);
                        _matriz = new float[_tama_Matriz][_tama_Matriz];

                        System.out.println("Tamanio de Matriz " + _tama_Matriz);
                        System.out.println("Tamanio de la Solucion "
                                + linea[1]);
                    } else {

                        Integer i = (Integer.parseInt(linea[0]));
                        Integer j = (Integer.parseInt(linea[1]));
                        _matriz[i][j] = (Float.parseFloat(linea[2]));
                        _matriz[j][i] = (Float.parseFloat(linea[2]));

                    }
                    num_linea++;
                }
                currentRecord = br.readLine();
            }
            br.close();
            System.out.println("");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * @brief Método getter para el parámetro _nombre
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _nombre String
     */
    public String getNombre() {
        return _nombre;
    }

    /**
     * @brief Método getter para el parámetro _ruta
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _ruta String
     */
    public String getRuta() {
        return _ruta;
    }

    /**
     * @brief Método getter para el parámetro _tama_Matriz
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _tama_Matriz Integer
     */
    public Integer getTama_Matriz() {
        return _tama_Matriz;
    }

    /**
     * @brief Método getter para el parámetro _tama_Solucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _tama_Solucion Integer
     */
    public Integer getTama_Solucion() {
        return _tama_Solucion;
    }

    /**
     * @brief Método getter para el parámetro _aleatorioSemilla
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _aleatorioSemilla Random
     */
    public Random getAleatorioSemilla() {
        return _aleatorioSemilla;
    }

    /**
     * @brief Método getter para el parámetro _semilla
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _semilla long
     */
    public long getSemilla() {
        return _semilla;
    }

    /**
     * @brief Método getter para el parámetro _matriz
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return _matriz double[][]
     */
    public float[][] getMatriz() {                                             //Mirar paso por referencia
        return _matriz;
    }

    /**
     * @brief Método setter para el parámetro _matriz
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @param _matriz double[][] Nuevo valor de _matriz
     */
    public void setMatriz(float[][] _matriz) {
        this._matriz = _matriz;
    }

    /**
     * @brief Muestra por pantalla los datos del Archivo y el contenido de
     * _matriz
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     */
    void presentarDatos() {
        System.out.println(_nombre);
        System.out.println("Datos matriz");
        for (int i = 0; i < _tama_Matriz; i++) {
            for (int j = 0; j < _tama_Matriz; j++) {
                System.out.print(_matriz[i][j] + " ");
            }
            System.out.println("");
        }

    }

}
