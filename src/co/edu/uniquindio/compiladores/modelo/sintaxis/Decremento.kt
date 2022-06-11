package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class Decremento : Sentencia {

    var variable:String

    constructor(variable: String){
        this.variable=variable
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Decremento")

        if(variable!=null){
            raiz.children.add(TreeItem("variable: "+variable))
        }
        return raiz
    }

}
