package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class DeclaracionVariable: Sentencia {

    var idVariable:String
    var tipoVariable:String
    var tipoDato:TipoDato?=null

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?){
        this.idVariable=nombre
        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Declaración Variable")

        if(idVariable!=null&&tipoVariable!=null&&tipoDato!=null){
            raiz.children.add(TreeItem("nombreVar: "+idVariable+", "+"tipoVar: "+tipoVariable+", "+"tipoDato: "+ tipoDato!!.tipo))
        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

}