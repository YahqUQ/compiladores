package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
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

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

}
