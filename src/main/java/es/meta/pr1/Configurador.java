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

/**
 *
 * @author andresrojasortega
 */
public class Configurador {

    ArrayList<String> directoriosDatos;
    Long semilla;
    Integer intentos;
    Integer iteraciones;
    Long semillaTabu;
    Integer intentosTabu;
    Integer iteracionesTabu;

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
                        break;
                    case "Intentos":
                        intentos = Integer.parseInt(split[1]);
                        break;
                    case "iteracioens":
                        iteraciones = Integer.parseInt(split[1]);
                        break;
                    case "Semilla tabu":
                        semillaTabu = Long.parseLong(split[1]);
                        break;
                    case "Intentos tabu":
                        intentosTabu = Integer.parseInt(split[1]);
                        break;
                    case "Iteraciones tabu":
                        iteracionesTabu = Integer.parseInt(split[1]);
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

    public Integer getIntentos() {
        return intentos;
    }

    public Integer getIteraciones() {
        return iteraciones;
    }

    public Long getSemillaTabu() {
        return semillaTabu;
    }

    public Integer getIntentosTabu() {
        return intentosTabu;
    }

    public Integer getIteracionesTabu() {
        return iteracionesTabu;
    }

}

