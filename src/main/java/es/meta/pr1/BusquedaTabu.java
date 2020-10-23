/** @file    BusquedaLocal.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 03/10/2020
 */
package es.meta.pr1;

import static java.lang.Math.log;
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
    public class ElementoSolucion implements Comparable<ElementoSolucion> {

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

            if (comparativa < 0) {
                return -1;
            } else if (comparativa > 0) {
                return 1;
            } else {
                return 0;
            }

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
            this._vecesSolucion++;
            this._contribucion = (float) this._vecesSolucion;
        }

        public String toString() {
            return "Key: " + getId() + ", Value: " + getContribucion();
        }

    }

    Archivo _archivoDatos;///<Contiene los datos del problema
    Set<Integer> _solucionMomento;
    Set<Integer> _solucionElite;///<Almacena el conjunto solución
    float _costeSolucionMomento;///<Almacena el coste de la solucion durante la ejecución
    float _costeSolucionElite;///<Almacena el coste de la mejor solución obtenida
    int _limiteIteraciones;///<Número de evaluaciones máximas
    long _iteracionesRealizadas;///<Número de evaluaciones actuales
    int _limiteSinMejora;///<Número de intentos de mejora máximos
    int _iteracionesSinMejora;///<Número de intentos de mejora actuales
    int _tenenciaTabu;///<Tamaño de la memoria de corto plazo
    ArrayList<ElementoSolucion> _memoriaLargoPlazo;///<Almacena la frecuencia de
    // aparición de los elementos en la solución
    LinkedList<Integer> _listaTabu;///<Almacena los últimos movimientos
    //de intercambio
    int _numRestartMenor;///<Número de reinicializaciones de intensificación
    int _numRestartMayor;///<Número de reinicializaciones de exploración
    GestorLog gestor;
    String linea = "";

    ArrayList<ElementoSolucion> _listaAportes;

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
        _solucionMomento = new HashSet<>();
        _solucionElite = new HashSet<>();
        _limiteIteraciones = iteraciones;
        _iteracionesRealizadas = 0;
        _limiteSinMejora = Intentos;
        _iteracionesSinMejora = 0;
        _tenenciaTabu = tenenciaTabu;
        _costeSolucionMomento = 0.0f;
        _costeSolucionElite = 0.0f;

        gestor = g;

        _listaAportes = new ArrayList<>();

        _memoriaLargoPlazo = new ArrayList<>();
        _listaTabu = new LinkedList<>();
        for (int i = 0; i < _tenenciaTabu; i++) {
            _listaTabu.addLast(-1);
        }
        for (int i = 0; i < _archivoDatos.getTama_Matriz(); i++) {
            _memoriaLargoPlazo.add(new ElementoSolucion(i, 0));
        }

        _numRestartMayor = 0;
        _numRestartMenor = 0;
    }

    void busquedaTabu(Random_p aleatorioSemilla) {

        int elementoMenor;
        ArrayList<Integer> vecindario = null;
        int tamanioVecindario;
        Pair mejorVecino = null;

        GeneraSolucionAleatoria(aleatorioSemilla);
        _solucionMomento = new HashSet<>(_solucionElite);
        _costeSolucionElite = CalcularCosteElite();
        _costeSolucionMomento = _costeSolucionElite;

        gestor.escribirArchivo("Solución inicial: " + _solucionMomento);
        gestor.escribirArchivo("");
        gestor.escribirArchivo("Coste: " + _costeSolucionMomento);
        gestor.escribirArchivo("");

        while (_iteracionesRealizadas < _limiteIteraciones) {

            linea = "";
            gestor.escribirArchivo("-----Iteración nº " + _iteracionesRealizadas + "-----");

            elementoMenor = CalcularAportes();
            tamanioVecindario = CalculaTamanioVecindario();
            vecindario = GeneraVecindarioRestringido(tamanioVecindario, aleatorioSemilla);
            mejorVecino = EvaluaVecindarioRestringido(vecindario, elementoMenor);
            Intercambio(elementoMenor, mejorVecino.getCandidato());
            _costeSolucionMomento = mejorVecino.getCoste();

            linea += " " + elementoMenor + " reemplazado por " + mejorVecino + ", coste actual: " + _costeSolucionMomento + ", mejor coste: " + _costeSolucionElite;

            ActualizarMemorias(mejorVecino.getCandidato());

            if (_costeSolucionMomento > _costeSolucionElite) {
                _solucionElite = new HashSet<>(_solucionMomento);
                _costeSolucionElite = _costeSolucionMomento;
                _iteracionesSinMejora = 0;
                
                linea+=" Nuevo mejor coste;";
                
            } else {
                _iteracionesSinMejora++;

                linea += " nº interaciones sin mejora: " + _iteracionesSinMejora + ";";

            }

            EliminarMasAntiguo();
            _iteracionesRealizadas++;
            _listaAportes.clear();

            if (_iteracionesSinMejora > _limiteSinMejora) {
                ReinicializarBusqueda(aleatorioSemilla);
            }

            gestor.escribirArchivo(linea);

        }

        System.out.println("COSTE: " + _costeSolucionElite);

    }

    /**
     * @brief Genera la solución incial
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param aleatorioSemilla Random Semilla generada aleatoriamente
     */
    void GeneraSolucionAleatoria(Random_p _aleatorioSemilla) {

        while (_solucionMomento.size() < _archivoDatos.getTama_Solucion()) {

            Integer candidato = _aleatorioSemilla.Randint(0, _archivoDatos.getTama_Matriz() - 1);

            if (!_solucionMomento.contains(candidato)) {
                _solucionMomento.add(candidato);
            }

        }

    }

    ArrayList<Integer> GeneraVecindarioRestringido(int tamanioVecindario, Random_p semilla) {
        int vecino = 0;
        ArrayList<Integer> vecindario = new ArrayList<>();

        while (vecindario.size() < tamanioVecindario) {
            vecino = semilla.Randint(0, _archivoDatos.getTama_Matriz() - 1);
            if ((!_solucionMomento.contains(vecino)) && (!_listaTabu.contains(vecino)) && (!vecindario.contains(vecino))) {
                vecindario.add(vecino);
            }
        }
        return vecindario;
    }

    Pair EvaluaVecindarioRestringido(ArrayList<Integer> vecindario, int elementoMenor) {
        float costeMax = 0.0f;
        int mejorVecino = -1;

        for (int i = 0; i < vecindario.size(); i++) {
            if (CosteFactorizado(elementoMenor, vecindario.get(i)) > costeMax) {
                costeMax = CosteFactorizado(elementoMenor, vecindario.get(i));
                mejorVecino = vecindario.get(i);
            }
        }
        return new Pair(mejorVecino, costeMax);
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

        Iterator<Integer> iterator = _solucionMomento.iterator();

        while (iterator.hasNext()) {

            int k = iterator.next();

            costeMenos += _archivoDatos.getMatriz()[k][i];

            if (k != i) {
                costeMas += _archivoDatos.getMatriz()[k][j];
            }

        }

        return (_costeSolucionMomento - costeMenos + costeMas);
    }

    int CalcularAportes() {

        float aporte = 0.0f;
        Iterator<Integer> iterator = _solucionMomento.iterator();

        while (iterator.hasNext()) {

            Iterator<Integer> iterator2 = _solucionMomento.iterator();
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
        return _listaAportes.get(0).getId();
    }

    void EliminarMasAntiguo() {
        if (_listaTabu.size() > _tenenciaTabu) {
            _listaTabu.pop();
        }
    }

    float CalcularCosteElite() {

        float coste = 0.0f;
        Object[] sol = _solucionElite.toArray();

        for (int i = 0; i < sol.length - 1; i++) {
            int a = (int) sol[i];
            for (int j = i + 1; j < sol.length; j++) {
                int b = (int) sol[j];
                coste += _archivoDatos.getMatriz()[a][b];
            }
        }

        return coste;

    }

    float CalcularCosteMomento() {

        float coste = 0.0f;
        Object[] sol = _solucionMomento.toArray();

        for (int i = 0; i < sol.length - 1; i++) {
            int a = (int) sol[i];
            for (int j = i + 1; j < sol.length; j++) {
                int b = (int) sol[j];
                coste += _archivoDatos.getMatriz()[a][b];
            }
        }

        return coste;

    }

    int CalculaTamanioVecindario() {
        int tamanioVecindario = (int) Math.exp((_limiteIteraciones - _iteracionesRealizadas) / ((_limiteIteraciones / log(_archivoDatos.getTama_Solucion()))));

        if (tamanioVecindario < 10) {
            tamanioVecindario = (int) (_archivoDatos.getTama_Solucion());
        }

        return tamanioVecindario;
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
        _solucionMomento.remove(i);
        _solucionMomento.add(j);

    }

    /**
     * @brief Actualiza las memorias de corto y largo plazo
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param elementoTabu elemento sustituido de la solución
     */
    void ActualizarMemorias(int elementoTabu) {

        Object[] sol = _solucionMomento.toArray();

        for (Object elemento : sol) {

            int a = (int) elemento;
            _memoriaLargoPlazo.get(a).setVeces();

        }

        _listaTabu.push(elementoTabu);
        _listaTabu.pop();
    }

    void ReinicializarBusqueda(Random_p aleatorioSemilla) {
        float limiteExploracion;
        float factorAleatorio;

        limiteExploracion = GeneraLimiteExploracion();
        factorAleatorio = (float) aleatorioSemilla.Randfloat(0, 1);

        if (factorAleatorio < limiteExploracion) {
            Intensificacion();
        } else {
            Diversificacion();
        }
        _listaTabu.clear();
        _iteracionesSinMejora = 0;
    }

    float GeneraLimiteExploracion() {
        return (float) _iteracionesRealizadas / _limiteIteraciones;
    }

    void Intensificacion() {
        _solucionMomento.clear();
        _costeSolucionMomento = 0;

        ArrayList<ElementoSolucion> aux = new ArrayList<>();
        int i = 0;
        for (ElementoSolucion ele : _memoriaLargoPlazo) {
            i = ele.getId();
            ElementoSolucion f = new ElementoSolucion(i, ele.getVeces());
            aux.add(f);
        }

        Collections.sort(aux);

        while (_solucionMomento.size() < _archivoDatos.getTama_Solucion()) {
            _solucionMomento.add(aux.get(aux.size() - 1).getId());

            Object[] sol = _solucionMomento.toArray();

            for (Object elemento : sol) {

                int a = (int) elemento;
                _memoriaLargoPlazo.get(a).setVeces();

            }

            aux.remove(aux.size() - 1);
        }

        _costeSolucionMomento = CalcularCosteMomento();
        
        linea+=", coste actual: "  +_costeSolucionMomento +", mejor coste: "+ _costeSolucionElite;

        if (_costeSolucionMomento > _costeSolucionElite) {
            _solucionElite = new HashSet<>(_solucionMomento);
            _costeSolucionElite = _costeSolucionMomento;
            
            linea+=" nuevo mejor coste;";
        }

        linea += " Intensificación ";
        _numRestartMayor++;
    }

    void Diversificacion() {
        _solucionMomento.clear();
        _costeSolucionMomento = 0;

        ArrayList<ElementoSolucion> aux = new ArrayList<>();
        int i = 0;
        for (ElementoSolucion ele : _memoriaLargoPlazo) {
            i = ele.getId();
            ElementoSolucion f = new ElementoSolucion(i, ele.getVeces());
            aux.add(f);
        }

        Collections.sort(aux);

        while (_solucionMomento.size() < _archivoDatos.getTama_Solucion()) {
            _solucionMomento.add(aux.get(0).getId());

            Object[] sol = _solucionMomento.toArray();

            for (Object elemento : sol) {

                int a = (int) elemento;
                _memoriaLargoPlazo.get(a).setVeces();

            }

            aux.remove(0);
        }

        _costeSolucionMomento = CalcularCosteMomento();
        
        linea+=", coste actual: "  +_costeSolucionMomento +", mejor coste: "+ _costeSolucionElite;

        if (_costeSolucionMomento > _costeSolucionElite) {
            _solucionElite = new HashSet<>(_solucionMomento);
            _costeSolucionElite = _costeSolucionMomento;
            
            linea+=" nuevo mejor coste;";
        }

        linea += " Intensificación ";
        _numRestartMayor++;
    }
    
    /**
     * @brief Muestra por pantalla los datos de la solución
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     */
    void PresentarResultados() {
        System.out.println("Intensificaciones: " + _numRestartMayor);
        System.out.println("Diversificaciones: " + _numRestartMenor);
        System.out.println("Vector Solución");
        System.out.println(_solucionElite);
        float _suma_Resultado = CalcularCosteElite();
        System.out.println("Coste de la solución: " + _suma_Resultado);

        gestor.escribirArchivo("");
        gestor.escribirArchivo("Resultados");
        gestor.escribirArchivo("Intensificaciones: " + _numRestartMayor);
        gestor.escribirArchivo("Diversificaciones: " + _numRestartMenor);
        gestor.escribirArchivo("Vector Solución: " + _solucionElite);
        gestor.escribirArchivo("Coste de la solución: " + _suma_Resultado);

        _solucionMomento.clear();
        _solucionMomento = null;

        System.out.println("");
    }
}
