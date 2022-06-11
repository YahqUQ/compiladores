package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.Simbolo
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class Incremento : Sentencia {

    var id: Token

    constructor(variable: Token){
        this.id=variable
    }
    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Incremento")
        if(id!=null){
            raiz.children.add(TreeItem("variable: "+id))
        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

        var simbolo= tablaSimbolos.buscarSimboloVariable(id.lexema,ambito)

        if(simbolo==null){
            tablaSimbolos.listaErrores.add(Error("Variable no existente: "+id.lexema,id))
        }else if(simbolo.tipo!="NUMBER_Z"&&simbolo.tipo!="NUMBER_F"){
            tablaSimbolos.listaErrores.add(Error("Operador incremento solo válido para variable numericas",id))

        }

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

}
