package co.edu.uniquindio.compiladores.modelo.sintaxis
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

abstract class Elemento {

    abstract fun getArbolVisual():TreeItem<String>

    abstract fun llenarTablaSimbolos(tablaSimbolo: TablaSimbolo)

    abstract fun analizarSemantica(tablaSimbolo: TablaSimbolo)

}
