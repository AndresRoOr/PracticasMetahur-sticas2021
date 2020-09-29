/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author David
 */
public class BusquedaLocal {
    
    public class Vecino implements Comparable<Vecino> {

        private Integer id;
	private Double _contribucion;
        
        public Vecino(Integer id, Double _contribucion) {
            this.id = id;
            this._contribucion = _contribucion;
        }
	 
        @Override
        public int compareTo(Vecino vecino) {
            return (int)(this.getContribucion() - vecino.getContribucion()  );
        }

        public Integer getId(){
            return this.id;
        }
        
        public Double getContribucion() {
            return _contribucion;
        }
        
        public void setContribucion(double cont){
            this._contribucion = cont;
        }
        
        public String toString() {
        return "Key: " + getId() + ", Value: " + getContribucion();
    }
        
    }
    
    Archivo _archivoDatos;
    ArrayList<Integer> _solucion;
    Map<Integer,Vecino> _Vecindario;
    Double _suma_Resultado;
    
    
    Integer _evaluciones; //Número de evaluaciones máximas
    Integer _numEvaluciones; //Número de evaluaciones actuales
    
    public BusquedaLocal(Archivo archivoDatos, Integer evaluaciones){
        _archivoDatos=archivoDatos;
        _solucion=new ArrayList<>();
        _Vecindario = new HashMap<>();
        _suma_Resultado=0.0;
        _evaluciones = evaluaciones;
        _numEvaluciones = 0;
    }
    
    
    void busquedaLocal(Random aleatorioSemilla){
        
        generearSolucionAleatoria(aleatorioSemilla);
        
        for(int i : _solucion){
            calcularContribucion(i);
        }
        
        List<Vecino> VecinosByCont = new ArrayList<>(_Vecindario.values());
	Collections.sort(VecinosByCont);
        System.out.print(VecinosByCont);
        
        int intentosReeemplazo = 0;
        
        int candidato = 0;
        
        int maxPosi = _archivoDatos.getTama_Matriz() -_archivoDatos.getTama_Solucion();
        
        while( _numEvaluciones < _evaluciones ){
            
            Integer reemplazar = VecinosByCont.get(0).getId();
            
            boolean exito = false;
            if (!_solucion.contains(candidato)){
                
                
                ArrayList<Integer> p = _solucion;

                exito = Intercambio(p, reemplazar, candidato);
                
            }
            
            candidato ++;
            
            if( exito == false && candidato == maxPosi){
                _Vecindario.remove(reemplazar);
                VecinosByCont.remove(0);
                candidato = 0;
            }
        }     
        
    }
    
    void generearSolucionAleatoria(Random _aleatorioSemilla){
        
        while(_solucion.size() < _archivoDatos.getTama_Solucion()){
            
            Integer candidato = _aleatorioSemilla.nextInt(_archivoDatos.getTama_Matriz());
            
            if(!_solucion.contains(candidato)) _solucion.add(candidato);
        
        }
        
    }
   
    
    void calcularContribucion(int vecino){
        
        Double suma = _suma_Resultado;

        int pos = _solucion.indexOf(vecino);
        
        for (int i = 0; i < _solucion.size() - 1; i++) {
            if(i != pos) suma += _archivoDatos.getMatriz()[_solucion.get(i)][_solucion.get(pos)];
            
        }

        _Vecindario.put(vecino, new Vecino(vecino, suma));
        
    }
    
    boolean Intercambio(ArrayList<Integer> S, Integer i, Integer j){
        
        double Acoste = 0.0;
        double Icoste = 0.0;
        double JCoste = 0.0;
        int pos = 0;
        int p = 0;
        
        System.out.println("Evaluando: "+ i + " " +j);
        
        for( Integer sel : S){
            
            if(sel!= i){
                Icoste+=_archivoDatos.getMatriz()[sel][i];
                JCoste+=_archivoDatos.getMatriz()[sel][j];
                pos++;
            }else{
                p = pos;
            }
            
        }
        
        Acoste = JCoste-Icoste;
        
        _numEvaluciones++;
        
        if(Acoste>0.0){
            
            S.set(pos, j);
            _suma_Resultado+=Acoste;
            _solucion = S;
            return true;
        }
        
        
        
        return false;
    }
    
    void PresentarResultados() {
        System.out.println("Vector Solución");
        System.out.println(_solucion);
        System.out.println("Coste de la solución: " + _suma_Resultado);

        _solucion = null;
        _archivoDatos.setMatriz(null);
        _Vecindario  = null;

        System.out.println("");
    }
    
}
