package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem
import sun.reflect.generics.tree.Tree

class ExpresionCadena:Expresion {

    var valor:String?=null
    var expresionCadenaDer:ExpresionCadena?=null
    var expresionCadenaIzq:ExpresionCadena?=null

    constructor(expresionCadena: String){
        this.valor=expresionCadena
    }
    constructor(expresionCadenaIzq: ExpresionCadena,expresionCadenaDer: ExpresionCadena){
        this.expresionCadenaIzq=expresionCadenaIzq
        this.expresionCadenaDer=expresionCadenaDer
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Expresión Cadena")

        if(valor!=null){
            raiz.children.add(TreeItem("valor: "+valor))

        }else if(expresionCadenaDer!=null&&expresionCadenaIzq!=null){
            raiz.children.add(expresionCadenaIzq!!.getArbolVisual())
            raiz.children.add(expresionCadenaDer!!.getArbolVisual())

        }
        return raiz
    }

}
