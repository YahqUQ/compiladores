package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

abstract class Elemento {

    abstract fun getArbolVisual():TreeItem<String>

}
