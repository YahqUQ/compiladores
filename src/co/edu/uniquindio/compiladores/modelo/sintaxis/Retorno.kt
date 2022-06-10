package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class Retorno : Sentencia {

    var expresion:Expresion?=null
    var identificador:String?=null

    constructor( expresion: Expresion){
        this.expresion=expresion
    }
    constructor( identificador: String){
        this.identificador=identificador
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Retorno")

        if(expresion!=null){
            raiz.children.add(expresion!!.getArbolVisual())
        }else if(identificador!= null){
            raiz.children.add(TreeItem("id Variable Retorno: "+identificador))
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


