/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author David
 */
public class BusquedaLocal {
    
    public class ElementoSolucion implements Comparable<ElementoSolucion> {

        private Integer id;
	private Float _contribucion;
        
        public ElementoSolucion(Integer id, Float _contribucion) {
            this.id = id;
            this._contribucion = _contribucion;
        }
	 
        @Override
        public int compareTo(ElementoSolucion vecino) {
            return (int)(this.getContribucion() - vecino.getContribucion()  );
        }

        public Integer getId(){
            return this.id;
        }
        
        public Float getContribucion() {
            return _contribucion;
        }
        
        public void setContribucion(float cont){
            this._contribucion = cont;
        }
        
        public String toString() {
        return "Key: " + getId() + ", Value: " + getContribucion();
        }
        
    }
    
    Archivo _archivoDatos;
    Set<Integer> _solucion;
   // Map<Integer,Vecino> _Vecindario;
    Float _suma_Resultado;
    Float _costeActual;
    
    //Contiene el aporte de cada elemento de la solución actual
    ArrayList<ElementoSolucion> _listaAportes;
    
    //Elementos de la solución que no mejoran al intercambiarlos por sus vecinos
    Set<Integer> _integrantesNoMejoran; 

    
    
    Integer _evaluciones; //Número de evaluaciones máximas
    long _numEvaluciones; //Número de evaluaciones actuales
    
    public BusquedaLocal(Archivo archivoDatos, Integer evaluaciones){
        _archivoDatos=archivoDatos;
        _solucion=new HashSet<>();
        //_Vecindario = new HashMap<>();
        _suma_Resultado=0.0f;
        _evaluciones = evaluaciones;
        _numEvaluciones = 0;
        _costeActual = 0.0f;
        _listaAportes = new ArrayList<>();

        _integrantesNoMejoran = new HashSet<>();
    }
    
    /**
     * @brief Metaheuristica que resuelve el problema
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    
    void busquedaLocal(Random aleatorioSemilla){
        
        generearSolucionAleatoria(aleatorioSemilla);
        
        _costeActual = calcularCoste();
        
        int eleMenor =0;
        
        Float costeSoluion = 0.0f;
        
        boolean mejora = true;
        
        while( _numEvaluciones < 500000 && mejora){
            
            mejora = false;
            //calculamos el aporte de todos los elementos de la solución actual
            calcularAportes(); 
            //por cada elemento seleccionado 
            for (int k=0; k<_archivoDatos.getTama_Solucion(); k++){
                //calculamos el elemento de la solución actual que menos aporte
                eleMenor=EleMenorAporte();
                
                //comprobamos el primer vecino que nos mejore
                
                for (int i=0; i<_archivoDatos.getTama_Matriz(); i++){
                    if (!_solucion.contains(i)){
                        
                        //calculamos si mejora la solución actual
                        
                        costeSoluion= CosteFactorizado(eleMenor, i);
                        _numEvaluciones++;
                        

                        //Si resulta mejor nos deplazamos a el
                        
                         if (_costeActual < costeSoluion) {
                            Intercambio (eleMenor,i);

                            //actualizamos el coste de la solucion
                            _costeActual = costeSoluion;
                            mejora=true;
                            _integrantesNoMejoran.clear();
                            break;
                         }else{
                             if(i == _archivoDatos.getTama_Matriz()-1){
                                 _integrantesNoMejoran.add(eleMenor);
                             }
                         }
                    }            
                }
                //Si visitados los vecinos de un elemento mejora pasamos a 
                //valorar a ese nuevo vecindario
                if (mejora){
                    break;
                }
            }
            _listaAportes.clear();
            
            
        }     
        
        _listaAportes = null;
        _integrantesNoMejoran.clear();
        _integrantesNoMejoran = null;
    }
    
    /**
     * @brief Genera la solución incial
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    
    void generearSolucionAleatoria(Random _aleatorioSemilla){
        
        while(_solucion.size() < _archivoDatos.getTama_Solucion()){
            
            Integer candidato = _aleatorioSemilla.nextInt(
                    _archivoDatos.getTama_Matriz());
            
            if(!_solucion.contains(candidato)) _solucion.add(candidato);
        
        }
        
    }
   
    /**
     * @brief Calcula el aporte de cada integrante de la solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     */
    
    void calcularAportes(){
        float aporte = 0.0f;
        Iterator<Integer> iterator = _solucion.iterator();
        
        while(iterator.hasNext()){
            
            Iterator<Integer> iterator2 = _solucion.iterator();
            int i = iterator.next();
            
            while(iterator2.hasNext()){
                
                int j = iterator2.next();
                aporte+= _archivoDatos.getMatriz()[i][j];
                
            }
            
            ElementoSolucion x = new ElementoSolucion(i, aporte);
            _listaAportes.add(x);  
            aporte=0.0f;
        }
        
    }
    
    /**
     * @brief Busca el elemento de menor aporte de la solución actual
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @return Integrantre de lasolucón con menor aporte
     */
    
    int EleMenorAporte(){
        int eleMenor = 0;
        double menor = Double.MAX_VALUE;
        Iterator<ElementoSolucion> iterator = _listaAportes.iterator();
        while(iterator.hasNext()){
            ElementoSolucion i = iterator.next();
            if (!_integrantesNoMejoran.contains(i.getId())){
                if(i.getContribucion() < menor){
                    menor = i.getContribucion();
                    eleMenor = i.getId();
                }
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
    
    float CosteFactorizado (Integer i, Integer j){
        
        float costeMenos=0.0f, costeMas=0.0f;
        
        Iterator<Integer> iterator = _solucion.iterator();
        
        while(iterator.hasNext()){
            
            int k = iterator.next();
            
            costeMenos += _archivoDatos.getMatriz()[k][i];
            
            if (k!=i)
                costeMas += _archivoDatos.getMatriz()[k][j];
            
        }    
        
        return (_costeActual-costeMenos+costeMas);
    }
    
    /**
     * @brief Sustituye el elemnto seleccionado por su candidato
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param i Elemento a sustiuir de la solución actual
     * @param j Candidato a nuevo integrante de la solución
     */
    
    void Intercambio( Integer i, Integer j){
        _solucion.remove(i);
        _solucion.add(j);
    
    }
    
    float calcularCoste(){
    
        float coste = 0.0f;
        Object[] sol = _solucion.toArray();

        for(int i = 0; i < sol.length - 1; i++){
            int a = (int) sol[i];
             for(int j = i + 1; j < sol.length; j++){
                 int b = (int) sol[j];
                 coste+= _archivoDatos.getMatriz()[a][b];
            }
        }
    
        return coste;
        
    }
    
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
