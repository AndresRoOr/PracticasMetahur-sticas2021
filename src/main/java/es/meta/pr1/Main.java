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
import java.util.Scanner;

/**
 *
 * @author David
 */
public class Main {

    
    public static void main(String[] args) throws IOException {

        Configurador config = new Configurador("archivos/config.txt");

        for (int i = 0; i < config.getDirectoriosDatos().size(); i++) {
            Metaheuristicas M1 = new Metaheuristicas("Ejemplo",
                    config.getDirectoriosDatos().get(i),config);
            M1.lector_Archivos();

            M1.busquedaLocal();
        }
        //M1.mostrar_Datos();
        
        System.exit(0);

    }
}
