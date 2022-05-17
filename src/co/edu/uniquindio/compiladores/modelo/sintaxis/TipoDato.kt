package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import javafx.scene.control.TreeItem

class TipoDato {


    var tipo:String

    constructor(tipoDato: String) {
        this.tipo=tipoDato
    }

    fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Tipo Dato")
        if (tipo!= null) {
            raiz.children
                .add( TreeItem( tipo))
        }

        return raiz
    }

}
