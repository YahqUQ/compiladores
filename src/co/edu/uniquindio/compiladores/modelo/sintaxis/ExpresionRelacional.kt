package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class ExpresionRelacional:Expresion {

    var valor: String?=null
    var expresionRelacionalIzq: ExpresionRelacional?=null
    var operadorRelacional: String?=null
    var expresionDer: Expresion?=null

    constructor(expresionRelacional: String){
        this.valor=expresionRelacional
    }

    constructor(expresionRelacionalIzq: ExpresionRelacional,operadorRelacional:String,expresionDer: Expresion){
        this.expresionRelacionalIzq=expresionRelacionalIzq
        this.operadorRelacional=operadorRelacional
        this.expresionDer=expresionDer
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Expresión Relacional")

        if(valor!=null){
            raiz.children.add(TreeItem("valor: "+valor))
        }else if(expresionRelacionalIzq!=null&&operadorRelacional!=null&&expresionDer!=null){
            raiz.children.add(expresionRelacionalIzq!!.getArbolVisual())
            raiz.children.add(TreeItem("operador"+operadorRelacional))
            raiz.children.add(expresionDer!!.getArbolVisual())
        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

}
