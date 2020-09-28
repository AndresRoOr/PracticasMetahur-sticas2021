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

    /*static ArrayList<String> listalineas = new ArrayList<>();
    static ArrayList<String> archivos = new ArrayList<>();

    static int seedblocal;
    static int intentosblocal;
    static int iteracionesblocal;
    static int seedtabu;
    static int intentostabu;
    static int iteracionestabu;

    static void leeFichero(String ruta) {

        listalineas.clear();
        archivos.clear();

        File file = new File(ruta);

        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                listalineas.add(sc.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e.getClass());
        }

        for (int i = 0; i <= listalineas.size() - 1; i++) {
            if (listalineas.get(i).equals("Datos")) {
                while (!listalineas.get(i + 1).equals("Parametros")) {
                    //System.out.print(listalineas.get(i+1));
                    archivos.add(listalineas.get(i + 1));
                    i++;
                }
            }
            if (listalineas.get(i).equals("Parametros")) {
                i++;
            }
            if (listalineas.get(i).equals("seedblocal")) {
                //System.out.print(listalineas.get(i+1));
                seedblocal = Integer.parseInt(listalineas.get(i + 1));
                i++;
            }
            if (listalineas.get(i).equals("intentosblocal")) {
                //System.out.print(listalineas.get(i+1));
                intentosblocal = Integer.parseInt(listalineas.get(i + 1));
                i++;
            }
            if (listalineas.get(i).equals("iteracionesblocal")) {
                //System.out.print(listalineas.get(i+1));
                iteracionesblocal = Integer.parseInt(listalineas.get(i + 1));
                i++;
            }

            if (listalineas.get(i).equals("seedtabu")) {
                //System.out.print(listalineas.get(i+1));
                seedtabu = Integer.parseInt(listalineas.get(i + 1));
                i++;
            }
            if (listalineas.get(i).equals("intentostabu")) {
                //System.out.print(listalineas.get(i+1));
                intentostabu = Integer.parseInt(listalineas.get(i + 1));
                i++;
            }
            if (listalineas.get(i).equals("iteracionestabu")) {
                //System.out.print(listalineas.get(i+1));
                iteracionestabu = Integer.parseInt(listalineas.get(i + 1));
                i++;
            }
        }
    }
    */
    
    public static void main(String[] args) throws IOException {

        Configurador config = new Configurador("archivos/config.txt");

        for (int i = 0; i < config.getDirectoriosDatos().size(); i++) {
            Metaheuristicas M1 = new Metaheuristicas("Ejemplo", config.getDirectoriosDatos().get(i),config);
            M1.lector_Archivos();

            M1.busquedaLocal();
        }
        //M1.mostrar_Datos();

    }
}
