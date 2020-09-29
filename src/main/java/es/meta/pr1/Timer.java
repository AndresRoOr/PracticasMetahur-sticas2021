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

    private long _inicio;
    private long _fin;
    
    public Timer() {
        
        this._fin =0;
        this._inicio = 0;      
    }
    
    void startTimer(){
        this._inicio = System.currentTimeMillis();
    }
    
    double stopTimer(){
        this._fin = System.currentTimeMillis();
        double tiempo = (double) ((_fin - _inicio));
        
        return tiempo;
    }
    
    
}
