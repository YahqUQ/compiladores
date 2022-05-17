package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class ExpresionArtimetica:Expresion {

    lateinit var expresionA:String
    lateinit var expresionArtimetica: ExpresionArtimetica
    lateinit var expresionArtimeticaIzq: ExpresionArtimetica
    lateinit var operadorArtimetico: String
    lateinit var expresionAritmeticaDer: ExpresionArtimetica

    constructor(expresionArtimetica: String){
        expresionA=expresionArtimetica
    }
    constructor(expresionArtimetica: ExpresionArtimetica){
        this.expresionArtimetica=expresionArtimetica
    }
    constructor(expresionArtimeticaIzq: ExpresionArtimetica,operadorArtimetico: String,expresionAritmeticaDer: ExpresionArtimetica){
        this.expresionArtimeticaIzq=expresionArtimeticaIzq
        this.operadorArtimetico=operadorArtimetico
        this.expresionAritmeticaDer=expresionAritmeticaDer
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Expresion Aritmética")

        if(expresionA!=null){
            raiz.children.add(TreeItem("valor:"+ expresionA))
        }else if(expresionArtimetica!=null){
            raiz.children.add(expresionArtimetica.getArbolVisual())
        }else if(expresionArtimeticaIzq!=null&&expresionAritmeticaDer!=null&&operadorArtimetico!=null){
            raiz.children.add(expresionArtimeticaIzq.getArbolVisual())
            raiz.children.add(TreeItem("operador: "+operadorArtimetico))
            raiz.children.add(expresionAritmeticaDer.getArbolVisual())
        }

        return raiz
    }

}
