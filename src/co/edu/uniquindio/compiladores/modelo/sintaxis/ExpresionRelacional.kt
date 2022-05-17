package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class ExpresionRelacional:Expresion {

    lateinit var valor: String
    lateinit var expresionRelacionalIzq: ExpresionRelacional
    lateinit var operadorRelacional: String
    lateinit var expresionDer: Expresion

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
            raiz.children.add(expresionRelacionalIzq.getArbolVisual())
            raiz.children.add(TreeItem("operador"+operadorRelacional))
            raiz.children.add(expresionDer.getArbolVisual())
        }

        return raiz
    }

}
