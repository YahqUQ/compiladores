package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

abstract class Expresion {

    abstract fun getArbolVisual(): TreeItem<String>
    abstract fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String)


}
