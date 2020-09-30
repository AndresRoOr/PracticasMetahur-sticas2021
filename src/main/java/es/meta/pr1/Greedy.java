/** @file    Greedy.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 27/09/2020
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @brief Clase que implementa la funcionalidad del algoritmo Greedy
 * @class Greedy
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @date 27/09/2020
 */
public class Greedy {

    ///Atributos de la clase:
    Archivo _archivoDatos;///<Contiene los datos sobre los que operar.
    Set<Integer> _solucionB;
    Double _suma_Resultado;///<Almacena la suma del valor heurístico.

    /**
     * @brief Constructor parametrizado de la clase Greedy
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @param archivoDatos Archivo Contiene los datos sobre los que operar
     */
    public Greedy(Archivo archivoDatos) {
        _archivoDatos = archivoDatos;
        _suma_Resultado = 0.0;
        _solucionB = new HashSet<>();
    }

    /**
     * @brief Algoritmo Greedy que resuelve el problema
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    void greedy(Random aleatorioSemilla) {

        //Generación del primer elemento
        Integer ultimo = GenSolucionIni(aleatorioSemilla);

        //Generación del conjunto de candidatos
        ArrayList<Pair> candidatos = GenCandidatos();

        Pair candidato;

        while (!FuncionSolucion()) {

            candidato = FuncionSeleccion(candidatos, ultimo);

            if (FuncionFactible(candidato.getCandidato())) {
                _solucionB.add(candidato.getCandidato());
                _suma_Resultado = candidato.getCoste();
                ultimo = candidato.getCandidato();
            }

            candidatos.remove(candidato);

        }
    }

    /**
     * @brief Genera el conjunto inicial de candidatos para el algoritmo Greedy
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 30/09/2020
     * @return candidatos ArrayList El conjunto de candidatos inicial.
     */
    ArrayList<Pair> GenCandidatos() {
        ArrayList<Pair> candidatos = new ArrayList<>();

        for (int i = 0; i < _archivoDatos.getTama_Matriz(); i++) {
            if (!_solucionB.contains(i)) {
                candidatos.add(new Pair(i, 0.0));
            }
        }

        return candidatos;
    }

    /**
     * @brief Función selección empleada para elegir el candidato más prometedor
     * por el algoritmo Greedy.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 28/09/2020
     * @param candidatos ArrayList El conjunto de todos los candidatos
     * @param ultimo Integer El último elemento que ha formado parte de la
     * solución
     * @return candidato Pair El candidato más prometedor
     */
    Pair FuncionSeleccion(ArrayList<Pair> candidatos, Integer ultimo) {
        double max = 0.0;
        Iterator<Pair> iterador = candidatos.iterator();
        Pair candidato = null;
        Pair seleccionado;

        while (iterador.hasNext()) {
            candidato = iterador.next();

            candidato.setCoste(_archivoDatos.getMatriz()[ultimo][candidato.getCandidato()]);

            if (candidato.getCoste() > max) {
                max = candidato.getCoste();
                seleccionado = candidato;
            }
        }

        return candidato;
    }

    /**
     * @brief Función solución empleada para determinar si el algoritmo Greedy
     * ha encontrado la solución válida.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 28/09/2020
     * @return
     */
    boolean FuncionSolucion() {
        return !(_solucionB.size() < _archivoDatos.getTama_Solucion());
    }

    /**
     * @brief Función de factibilidad empleada por el algoritmo Greedy para
     * comprobar si un candidato es factible como parte de la solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 28/09/2020
     * @param candidato Integer Candidato sobre el que hacer la comprobación
     * @return true Si el candidato es factible, false en caso contrario
     */
    boolean FuncionFactible(Integer candidato) {
        return (candidato != -1);
    }

    /**
     * @brief Genera una solución inicial que contiene un elemento elegido
     * aleatoriamente
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 28/09/2020
     * @param aleatorioSemilla Random Utilizado para generar un número aleatorio
     * @return sol_Inicial Integer El primer elemento de la solución elegido.
     */
    Integer GenSolucionIni(Random aleatorioSemilla) {
        _solucionB.clear();
        Integer sol_Inicial
                = aleatorioSemilla.nextInt(_archivoDatos.getTama_Matriz());
        _solucionB.add(sol_Inicial);

        return sol_Inicial;
    }

    /**
     * @brief Muestra los resultados del algoritmo Greedy por pantalla.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     */
    void PresentarResultados() {
        System.out.println("Vector Solución");
        System.out.println(_solucionB);
        System.out.println("Coste de la solución: " + _suma_Resultado);

        _solucionB = null;
        _archivoDatos.setMatriz(null);

        System.out.println("");
    }

    /**
     * @brief Calcula la solución resultante de añadir un nuevo valor haciendo
     * uso de las solución parcial actual.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 30/09/2020
     * @param candidato Integer
     * @return suma Double Resultado parcial obtenido
     */
    Double calculoSolucionParcial(Integer candidato) {

        Double coste = _suma_Resultado;
        Iterator<Integer> iterador = _solucionB.iterator();

        while (iterador.hasNext()) {
            coste += _archivoDatos.getMatriz()[iterador.next()][candidato];
        }

        return coste;
    }

    /**
     * @brief Calcula el valor heurístico de un conjunto solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return suma Double Valor heurístico calculado a partir del conjunto
     * solución
     */
    Double calculoValorSolucion() {

        Double coste = 0.0;
        Iterator<Integer> iterador = _solucionB.iterator();
        while (iterador.hasNext()) {
            Iterator<Integer> iterador1 = _solucionB.iterator();
            while (iterador1.hasNext()) {

                coste += _archivoDatos.getMatriz()[iterador.next()][iterador1.next()];

            }
        }

        return coste;
    }
}
