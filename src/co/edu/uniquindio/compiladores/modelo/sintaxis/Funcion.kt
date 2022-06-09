package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.Simbolo
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

/*
<Función> ::= FUNCTION Identificador “(“ [<Lista Parámetros>] “)” “;” (Tipo de Dato | NONE)  “{“ [<Lista Sentencias>] “}”
 */
class Funcion : Elemento {

    var nombreToken: Token
    var tipoRetorno: TipoDato?=null
    var listaParametros: ArrayList<Parametro>
    var listaSentencias: ArrayList<Sentencia>

    constructor(
        nombre: Token, listaParametros: ArrayList<Parametro>, tipoDato: TipoDato?,
        listaSentencias: ArrayList<Sentencia>
    ) {
        this.nombreToken = nombre
        if (tipoDato != null) {
            this.tipoRetorno = tipoDato
        }
        this.listaSentencias = listaSentencias
        this.listaParametros = listaParametros
    }

    override fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Función")
        if (tipoRetorno != null) {
            raiz.children
                .add(TreeItem(nombreToken.lexema + ":" + tipoRetorno!!.tipo))
        }


        val params = TreeItem("Parámetros")
        raiz.children.add(params)

        if(listaParametros!=null){
            for (parametro in listaParametros) {
                params.children.add(parametro.getArbolVisual())
            }
        }


        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)

        if(listaSentencias!=null) {
            for (sentencia in listaSentencias) {
                sentencias.children.add(sentencia.getArbolVisual())
            }
        }
        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolo: TablaSimbolo) {

        var tipoParametros:  ArrayList<String>? = ArrayList()

        for(parametro:Parametro in listaParametros){

            tipoParametros!!.add(parametro.tipoVar!!.tipo)
            tipoParametros!!.add(parametro.tipoVarA!!.tipo)

            if(parametro.tipoVarB!=null){
                tipoParametros!!.add("ArrayBnotImp")
            }

            tablaSimbolo.guardarSimboloVariable(parametro.nombre,parametro.tipoVar!!.tipo,true,"Funcion:"+nombreToken.lexema+listaParametros+"/",nombreToken.fila,nombreToken.columna)

        }
        tablaSimbolo.guardarSimboloFuncion(nombreToken.lexema, tipoRetorno!!.tipo,tipoParametros!!,nombreToken.fila,nombreToken.columna)

        for(sentencia:Sentencia in listaSentencias){
            sentencia.llenarTablaSimbolos(tablaSimbolo,"Funcion:"+nombreToken.lexema+listaParametros+"/")
        }
    }

    override fun analizarSemantica(tablaSimbolo: TablaSimbolo) {
        var tipoParametros:  ArrayList<String>? = ArrayList()

        for(parametro:Parametro in listaParametros){

            tipoParametros!!.add(parametro.tipoVar!!.tipo)
            tipoParametros!!.add(parametro.tipoVarA!!.tipo)

            if(parametro.tipoVarB!=null){
                tipoParametros!!.add("ArrayBnotImp")
            }

            tablaSimbolo.guardarSimboloVariable(parametro.nombre,parametro.tipoVar!!.tipo,true,"Funcion:"+nombreToken.lexema+listaParametros+"/",nombreToken.fila,nombreToken.columna)

        }

        for(sentencia:Sentencia in listaSentencias){
            sentencia.analizarSemantica(tablaSimbolo,"Funcion:"+nombreToken.lexema+listaParametros+"/")
        }
    }



}
