/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author David
 */
public class Archivo {

    String _nombre;//> Nombre del problema
    String _ruta;//> Ruta completa del archivo de datos
    Integer _tama_Matriz;//>Tamaño de la matriz de datos
    Integer _tama_Solucion;//>Tamaño de la solución
    Random _aleatorioSemilla;//>Número aleatorio***************************************************************************
    long _semilla;//> Semilla para que sea repetible el algoritmo
    int _num_Mejoras;
    float[][] _matriz;//>Matriz que almacena los datos del archivo

    ArrayList<ArrayList<Integer>> _memorias;

    Map<Integer, Double> _valores;

    Archivo(String nombre, String ruta, long semilla) throws FileNotFoundException, IOException {
        _nombre = nombre;
        _ruta = ruta;
        _semilla = semilla;
        _aleatorioSemilla = new Random(_semilla);
        _tama_Matriz = 0;
        _tama_Solucion = 0;
        /* _solucion = new ArrayList<>();
        _memorias = new ArrayList<>();
        _suma_Resultado = 0.0;
        
        _valores = new HashMap<>();
        
        _num_Mejoras = 0;*/

        try {
            BufferedReader br = new BufferedReader(new FileReader(_ruta));
            String currentRecord = br.readLine();
            int num_linea = 0;
            //Double indice = 0.0;
            while (currentRecord != null) {
                if (!currentRecord.isEmpty()) {
                    //System.out.println(currentRecord);
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

    public String getNombre() {
        return _nombre;
    }

    public String getRuta() {
        return _ruta;
    }

    public Integer getTama_Matriz() {
        return _tama_Matriz;
    }

    public Integer getTama_Solucion() {
        return _tama_Solucion;
    }

    public Random getAleatorioSemilla() {
        return _aleatorioSemilla;
    }

    public long getSemilla() {
        return _semilla;
    }

    public float[][] getMatriz(){ //Mirar paso por referencia
        return _matriz;
    }

    public void setMatriz(float[][] _matriz) {
        this._matriz = _matriz;
    }
    
    

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
