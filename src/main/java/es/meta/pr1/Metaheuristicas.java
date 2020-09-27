/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author David
 */
public class Metaheuristicas {

    Configurador _config;
    String _nombre;
    ArrayList<Archivo> _archivos;
    String _ruta_Carpeta_Archivos;

    Metaheuristicas(String nombre, String ruta, Configurador config) {
        _config = config;
        _nombre = nombre;
        _ruta_Carpeta_Archivos = ruta;
        _archivos = new ArrayList<>();
    }

    void lector_Archivos() throws FileNotFoundException, IOException {
        final File carpeta = new File(_ruta_Carpeta_Archivos);
        for (final File fichero_Entrada : carpeta.listFiles()) {
            System.out.println(fichero_Entrada.getName());
            Archivo ar = new Archivo(fichero_Entrada.getName(),
                    _ruta_Carpeta_Archivos + "/"
                    + fichero_Entrada.getName(), 79956084);
            _archivos.add(ar);
        }
    }

    void mostrar_Datos() {
        for (Archivo ar : _archivos) {
            ar.presentarDatos();
        }
    }

    void greedy() {
        for (Archivo ar : _archivos) {

            long inicio = System.currentTimeMillis();
            //ar.greedy();
            Greedy g = new Greedy(ar);
            g.greedy(new Random(_config.getSemilla()));
            long fin = System.currentTimeMillis();
            double tiempo = (double) ((fin - inicio));
            System.out.println("Datos de la solución al problema: " + ar._nombre);
            System.out.println("Tiempo de ejecución del algoritmo: " + tiempo + " milisegundos");
            //ar.PresentarResultados();
            g.PresentarResultados();

        }
    }

}
