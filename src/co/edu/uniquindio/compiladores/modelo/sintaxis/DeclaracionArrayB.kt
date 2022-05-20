package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class DeclaracionArrayB : Sentencia {

    var nombre: String
    var tipoVariable: String
    var tipoDato: TipoArrayB

    constructor(nombre: String, tipoVariable: String, tipoDato: TipoArrayB) {
        this.nombre = nombre
        this.tipoVariable = tipoVariable
        this.tipoDato = tipoDato
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Declaración ArrayB")
        if (nombre != null && tipoVariable != null && tipoDato != null) {
            raiz.children.add(TreeItem("nombreVar: "+nombre+", "+"tipoVar: "+tipoVariable+", "+"tipoDato: "+tipoDato.tipo))
        }
        return raiz
    }
}