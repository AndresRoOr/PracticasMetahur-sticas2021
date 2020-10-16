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
import java.util.LinkedList;
import java.util.Set;
import javafx.util.Pair;

/**
 * @brief Clase que implementa la funcionalidad del algoritmo de Búsqueda Local
 * @class BusquedaLocal
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @date 03/10/2020
 */
public class BusquedaTabu {

    /**
     * @brief Clase auxiliar necesaria para representar toda la información de
     * cada elemento perteneciente al atributo _listaAportes
     * @class ElementoSolucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     */
    
    public class ElementoSolucion implements Comparable<ElementoSolucion> {

        ///Atributos de la clase:
        private Integer id;///<Indica el elemento de la solución que representa
        private Float _contribucion;///<Coste que aporta a la solución

        /**
         * @brief Constructor parametrizado de la clase ElementoSolucion
         * @author Andrés Rojas Ortega
         * @author David Díaz Jiménez
         * @date 03/10/2020
         * @param id Integer
         * @param _contribucion Float
         */
        public ElementoSolucion(Integer id, Float _contribucion) {
            this.id = id;
            this._contribucion = _contribucion;
        }

        @Override
        public int compareTo(ElementoSolucion vecino) {
            return (int) (this.getContribucion() - vecino.getContribucion());
        }

        public Integer getId() {
            return this.id;
        }

        public Float getContribucion() {
            return _contribucion;
        }

        public void setContribucion(float cont) {
            this._contribucion = cont;
        }

        public String toString() {
            return "Key: " + getId() + ", Value: " + getContribucion();
        }

    }

    Archivo _archivoDatos;///<Contiene los datos del problema
    Set<Integer> _solucion;
    Set<Integer> _mejorSolucion;///<Almacena el conjunto solución
    float _suma_Resultado;///<Usado para almacenar el coste de la solución final
    float _costeActual;///<Almacena el coste de la solucion durante la ejecución
    float _mejorCoste;
    ArrayList<ElementoSolucion> _listaAportes;///<Contiene el aporte de cada elemento de la solución actual
    Set<Integer> _integrantesNoMejoran;///<Elementos de la solución que no mejoran al intercambiarlos por sus vecinos
    Integer _iteraciones;///<Número de evaluaciones máximas
    long _numIteraciones;///<Número de evaluaciones actuales
    Integer _intentos;
    Integer _numIntentos;
    ArrayList<Integer> _memoriaLargoPlazo;
    LinkedList<Integer> _memoriaCortoPlazo;

    /**
     * @brief Constructor parametrizado de la clase BusquedaLocal
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param archivoDatos Archivo
     * @param evaluaciones Integer
     */
    public BusquedaTabu(Archivo archivoDatos, Integer evaluaciones, Integer Intentos) {
        _archivoDatos = archivoDatos;
        _solucion = new HashSet<>();
        _mejorSolucion = new HashSet<>();
        _suma_Resultado = 0.0f;
        _iteraciones = evaluaciones;
        _numIteraciones = 0;
        _intentos = 100;
        _numIntentos = 0;
        _costeActual = 0.0f;
        _mejorCoste = 0.0f;
        _listaAportes = new ArrayList<>();

        _integrantesNoMejoran = new HashSet<>();
        _memoriaLargoPlazo = new ArrayList<>();
        _memoriaCortoPlazo = new LinkedList<>();
        for(int i = 0; i <5; i++){
            _memoriaCortoPlazo.addLast(-1);
        }
        for(int i = 0; i <_archivoDatos.getTama_Matriz(); i++){
            _memoriaLargoPlazo.add(0);
        }
    }

    /**
     * @brief Metaheuristica que resuelve el problema
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    void busquedaTabu(Random_p aleatorioSemilla) {

        generearSolucionAleatoria(aleatorioSemilla);

        _costeActual = calcularCoste();
        _mejorCoste = _costeActual;
        
        _mejorSolucion = _solucion;
        
        int eleMenor = 0;

        boolean mejora = true;

        while (_numIteraciones < _iteraciones && mejora) {

            mejora = false;
            //calculamos el aporte de todos los elementos de la solución actual
            calcularAportes();
            //por cada elemento seleccionado
            if(_numIntentos != _intentos){
                for (int k = 0; k < _archivoDatos.getTama_Solucion() && !mejora; k++) {
                //calculamos el elemento de la solución actual que menos aporte
                    eleMenor = EleMenorAporte();
                    if (eleMenor == -1) {
                        break;
                    }

                    //comprobamos el vecindario

                    mejora = EvaluarSolucion(eleMenor);

                    
                }
            }else{
                
                System.out.println("Reinicialización");
                
            }
            _listaAportes.clear();

        }

        _listaAportes = null;
        _integrantesNoMejoran.clear();
        _integrantesNoMejoran = null;

        System.out.println("COSTE: " + _costeActual);
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
            
            eleMenor = ele.getId();
            return eleMenor;
            
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
    boolean EvaluarSolucion( int eleMenor) {
        boolean mejora = false;
        Integer mejorCandidato = 0;
        Integer candidatosEva = 1;
        Float Coste= 0.0f;
        Float mejorCosteCandidato =0.0f;
        int min = (eleMenor -5);
        int max = (eleMenor +5);
        if(min < 0){ min = 0; max = 10;}
        if(max>_archivoDatos.getTama_Matriz()-1){ max = _archivoDatos.getTama_Matriz()-1; min = max -10;}
        
        for(int i = min; i< max && candidatosEva <=10; i++){
            
            if(!_solucion.contains(i) && !_memoriaCortoPlazo.contains(i)){
                
                Coste = CosteFactorizado(eleMenor, i);
                if(Coste> mejorCosteCandidato){
                    mejorCosteCandidato = Coste;
                    mejorCandidato = i;
                }
                candidatosEva++;
            }
            
        }
        
        _costeActual = mejorCosteCandidato;
        Intercambio(eleMenor, mejorCandidato);
        _numIteraciones++;
        
        //Si resulta mejor nos deplazamos a el
        if (_costeActual < _mejorCoste) {   _numIntentos++;
        } else { 
            _numIntentos = 0; 
            _mejorSolucion = _solucion;
            _mejorCoste = _costeActual;
        }
        
        ActualizarMemorias(eleMenor);
        
        mejora = true;
        
        return mejora;
    }
    
    void ActualizarMemorias(Integer elementoTabu){
        
        Object[] sol = _solucion.toArray();

        for (Object elemento : sol) {
            
            int a = (int) elemento;
            _memoriaLargoPlazo.set(a, _memoriaLargoPlazo.get(a) +1);
            
        }
           
        _memoriaCortoPlazo.addLast(elementoTabu);
        _memoriaCortoPlazo.pop();
    }

    /**
     * @brief Muestra por pantalla los datos de la solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     */
    void PresentarResultados() {
        System.out.println("Vector Solución");
        System.out.println(_solucion);
        _suma_Resultado = calcularCoste();
        System.out.println("Coste de la solución: " + _suma_Resultado);

        _solucion.clear();
        _solucion = null;

        System.out.println("");
    }

}
