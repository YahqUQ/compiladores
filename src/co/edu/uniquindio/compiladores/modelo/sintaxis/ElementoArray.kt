package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class ElementoArray(categoria: String, valor: String) {

    var categoria: String = categoria
    var valor: String = valor

    fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Elemento")
        if (this.categoria != null) {
            raiz.children.add(TreeItem("Categoria: " + this.categoria))
            raiz.children.add(TreeItem("Valor: " + this.valor))
        }
        return raiz
    }


}