/**
 * @file    Main.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 27/09/2020
 */
package es.meta.pr1;

import com.formdev.flatlaf.FlatLightLaf;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;


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
    public  static int narchivos;
    
    public static void main(String[] args) throws IOException {
        
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        
        Configurador config = new Configurador("archivos/config.txt");
        
        ArrayList<File> directorios = new ArrayList<>();
        directorios.add(new File("./archivos/Log"));
        directorios.add(new File("./archivos/Log/btabu"));
        directorios.add(new File("./archivos/Log/blocal"));
        directorios.add(new File("./archivos/Log/greedy"));
        
        directorios.stream().filter(directorio -> (!directorio.exists())).forEachOrdered((File directorio) -> {
            if (directorio.mkdirs()) {
                System.out.println("Directorio " + directorio.getName() + " creado");
            }
        });
        
        for(String direc : config.getDirectoriosDatos()){
            File directory = new File(direc);
            int fileCount=directory.list().length;
            narchivos+=fileCount;
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
                Metaheuristicas M1 = new Metaheuristicas(config.getDirectoriosDatos().get(i),
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
