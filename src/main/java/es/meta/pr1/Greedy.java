/** @file    Greedy.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 27/09/2020
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.util.Random;

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
    ArrayList<Integer> _solucion;///<Almacena el conjunto solución.
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
        _solucion = new ArrayList<>();
        _suma_Resultado = 0.0;
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
        _solucion.clear();
        Integer sol_Inicial
                = aleatorioSemilla.nextInt(_archivoDatos.getTama_Matriz());
        _solucion.add(sol_Inicial);

        Integer candidato;

        while (!FuncionSolucion()) {

            candidato = FuncionSeleccion();

            if (candidato != -1) {
                _solucion.add(candidato);
                _suma_Resultado = calculoSolucionParcial();
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
        Integer candidato = -1;

        for (int i = 0; i < _archivoDatos.getTama_Matriz(); i++) {
            if (!_solucion.contains(i)) {
                _solucion.add(i);
                Double valor = calculoSolucionParcial();
                if (valor > max) {
                    max = valor;
                    candidato = i;
                }
                _solucion.remove(_solucion.size() - 1);
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
        return !(_solucion.size() < _archivoDatos.getTama_Solucion());
    }

    /**
     * @brief Muestra los resultados del algoritmo Greedy por pantalla.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     */
    void PresentarResultados() {
        System.out.println("Vector Solución");
        System.out.println(_solucion);
        System.out.println("Coste de la solución: " + _suma_Resultado);

        _solucion = null;
        _archivoDatos.setMatriz(null);

        System.out.println("");
    }

    /**
     * @brief Calcula la solución resultante de añadir un nuevo valor haciendo
     * uso de las solución parcial actual.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 27/09/2020
     * @return suma Double Resultado parcial obtenido
     */
    Double calculoSolucionParcial() {

        Double suma = _suma_Resultado;

        for (int i = 0; i < _solucion.size() - 1; i++) {
            suma += _archivoDatos.getMatriz()[_solucion.get(i)]
                    [_solucion.get(_solucion.size() - 1)];
        }

        return suma;
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

        Double suma = 0.0;
        for (int i = 0; i < _solucion.size(); i++) {
            for (int j = i + 1; j < _solucion.size(); j++) {

                suma += _archivoDatos.getMatriz()[_solucion.get(i)]
                        [_solucion.get(j)];

            }
        }

        return suma;
    }
}
