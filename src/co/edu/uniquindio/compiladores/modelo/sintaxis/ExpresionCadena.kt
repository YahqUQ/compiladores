package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem


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

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

}
