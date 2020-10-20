/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Arrays;

/**
 *
 * @author andresrojasortega
 */
public class Configurador {

    ArrayList<String> directoriosDatos;
    Long semilla;
    Integer iteraciones;
    Integer intentosTabu;
    Integer iteracionesTabu;
    Integer teneciaTabu;
    Long recuperarSemilla;
    

    public Configurador(String ruta) {
        directoriosDatos = new ArrayList<>();
        String linea;
        FileReader f = null;
        try {
            f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);

            while ((linea = b.readLine()) != null) {
                String[] split = linea.split("=");
                switch (split[0]) {
                    case "Datos":
                        String[] v = split[1].split(" ");
                        for (int i = 0; i < v.length; i++) {
                            directoriosDatos.add(v[i]);
                        }
                        break;
                    case "Semilla":
                        semilla = Long.parseLong(split[1]);
                        recuperarSemilla = semilla;
                        break;
                    case "Iteraciones":
                        iteraciones = Integer.parseInt(split[1]);
                        break;
                    case "Intentos tabu":
                        intentosTabu = Integer.parseInt(split[1]);
                        break;
                    case "Iteraciones tabu":
                        iteracionesTabu = Integer.parseInt(split[1]);
                        break;
                    case "Tenencia tabu":
                        teneciaTabu = Integer.parseInt(split[1]);
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != f) {
                    f.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public ArrayList<String> getDirectoriosDatos() {
        return directoriosDatos;
    }

    public Long getSemilla() {
        return semilla;
    }

    public Integer getIteraciones() {
        return iteraciones;
    }

    public Integer getIntentosTabu() {
        return intentosTabu;
    }

    public Integer getIteracionesTabu() {
        return iteracionesTabu;
    }
    
    public Integer getTenenciaTabu() {
        return teneciaTabu;
    }

    void rotarSemilla() {
        //-------------------------------------------------
        long semillaAntigua = semilla;
        //-------------------------------------------------

        char[] cadenaSemilla = semilla.toString().toCharArray();
        char[] cadenaRotada = new char[8];

        cadenaRotada[7] = cadenaSemilla[0];
        
        for (int i = 0; i < 7; i++) {
            cadenaRotada[i] = cadenaSemilla[i + 1];
        }

        semilla = Long.parseLong(String.valueOf(cadenaRotada));
    }
    
    void recuperarSemilla(){
        semilla=recuperarSemilla;
    }
}
    
    
