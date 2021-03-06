/**
 * @file    ElementoSolucion.java
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @version 1.0
 * @date 02/11/2020
 */
package es.meta.pr1;

/**
 * @brief Clase que almacena la información de cada elemento que forma parte de
 * la solución
 * @class ElementoSolucion
 * @author Andrés Rojas Ortega
 * @author David Díaz Jiménez
 * @date 02/11/2020
 */
public class ElementoSolucion implements Comparable<ElementoSolucion> {

    ///Atributos de la clase:
    private int id;///<Indica el elemento de la solución que representa
    private float _contribucion;///<Coste que aporta a la solución
    private int _vecesSolucion;///<Nº de veces que un elemento ha formado parte
    ///de la solución

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

    /**
     * @brief Constructor parametrizado de la clase ElementoSolucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 03/10/2020
     * @param id Integer
     * @param veces int
     */
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

    /**
     * @brief Metodo getter del atributo id
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 02/1/2020
     * @return id int
     */
    public int getId() {
        return this.id;
    }

    /**
     * @brief Metodo getter del atributo _contribucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 02/11/2020
     * @return _contribucion float
     */
    public float getContribucion() {
        return this._contribucion;
    }

    /**
     * @brief Metodo getter del atributo _vecesSolucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 02/11/2020
     * @return _vecesSolucion int
     */
    public int getVeces() {
        return this._vecesSolucion;
    }

    /**
     * @brief Metodo setter del atributo _contribucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 02/11/2020
     * @param cont float
     */
    public void setContribucion(float cont) {
        this._contribucion = cont;
    }

    /**
     * @brief Actualiza el número de veces que el elemento forma parte de una
     * solucion
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 02/11/2020
     */
    public void setVeces() {
        this._vecesSolucion++;
        this._contribucion = (float) this._vecesSolucion;
    }

    /**
     * @brief Metodo toString de la clase
     * @author Andrés Rojas Ortega
     * @author David Díaz Jiménez
     * @date 02/11/2020
     * @return Devuelve la información de la clase en una cadena String
     */
    public String toString() {
        return "Key: " + getId() + ", Value: " + getContribucion();
    }

}
