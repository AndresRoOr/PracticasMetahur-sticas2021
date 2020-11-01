/** @file    BusquedaLocal.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 03/10/2020
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @brief Clase que implementa la funcionalidad del algoritmo de Búsqueda Local
 * @class BusquedaLocal
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @date 03/10/2020
 */
public class BusquedaLocal {

    Archivo _archivoDatos;///<Contiene los datos del problema
    Set<Integer> _solucion;///<Almacena el conjunto solución
    float _suma_Resultado;///<Usado para almacenar el coste de la solución final
    float _costeActual;///<Almacena el coste de la solucion durante la ejecución
    ArrayList<ElementoSolucion> _listaAportes;///<Contiene el aporte de cada elemento de la solución actual
    Set<Integer> _integrantesNoMejoran;///<Elementos de la solución que no mejoran al intercambiarlos por sus vecinos
    Integer _evaluciones;///<Número de evaluaciones máximas
    long _numEvaluciones;///<Número de evaluaciones actuales
    private GestorLog gestor;
    String linea = "";

    /**
     * @brief Constructor parametrizado de la clase BusquedaLocal
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param archivoDatos Archivo
     * @param evaluaciones Integer
     */
    public BusquedaLocal(Archivo archivoDatos, Integer evaluaciones, GestorLog g) {
        _archivoDatos = archivoDatos;
        _solucion = new HashSet<>();
        _suma_Resultado = 0.0f;
        _evaluciones = evaluaciones;
        _numEvaluciones = 0;
        _costeActual = 0.0f;
        _listaAportes = new ArrayList<>();

        _integrantesNoMejoran = new HashSet<>();

        gestor = g;
    }

    /**
     * @brief Metaheuristica que resuelve el problema
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    void busquedaLocal(Random_p aleatorioSemilla) {

        generearSolucionAleatoria(aleatorioSemilla);

        _costeActual = calcularCoste();

        gestor.escribirArchivo("Solución inicial: " + _solucion);

        gestor.escribirArchivo("Coste: " + _costeActual);

        int eleMenor = 0;

        float costeSolucion = 0.0f;

        boolean mejora = true;

        while (mejora) {

            linea = "";

            mejora = false;
            //calculamos el aporte de todos los elementos de la solución actual
            calcularAportes();

            //calculamos el elemento de la solución actual que menos aporte
            eleMenor = EleMenorAporte();
            if (eleMenor == -1) {
                break;
            }

            //comprobamos el primer vecino que nos mejore
            for (int i = 0; i < _archivoDatos.getTama_Matriz() && !mejora; i++) {
                if (!_solucion.contains(i)) {
                    linea = "";
                    gestor.escribirArchivo("-----Evaluación nº " + _numEvaluciones + "-----");
                    mejora = EvaluarSolucion(i, costeSolucion, eleMenor);

                }
            }
            _listaAportes.clear();

        }

        _listaAportes = null;
        _integrantesNoMejoran.clear();
        _integrantesNoMejoran = null;

        // System.out.println("COSTE: " + _costeActual);
    }

    /**
     * @brief Genera la solución incial
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    void generearSolucionAleatoria(Random_p _aleatorioSemilla) {

        while (_solucion.size() < _archivoDatos.getTama_Solucion()) {

            Integer candidato = _aleatorioSemilla.Randint(0, _archivoDatos.getTama_Matriz() - 1);

            if (!_solucion.contains(candidato)) {
                _solucion.add(candidato);
            }

        }

    }

    /**
     * @brief Calcula el aporte de cada integrante de la solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     */
    void calcularAportes() {

        float aporte = 0.0f;
        Iterator<Integer> iterator = _solucion.iterator();

        while (iterator.hasNext()) {

            Iterator<Integer> iterator2 = _solucion.iterator();
            int i = iterator.next();

            while (iterator2.hasNext()) {

                int j = iterator2.next();
                aporte += _archivoDatos.getMatriz()[i][j];

            }

            ElementoSolucion x = new ElementoSolucion(i, aporte);
            _listaAportes.add(x);
            aporte = 0.0f;
        }
        Collections.sort(_listaAportes);
    }

