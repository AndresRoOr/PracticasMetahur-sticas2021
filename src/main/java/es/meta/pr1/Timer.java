/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

/**
 *
 * @author David
 */
public class Timer {

    private long inicio;
    private long fin;
    
    public Timer() {
        
        this.fin =0;
        this.inicio = 0;      
    }
    
    void startTimer(){
        this.inicio = System.currentTimeMillis();
    }
    
    double stopTimer(){
        this.fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio));
        
        return tiempo;
    }
    
    
}
