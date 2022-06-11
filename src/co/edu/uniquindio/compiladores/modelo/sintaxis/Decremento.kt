package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo

import javafx.scene.control.TreeItem

class Decremento : Sentencia {

    var variable:Token

    constructor(variable: Token){
        this.variable=variable
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Decremento")

        if(variable!=null){
            raiz.children.add(TreeItem("variable: "+variable.lexema))
        }
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

        var simbolo= tablaSimbolos.buscarSimboloVariable(variable.lexema,ambito)

        if(simbolo==null){
            tablaSimbolos.listaErrores.add(Error("Variable no existente",variable))
        }else if(simbolo.tipo!="NUMBER_Z"&&simbolo.tipo!="NUMBER_F"){
            tablaSimbolos.listaErrores.add(Error("Operador decremento solo válido para variable numericas",variable))

        }

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

}
