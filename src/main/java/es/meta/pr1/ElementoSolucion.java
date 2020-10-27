/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.meta.pr1;

/**
 *
 * @author andresro
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