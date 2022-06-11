package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class Incremento : Sentencia {

    var id: String

    constructor(variable: String){
        this.id=variable
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Incremento")
        if(id!=null){
            raiz.children.add(TreeItem("variable: "+id))
        }

        return raiz
    }

}
