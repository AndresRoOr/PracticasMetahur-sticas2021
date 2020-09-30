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
        _solucionB = new HashSet <>();
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
        GenSolucionIni(aleatorioSemilla);

        Integer candidato;

        while (!FuncionSolucion()) {

            candidato = FuncionSeleccion();

            if (FuncionFactible(candidato)) {
                _solucionB.add(candidato);
                _suma_Resultado = calculoSolucionParcial(candidato);
            }

        }

    }

    /**
     * @brief Función selección empleada para elegir el candidato más prometedor
     * por el algoritmo Greedy.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 28/09/2020
     * @return candidato Integer El candidato más prometedor
     */
    Integer FuncionSeleccion() {
        Double max = 0.0;
        Integer candi = -1;

        for (int i = 0; i < _archivoDatos.getTama_Matriz(); i++) {
            if (!_solucionB.contains(i)) {
                Double valor = calculoSolucionParcial(i);
                if (valor > max) {
                    max = valor;
                    candi = i;
                }
            }
        }

        return candi;
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
     */
    void GenSolucionIni(Random aleatorioSemilla) {
        _solucionB.clear();
        Integer sol_Inicial
                = aleatorioSemilla.nextInt(_archivoDatos.getTama_Matriz());
        _solucionB.add(sol_Inicial);
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

        while(iterador.hasNext()) {
            coste += _archivoDatos.getMatriz()[iterador.next()]
                    [candidato];
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
        while(iterador.hasNext()) {
            Iterator<Integer> iterador1 = _solucionB.iterator();
            while(iterador1.hasNext()) {
                
                coste += _archivoDatos.getMatriz()[iterador.next()]
                        [iterador1.next()];

            }
        }

        return coste;
    }
}
