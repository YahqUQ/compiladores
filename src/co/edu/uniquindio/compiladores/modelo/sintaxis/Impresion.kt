package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class Impresion : Sentencia {

    var expresionCadena:ExpresionCadena

    constructor( expresionCadena:ExpresionCadena){
        this.expresionCadena=expresionCadena
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Impresion")

        if(expresionCadena!=null){
            raiz.children.add(expresionCadena.getArbolVisual())
        }
        return raiz
    }

}


