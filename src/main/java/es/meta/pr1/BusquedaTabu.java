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
    
    public class ElementoSolucion implements Comparable<ElementoSolucion>{

        ///Atributos de la clase:
        private int id;///<Indica el elemento de la solución que representa
        private float _contribucion;///<Coste que aporta a la solución
        private int _vecesSolucion;
        /**
         * @brief Constructor parametrizado de la clase ElementoSolucion
         * @author Andrés Rojas Ortega
         * @author David Díaz Jiménez
         * @date 03/10/2020
         * @param id Integer
         * @param _contribucion Float
         */
        public ElementoSolucion(int id, float _contribucion) {
            this.id = id;
            this._contribucion = _contribucion;
            this._vecesSolucion = 0;
        }
        
        public ElementoSolucion(int id, int veces) {
            this.id = id;
            this._vecesSolucion = veces;
            this._contribucion = veces;
        }

        @Override
        public int compareTo(ElementoSolucion vecino) {
            Float ele1 = this.getContribucion();
            Float ele2 = vecino.getContribucion();
            int comparativa = ele1.compareTo(ele2);
            
            if(comparativa < 0)
                return -1;
            else if(comparativa > 0)
                return 1;
            else
                return 0;
    
        }

        public int getId() {
            return this.id;
        }

        public float getContribucion() {
            return this._contribucion;
        }
        
        public int getVeces() {
            return this._vecesSolucion;
        }

        public void setContribucion(float cont) {
            this._contribucion = cont;
        }
        
        public void setVeces() {
            this._vecesSolucion ++;
            this._contribucion = (float)this._vecesSolucion;
        }

        public String toString() {
            return "Key: " + getId() + ", Value: " + getContribucion();
        }

    }

    Archivo _archivoDatos;///<Contiene los datos del problema
    Set<Integer> _solucion;
    Set<Integer> _mejorSolucion;///<Almacena el conjunto solución
    float _costeActual;///<Almacena el coste de la solucion durante la ejecución
    float _mejorCoste;///<Almacena el coste de la mejor solución obtenida
    int _iteraciones;///<Número de evaluaciones máximas
    long _numIteraciones;///<Número de evaluaciones actuales
    int _intentos;///<Número de intentos de mejora máximos
    int _numIntentos;///<Número de intentos de mejora actuales
    int _tenenciaTabu;///<Tamaño de la memoria de corto plazo
    ArrayList<ElementoSolucion> _memoriaLargoPlazo;///<Almacena la frecuencia de
    // aparición de los elementos en la solución
    LinkedList<Integer> _memoriaCortoPlazo;///<Almacena los últimos movimientos
    //de intercambio
    int _numRestartMenor;///<Número de reinicializaciones de intensificación
    int _numRestartMayor;///<Número de reinicializaciones de exploración
    GestorLog gestor;
    String linea ="";

    /**
     * @brief Constructor parametrizado de la clase BusquedaLocal
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param archivoDatos Archivo
     * @param iteraciones
     * @param Intentos
     * @param tenenciaTabu
     */
    public BusquedaTabu(Archivo archivoDatos, Integer iteraciones, Integer Intentos, Integer tenenciaTabu, GestorLog g) {
        _archivoDatos = archivoDatos;
        _solucion = new HashSet<>();
        _mejorSolucion = new HashSet<>();
        _iteraciones = iteraciones;
        _numIteraciones = 0;
        _intentos = Intentos;
        _numIntentos = 0;
        _tenenciaTabu = tenenciaTabu;
        _costeActual = 0.0f;
        _mejorCoste = 0.0f;
        
        gestor = g;

        _memoriaLargoPlazo = new ArrayList<>();
        _memoriaCortoPlazo = new LinkedList<>();
        for(int i = 0; i <_tenenciaTabu; i++){
            _memoriaCortoPlazo.addLast(-1);
        }
        for(int i = 0; i <_archivoDatos.getTama_Matriz(); i++){
            _memoriaLargoPlazo.add(new ElementoSolucion(i, 0));
        }
        
        _numRestartMayor = 0;
        _numRestartMenor = 0;
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

        Random_p s = aleatorioSemilla;
        
        for(int ele: _solucion){
                int a = ele;
                _mejorSolucion.add(a);
            }
        _costeActual = calcularCoste(false);
        _mejorCoste = _costeActual;
        
        gestor.escribirArchivo("Solución inicial: " + _solucion);
        gestor.escribirArchivo("");
        gestor.escribirArchivo("Coste: " + _costeActual);
        gestor.escribirArchivo("");
        
        int eleMenor = 0;

        while (_numIteraciones < _iteraciones) {
            
            linea = "";
            gestor.escribirArchivo("-----Iteración nº " +_numIteraciones+"-----");
                
            if(_numIntentos < _intentos){
             
                eleMenor = calcularAportes();
                
                EvaluarSolucion(eleMenor,s);
                
            }else{
                
                Reinicializar(s);
                _numIntentos = 0;
                
            }
            
            gestor.escribirArchivo(linea);

        }

        System.out.println("COSTE: " + _mejorCoste);
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
     * @return elemento de la solución actual con el menor aporte
     */
    int calcularAportes() {

        float aporte = 0.0f;
        Iterator<Integer> iterator = _solucion.iterator();
        float menosAporte = Float.MAX_VALUE;
        int elementoMenorAporte = -1;

        while (iterator.hasNext()) {

            Iterator<Integer> iterator2 = _solucion.iterator();
            int i = iterator.next();

            while (iterator2.hasNext()) {

                int j = iterator2.next();
                aporte += _archivoDatos.getMatriz()[i][j];

            }

            ElementoSolucion x = new ElementoSolucion(i, aporte);
            
            if(aporte<menosAporte){
                menosAporte = aporte;
                elementoMenorAporte = i;
            }
            
            aporte = 0.0f;
        }

        return elementoMenorAporte;
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
    float CosteFactorizado(int i, int j) {

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
    void Intercambio(int i, int j) {
        _solucion.remove(i);
        _solucion.add(j);

    }
    
    
    /**
     * @brief Cuando la búsqueda Tabú llega a las X iteraciones sin mejora
     * se reinicilaiza la búsqueda, creando una solución con los más visitidos 
     * o con los menos visitados
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 20/10/2020
     * @param ale aleatorio
     */
    void Reinicializar(Random_p ale){
        
        float p = (float) ale.Randfloat(0, 1);
        
        Set<Integer> sol = new HashSet<>();
        ArrayList<ElementoSolucion> aux = new ArrayList<>();
        int i = 0;
        for(ElementoSolucion ele : _memoriaLargoPlazo){
            i = ele.getId();
            ElementoSolucion f = new ElementoSolucion(i, ele.getVeces());
            aux.add(f);
        }
        
        Collections.sort(aux);
        
        if(p>0.5){
            //Intensificar
            while(sol.size()< _archivoDatos.getTama_Solucion()){
                sol.add(aux.get(aux.size()-1).getId());
                aux.remove(aux.size()-1);
            }
            
            linea+=" Intensificación ";
            _numRestartMayor++;
            
        }else{
            //Diversificar
            while(sol.size()< _archivoDatos.getTama_Solucion()){
                sol.add(aux.get(0).getId());
                aux.remove(0);
            }
            linea+=" Diversificación ";
            _numRestartMenor++;
        }
        
        // Actualizamos la solución
        _solucion.clear();
        for(int ele: sol){
                int a = ele;
                _solucion.add(a);
            }
        _costeActual = calcularCoste(true);
        
        linea+=", coste actual: "  +_costeActual +", mejor coste: "+ _mejorCoste;
        //Si mejora a la mejor solución, la actualizamos
        
        if(_costeActual > _mejorCoste){
            _mejorCoste = _costeActual;
            _mejorSolucion.clear();
            for(int ele: _solucion){
                int a = ele;
                _mejorSolucion.add(a);
            }
            
            linea+=" nuevo mejor coste;";
        }
        
        //Reiniciamos las memorias
        
        _memoriaCortoPlazo.clear();
        _memoriaLargoPlazo.clear();
        
        for(int a = 0; a <_tenenciaTabu; a++){
            _memoriaCortoPlazo.addLast(-1);
        }
        for(int a = 0; a <_archivoDatos.getTama_Matriz(); a++){
            _memoriaLargoPlazo.add(new ElementoSolucion(a, 0));
        }
        
        //Actualizamos las memorias
        
        _numIteraciones++;
        ActualizarMemorias(-1);
    }

    /**
     * @brief Calcula el coste de la solucion.
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @return coste Float El coste de la solución.
     */
    float calcularCoste(boolean reincicializacion) {

        float coste = 0.0f;
        Object[] sol = new Object[1];
        
        if(reincicializacion == false){
            sol=  _mejorSolucion.toArray();
        }else{
            sol = _solucion.toArray();
        }

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
    boolean EvaluarSolucion( int eleMenor, Random_p random) {
        boolean mejora = false;
        int mejorCandidato = 0;
        int candidatosEva = 1;
        float Coste= 0.0f;
        float mejorCosteCandidato =0.0f;
        int min=0;
        int max=_archivoDatos.getTama_Matriz()-1;
        Set<Integer> vecindario = new HashSet<>();
        
        while(candidatosEva <10){
            int i = random.Randint(0, _archivoDatos.getTama_Matriz()-1);
                    
            if(!_solucion.contains(i)&& !_memoriaCortoPlazo.contains(i) &&
                    !vecindario.contains(i)){
                
                Coste = CosteFactorizado(eleMenor, i);
                vecindario.add(i);
                if(Coste>= mejorCosteCandidato){
                    mejorCosteCandidato = Coste;
                    mejorCandidato = i;
                }
                candidatosEva++;
                        
            }else{
                if(max +1 < _archivoDatos.getTama_Matriz()-1) max++;
            }
            
        }
        
        _costeActual = mejorCosteCandidato;
        Intercambio(eleMenor, mejorCandidato);
        _numIteraciones++;
        
        linea+=" "+ eleMenor+" reemplazado por "+ mejorCandidato+ ", coste actual: " +_costeActual +", mejor coste: "+ _mejorCoste;
        
        //Si resulta mejor nos deplazamos a el
        if (_costeActual <= _mejorCoste) {  
            _numIntentos++;
            linea+=", nº interaciones sin mejora: " + _numIntentos +";";
           
        } else { 
            _numIntentos = 0; 
            _mejorSolucion.clear();
            for(int ele: _solucion){
                int a = ele;
                _mejorSolucion.add(a);
            }
            
            _mejorCoste = _costeActual;
            
            linea+=" Nuevo mejor coste;";
        }
        
        ActualizarMemorias(eleMenor);
        
        mejora = true;
        
        return mejora;
    }
    
    /**
     * @brief Actualiza las memorias de corto y largo plazo
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param elementoTabu elemento sustituido de la solución
     */
    
    void ActualizarMemorias(int elementoTabu){
        
        Object[] sol = _solucion.toArray();

        for (Object elemento : sol) {
            
            int a = (int) elemento;
            _memoriaLargoPlazo.get(a).setVeces();
            
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
        System.out.println("Intensificaciones: " +_numRestartMayor);
        System.out.println("Diversificaciones: " +_numRestartMenor);
        System.out.println("Vector Solución");
        System.out.println(_mejorSolucion);
        float _suma_Resultado = calcularCoste(false);
        System.out.println("Coste de la solución: " + _suma_Resultado);
        
        gestor.escribirArchivo("");
        gestor.escribirArchivo("Resultados");
        gestor.escribirArchivo("Intensificaciones: " +_numRestartMayor);
        gestor.escribirArchivo("Diversificaciones: " +_numRestartMenor);
        gestor.escribirArchivo("Vector Solución: " + _mejorSolucion);
        gestor.escribirArchivo("Coste de la solución: " + _suma_Resultado);

        _solucion.clear();
        _solucion = null;

        System.out.println("");
    }

}
