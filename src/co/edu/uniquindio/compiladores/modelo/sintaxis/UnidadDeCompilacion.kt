package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
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

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo) {
        for (e in listaElementos) {
            e.llenarTablaSimbolos(tablaSimbolos)
        }
    }
    fun analizarSemantica(tablaSimbolos: TablaSimbolo) {
        for (e in listaElementos) {
            e.analizarSemantica(tablaSimbolos)
        }
    }

}
