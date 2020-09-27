/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author andresrojasortega
 */
public class Greedy {
    Archivo _archivoDatos;
    ArrayList<Integer> _solucion;
    Double _suma_Resultado;
    
    public Greedy(Archivo archivoDatos){
        _archivoDatos=archivoDatos;
        _solucion=new ArrayList<>();
        _suma_Resultado=0.0;
    }
    
    void greedy(Random aleatorioSemilla) {

        //Generación del primer elemento
        _solucion.clear();
        Integer sol_Inicial = aleatorioSemilla.nextInt(_archivoDatos.getTama_Matriz());
        _solucion.add(sol_Inicial);

        while (_solucion.size() < _archivoDatos.getTama_Solucion()) {

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

            if (candidato != -1) {
                _solucion.add(candidato);
                _suma_Resultado = max;
            }

        }

    }

    void PresentarResultados() {
        System.out.println("Vector Solución");
        System.out.println(_solucion);
        System.out.println("Coste de la solución: " + _suma_Resultado);

        _solucion = null;
        _archivoDatos.setMatriz(null);

        System.out.println("");
    }

    Double calculoSolucionParcial() {

        Double suma = _suma_Resultado;

        for (int i = 0; i < _solucion.size() - 1; i++) {
            suma += _archivoDatos.getMatriz()[_solucion.get(i)][_solucion.get(_solucion.size() - 1)];
        }

        return suma;
    }

    Double calculoValorSolucion() {

        Double suma = 0.0;
        for (int i = 0; i < _solucion.size(); i++) {
            for (int j = i + 1; j < _solucion.size(); j++) {

                suma += _archivoDatos.getMatriz()[_solucion.get(i)][_solucion.get(j)];
                //System.out.println("Caculado: " + _solucion.get(i) +" " + _solucion.get(j) );
            }
        }

        //System.out.println(suma);
        return suma;
    }
}
