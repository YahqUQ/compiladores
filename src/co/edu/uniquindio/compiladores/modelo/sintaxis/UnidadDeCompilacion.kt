package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class UnidadDeCompilacion {

    var listaElementos:ArrayList<Elemento>

    constructor(listaElementos: ArrayList<Elemento>){
        this.listaElementos=listaElementos
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Unidad de compilación")
        for (elemento in listaElementos) {
            raiz.children.add(elemento.getArbolVisual())
        }
        return raiz
    }



}
