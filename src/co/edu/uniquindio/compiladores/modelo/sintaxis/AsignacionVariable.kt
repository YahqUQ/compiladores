package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class AsignacionVariable : Sentencia {

    var tokenIDVariable:Token?=null
    var tokenIDAsignado: Token?=null
    var expresion:Expresion?=null
    var invocacionFuncion:InvocacionFuncion?=null

    constructor(identificador:Token,identificadorAsignado: Token){
        this.tokenIDVariable= identificador
        this.tokenIDAsignado=identificadorAsignado
    }
    constructor(identificador: Token, expresion:Expresion){
        this.tokenIDVariable=identificador
        this.expresion=expresion
    }
    constructor(identificador: Token, invocacionFuncion: InvocacionFuncion){
        this.tokenIDVariable=identificador
        this.invocacionFuncion=invocacionFuncion
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Asignación Variable")

        if(tokenIDVariable!=null){
            raiz.children.add(TreeItem("id: "+ tokenIDVariable!!.lexema))
            if(tokenIDAsignado!=null){
                raiz.children.add(TreeItem("Variable Asignada: "+tokenIDAsignado))
            }else if(expresion!=null){
                raiz.children.add(expresion!!.getArbolVisual())
            }else if(invocacionFuncion!=null){
                raiz.children.add(invocacionFuncion!!.getArbolVisual())
            }

        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

        if(tokenIDVariable!=null){

            val simbolo = tablaSimbolos.buscarSimboloVariable(tokenIDVariable!!.lexema,ambito)

            if(simbolo==null){
                tablaSimbolos.listaErrores.add(Error("Variable no existente",tokenIDVariable!!.fila , tokenIDVariable!!.columna))
            }
        }

        if(tokenIDAsignado!=null){

            val simbolo = tablaSimbolos.buscarSimboloVariable(tokenIDAsignado!!.lexema, ambito)

            if(simbolo==null){
                tablaSimbolos.listaErrores.add(Error("Variable no existente",tokenIDAsignado!!.fila , tokenIDAsignado!!.columna))
            }


        }else if(invocacionFuncion!=null){
            invocacionFuncion!!.analizarSemantica(tablaSimbolos,ambito)

        }else if(expresion!=null){
            expresion!!.analizarSemantica(tablaSimbolos,ambito)

        }
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

    }
}
