/**
 * @file    Configurador.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 27/09/2020
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * @brief Clase que almacena todos los parámetros principales del programa
 * @class Configurador
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @date 27/09/2020
 */
public class Configurador {

    ///Atributos de la clase:
    ArrayList<String> directoriosDatos;///<Almacena los directorios donde se 
    ///encuentran los archivos con la información del problema
    Long semilla;///<Semilla utilizada para generar número aleatorios
    Integer intentos;///<Número de intentos
    Integer iteraciones;///<Número de iteraciones
    Long semillaTabu;///<Semilla utilizada para generar números aleatorios en el
    ///algoritmo de la búsqueda tabú
    Integer intentosTabu;///<Número de intentos en la búsqueda tabú
    Integer iteracionesTabu;///<Número de iteraciones en la búsqueda tabú

    /**
     * @brief Constructor parametrizado de la clase Configurador
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @param ruta String Contiene la ruta completa del archivo que contiene la
     * información de los parámetros
     */
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
                    case "Iteraciones":
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
            b.close();

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (null != f) {
                    f.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * @brief Funcion getter del atributo directoriosDatos
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return directoriosDatos ArrayList
     */
    public ArrayList<String> getDirectoriosDatos() {
        return directoriosDatos;
    }

    /**
     * @brief Funcion getter del atributo semilla
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return semilla Long
     */
    public Long getSemilla() {
        return semilla;
    }

    /**
     * @brief Funcion getter del atributo intentos
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return intentos Integer
     */
    public Integer getIntentos() {
        return intentos;
    }

    /**
     * @brief Funcion getter del atributo iteraciones
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return iteraciones Integer
     */
    public Integer getIteraciones() {
        return iteraciones;
    }

    /**
     * @brief Funcion getter del atributo semillaTabu
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return semillaTabu Long
     */
    public Long getSemillaTabu() {
        return semillaTabu;
    }

    /**
     * @brief Funcion getter del intentosTabu
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return intentosTabu Integer
     */
    public Integer getIntentosTabu() {
        return intentosTabu;
    }

    /**
     * @brief Funcion getter del atributo iteracionesTabu
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return iteracionesTabu Integer
     */
    public Integer getIteracionesTabu() {
        return iteracionesTabu;
    }

}