    /**
     * @brief Busca el elemento de menor aporte de la solución actual
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @return Integrantre de lasolucón con menor aporte
     */
    int EleMenorAporte() {
        int eleMenor = -1;

        for (int i = 0; i < _listaAportes.size() - 1; i++) {
            ElementoSolucion ele = _listaAportes.get(i);
            if (!_integrantesNoMejoran.contains(ele.getId())) {
                eleMenor = ele.getId();
                return eleMenor;
            }
        }
        return eleMenor;
    }

    /**
     * @brief Calcula el coste factorizado de la solución actual
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param i Elemento a sustiuir de la solución actual
     * @param j Candidato a nuevo integrante de la solución
     * @return Devuelve el coste
     */
    float CosteFactorizado(Integer i, Integer j) {

        float costeMenos = 0.0f, costeMas = 0.0f;

        Iterator<Integer> iterator = _solucion.iterator();

        while (iterator.hasNext()) {

            int k = iterator.next();

            costeMenos += _archivoDatos.getMatriz()[k][i];

            if (k != i) {
                costeMas += _archivoDatos.getMatriz()[k][j];
            }

        }

        return (_costeActual - costeMenos + costeMas);
    }

    /**
     * @brief Sustituye el elemnto seleccionado por su candidato
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param i Elemento a sustiuir de la solución actual
     * @param j Candidato a nuevo integrante de la solución
     */
    void Intercambio(Integer i, Integer j) {
        _solucion.remove(i);
        _solucion.add(j);

    }

    /**
     * @brief Calcula el coste de la solucion.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @return coste Float El coste de la solución.
     */
    float calcularCoste() {

        float coste = 0.0f;
        Object[] sol = _solucion.toArray();

        for (int i = 0; i < sol.length - 1; i++) {
            int a = (int) sol[i];
            for (int j = i + 1; j < sol.length; j++) {
                int b = (int) sol[j];
                coste += _archivoDatos.getMatriz()[a][b];
            }
        }

        return coste;

    }

    /**
     * @brief Evalua la solución candidata y desplaza la solución actual a la
     * candidata si la mejora
     * @author Andrés Rojas Ortefa
     * @author David Díaz Jiménez
     * @date 08/10/2020
     * @param i int Elemento candidato.
     * @param costeSolucion float Coste de la solucion candidata
     * @param eleMenor int Elemento de la solucion que aporta menos
     * @return
     */
    boolean EvaluarSolucion(int i, float costeSolucion, int eleMenor) {
        boolean mejora = false;

        //calculamos si mejora la solución actual
        costeSolucion = CosteFactorizado(eleMenor, i);
        _numEvaluciones++;

        //Si resulta mejor nos deplazamos a el
        if (_costeActual < costeSolucion) {
            Intercambio(eleMenor, i);

            linea += eleMenor + " sustituido por " + i;

            //actualizamos el coste de la solucion
            _costeActual = costeSolucion;

            linea += "  Nuevo mejor coste: " + _costeActual;

            mejora = true;
            _integrantesNoMejoran.clear();
        } else {
            if (i == _archivoDatos.getTama_Matriz() - 1) {
                _integrantesNoMejoran.add(eleMenor);
                linea += eleMenor + " añadido a los integrantes que no mejoran ";
            }

            linea += " No se realiza ningún movimiento ";
        }
        gestor.escribirArchivo(linea);
        return mejora;
    }

    /**
     * @brief Muestra por pantalla los datos de la solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     */
    void PresentarResultados() {
        //System.out.println("Vector Solución");
        //System.out.println(_solucion);
        _suma_Resultado = calcularCoste();
        //System.out.println("Coste de la solución: " + _suma_Resultado);
        Main.console.presentarSalida("Coste de la solución: " + _suma_Resultado +"\n");

        gestor.escribirArchivo("");
        gestor.escribirArchivo("Vector Solución: " + _solucion);
        gestor.escribirArchivo("Coste de la solución: " + _suma_Resultado);

        _solucion.clear();
        _solucion = null;

        System.out.println("");
    }

}
