package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class DeclaracionVariable: Sentencia {

    var tokenIDVariable:Token
    var tipoVariable:String
    var tipoDato:TipoDato?=null

    constructor(nombre: Token, tipoVariable: String, tipoDato: TipoDato?){
        this.tokenIDVariable=nombre
        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Declaración Variable")

        if(tokenIDVariable!=null&&tipoVariable!=null&&tipoDato!=null){
            raiz.children.add(TreeItem("nombreVar: "+tokenIDVariable+", "+"tipoVar: "+tipoVariable+", "+"tipoDato: "+ tipoDato!!.tipo))
        }

        return raiz
    }


    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        var modificable: Boolean   = if(tipoVariable=="INMUTABLE") false else true
        tablaSimbolos.guardarSimboloVariable(tokenIDVariable.lexema, tipoDato!!.tipo,modificable,ambito,tokenIDVariable.fila,tokenIDVariable.columna)
    }

}