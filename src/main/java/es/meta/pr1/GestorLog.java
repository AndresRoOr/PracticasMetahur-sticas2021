/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author David
 */
public class GestorLog {
    
    private String _archiveName;
    FileWriter fichero = null;
    PrintWriter pw = null;

    public GestorLog(String name){
        _archiveName = name;
        
    }
    
    void cambiarNombre(String nombre){
        _archiveName = nombre;
    }
    
    
    void abrirArchivo(){
        
        
        try
        {
            fichero = new FileWriter("./archivos/Log/"+_archiveName);
            pw = new PrintWriter(fichero);


        } catch (Exception e) {
            e.printStackTrace();
        }
           
    }
    
    void escribirArchivo(String linea){
        
        pw.println(linea);
        
    }
    
    void cerrarArchivo(){
        
        try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        
    }
    
}
