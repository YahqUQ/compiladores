package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class Retorno : Sentencia {

    lateinit var expresion:Expresion
    lateinit var identificador:String

    constructor( expresion: Expresion){
        this.expresion=expresion
    }
    constructor( identificador: String){
        this.identificador=identificador
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Retorno")

        if(expresion!=null){
            raiz.children.add(expresion.getArbolVisual())
        }else if(identificador!= null){
            raiz.children.add(TreeItem("id Variable Retorno: "+identificador))
        }
        return raiz
    }

}


