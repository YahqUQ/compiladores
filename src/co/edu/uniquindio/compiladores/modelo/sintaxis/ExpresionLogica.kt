package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class ExpresionLogica: Expresion {

    lateinit var valor: String
    lateinit var expresionLogica: ExpresionLogica
    lateinit var expresionLogicaIzq: ExpresionLogica
    lateinit var operadorLogico: String
    lateinit var expresionLogicaDer: ExpresionLogica

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
            raiz.children.add(expresionLogica.getArbolVisual())

        }else if(expresionLogica!=null&&operadorLogico==null){
            raiz.children.add(expresionLogica.getArbolVisual())

        }else if(expresionLogicaIzq!=null&& operadorLogico!=null&&expresionLogicaDer!=null){
            raiz.children.add(expresionLogicaIzq.getArbolVisual())
            raiz.children.add(TreeItem("operador: "+operadorLogico))
            raiz.children.add(expresionLogicaDer.getArbolVisual())

        }

        return raiz
    }

}
