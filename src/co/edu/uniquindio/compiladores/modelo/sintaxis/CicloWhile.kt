package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.Simbolo
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class CicloWhile : Sentencia {

    var condicionL:ExpresionLogica?= null
    var condicionR:ExpresionRelacional?=null
    var listaSentencias:ArrayList<Sentencia>

    constructor(condicion:ExpresionLogica,listaSentencias: ArrayList<Sentencia>){
        this.condicionL=condicion
        this.listaSentencias=listaSentencias
    }

    constructor(condicion:ExpresionRelacional,listaSentencias: ArrayList<Sentencia>){
        this.condicionR=condicion
        this.listaSentencias=listaSentencias
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Ciclo While")

        if(condicionL!=null){
            raiz.children.add(condicionL!!.getArbolVisual())

            val sentencias = TreeItem("Sentencias")
            raiz.children.add(sentencias)

            if(listaSentencias!=null) {
                for (sentencia in listaSentencias) {
                    sentencias.children.add(sentencia.getArbolVisual())
                }
            }
        }else if(condicionR!=null){
            raiz.children.add((condicionR!!.getArbolVisual()))

            val sentencias = TreeItem("Sentencias")
            raiz.children.add(sentencias)
            if(listaSentencias!=null) {
                for (sentencia in listaSentencias) {
                    sentencias.children.add(sentencia.getArbolVisual())
                }
            }
        }
        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        for(sentencia:Sentencia in listaSentencias){
            sentencia.llenarTablaSimbolos(tablaSimbolos,ambito+"While:"+condicionL!!+condicionR!!+"/")
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

        for(sentencia:Sentencia in listaSentencias){
            sentencia.analizarSemantica(tablaSimbolos,ambito+"While:"+condicionL!!+condicionR!!+"/")
        }
    }


}
