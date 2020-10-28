/**
 * @file    Main.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 27/09/2020
 */
package es.meta.pr1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @brief Clase Main del programa
 * @class Main
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @date 27/09/2020
 */
public class Main {

    /**
     * @brief Función principal del programa
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Configurador config = new Configurador("archivos/config.txt");
        
        ArrayList<File> directorios = new ArrayList<>();
        directorios.add(new File("./archivos/Log"));
        directorios.add(new File("./archivos/Log/btabu"));
        directorios.add(new File("./archivos/Log/blocal"));
        directorios.add(new File("./archivos/Log/greedy"));

        for(File directorio : directorios){
            if (!directorio.exists()) {
                if (directorio.mkdirs()) {
                    System.out.println("Directorio " + directorio.getName() + " creado");
                } else {
                    
                }
            }
        }

        for (int i = 0; i < config.getDirectoriosDatos().size(); i++) {
            Metaheuristicas M1 = new Metaheuristicas("Ejemplo",
                    config.getDirectoriosDatos().get(i), config);
            M1.lector_Archivos();

            M1.busquedaTabu();
                    
        }       
        System.exit(0);
    }
}
