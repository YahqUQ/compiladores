package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

abstract class Sentencia{

    abstract fun getArbolVisual():TreeItem<String>
    abstract fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String)
    abstract fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String)


}
