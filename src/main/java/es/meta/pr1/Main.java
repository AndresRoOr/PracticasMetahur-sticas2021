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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public static Consola console = new Consola();
    
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
        
        
        console.presentarSalida("");

        while(console.getEleccion()!=4){
                
            while(console.getEleccion() == 0){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(console.getEleccion() == 4){
                System.exit(0);
            }
            
            for (int i = 0; i < config.getDirectoriosDatos().size(); i++) {
                Metaheuristicas M1 = new Metaheuristicas("Ejemplo",
                        config.getDirectoriosDatos().get(i), config);
                M1.lector_Archivos();


                switch(console.getEleccion()){

                    case 1:

                        M1.greedy();
                        break;

                    case 2:  
                        M1.busquedaLocal();
                        break;

                    case 3:
                        M1.busquedaTabu();
                        break;
                    case 4:
                        System.exit(0);


                }

            } 
            
            console.restaurarEleccion();
            //System.exit(0);
        }
    }
}
