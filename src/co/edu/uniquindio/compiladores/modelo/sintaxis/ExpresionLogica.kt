package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class ExpresionLogica: Expresion {

    var valor: String?=null
    var expresionLogica: ExpresionLogica?=null
    var expresionLogicaIzq: ExpresionLogica?=null
    var operadorLogico: String?=null
    var expresionLogicaDer: ExpresionLogica?=null

    constructor(valor: String){
        this.valor=valor
    }
    constructor(expresionLogica: ExpresionLogica){
        this.expresionLogica=expresionLogica
    }

    constructor(operadorLogico:String ,expresionLogica: ExpresionLogica){
        this.operadorLogico=operadorLogico
        this.expresionLogica=expresionLogica
    }
    constructor(expresionLogicaIzq: ExpresionLogica,operadorLogico: String,expresionLogicaDer: ExpresionLogica){
        this.expresionLogicaIzq=expresionLogicaIzq
        this.operadorLogico=operadorLogico
        this.expresionLogicaDer=expresionLogicaDer
    }

    override fun getArbolVisual(): TreeItem<String> {
       var raiz= TreeItem("Expresión Lógica")

        if(valor!=null){
            raiz.children.add(TreeItem("valor: "+valor))

        }else if(expresionLogica!=null&&operadorLogico!=null){
            raiz.children.add(TreeItem("operador: "+operadorLogico))
            raiz.children.add(expresionLogica!!.getArbolVisual())

        }else if(expresionLogica!=null&&operadorLogico==null){
            raiz.children.add(expresionLogica!!.getArbolVisual())

        }else if(expresionLogicaIzq!=null&& operadorLogico!=null&&expresionLogicaDer!=null){
            raiz.children.add(expresionLogicaIzq!!.getArbolVisual())
            raiz.children.add(TreeItem("operador: "+operadorLogico))
            raiz.children.add(expresionLogicaDer!!.getArbolVisual())

        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

}
