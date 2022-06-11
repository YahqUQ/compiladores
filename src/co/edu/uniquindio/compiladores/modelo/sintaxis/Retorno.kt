package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import co.edu.uniquindio.compiladores.modelo.lexico.Error
import javafx.scene.control.TreeItem

class Retorno : Sentencia {

    var expresion:Expresion?=null
    var identificador:Token?=null

    constructor( expresion: Expresion){
        this.expresion=expresion
    }
    constructor( identificador: Token){
        this.identificador=identificador
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Retorno")

        if(expresion!=null){
            raiz.children.add(expresion!!.getArbolVisual())
        }else if(identificador!= null){
            raiz.children.add(TreeItem("id Variable Retorno: "+ identificador!!.lexema))
        }
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String,f:Funcion) {

        if(identificador!=null){

            var simbolo = tablaSimbolos.buscarSimboloVariable(identificador!!.lexema,ambito)

            if(simbolo!=null){
                if(!f.tipoRetorno!!.tipo!!.equals(simbolo.tipo)){
                    tablaSimbolos.listaErrores.add(Error("El tipo de retorno no coincide: Se esperaba "+f.tipoRetorno!!.tipo, identificador!!))
                }
            }else{
                tablaSimbolos.listaErrores.add(Error("Variable no Existente", identificador!!))
            }

        }else if (expresion!=null){

            var tipoExpresion= ""
                tipoExpresion= if(expresion is ExpresionRelacional) "BOOLEAN" else tipoExpresion
                tipoExpresion= if(expresion is ExpresionLogica) "BOOLEAN" else tipoExpresion
                tipoExpresion= if(expresion is ExpresionArtimetica) "NUMBER_Z" else tipoExpresion
                tipoExpresion= if(expresion is ExpresionCadena) "STRING" else tipoExpresion

            if (!f.tipoRetorno!!.tipo.equals(tipoExpresion)){
                tablaSimbolos.listaErrores.add(Error("El tipo de retorno no coincide: Se esperaba "+f.tipoRetorno!!.tipo,f.nombreToken))
            }

        }
    }




}




