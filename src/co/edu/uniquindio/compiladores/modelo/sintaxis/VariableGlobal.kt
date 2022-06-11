package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class VariableGlobal: Elemento {

    var decAsignacionVariable:DeclaracionAsignacionVariable?=null

    constructor(declaracionAsignacionVariable: DeclaracionAsignacionVariable){
        this.decAsignacionVariable=declaracionAsignacionVariable
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Variable Global")

        if(decAsignacionVariable!=null){
            raiz.children.add(decAsignacionVariable!!.getArbolVisual())
        }
        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo) {
        decAsignacionVariable?.llenarTablaSimbolos(tablaSimbolos,"UnidadCompilacion/")
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo) {
        decAsignacionVariable?.analizarSemantica(tablaSimbolos,"UnidadCompilacion/")
    }

}
