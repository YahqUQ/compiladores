package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
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

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

}
