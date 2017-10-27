package org.example.asteroides;

import java.util.Vector;

/**
 * Created by jay on 10/19/17.
 */
public interface AlmacenPuntuaciones {
    void guardarPuntuacion(int puntos, String nombre, long fecha);
    Vector<String> listaPuntuaciones(int cantidad);
}