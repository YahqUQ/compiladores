package co.edu.uniquindio.compiladores.modelo.sintaxis

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

}
