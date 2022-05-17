package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import javafx.scene.control.TreeItem

class TipoArray {

    var tipo:String

    constructor(tipoArra: String) {
        this.tipo=tipoArra
    }

    fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Tipo Array")
        if (tipo!= null) {
            raiz.children
                .add( TreeItem( tipo))
        }

        return raiz
    }
}
