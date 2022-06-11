package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.Simbolo
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import co.edu.uniquindio.compiladores.modelo.lexico.Error
import javafx.scene.control.TreeItem

class InvocacionFuncion : Sentencia {

    var tokenId: Token
    var listaArgumentos:ArrayList<Argumento>

    constructor(nombre:Token,listaArgumentos:ArrayList<Argumento>){
        this.tokenId=nombre
        this.listaArgumentos=listaArgumentos
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Invocación Función")

        if (tokenId!=null){
            raiz.children.add(TreeItem("id Funcion: "+tokenId.lexema))

        }

        if(listaArgumentos!=null){
            val argumentos=TreeItem("Agumentos")
            raiz.children.add(argumentos)

            for(argumento in listaArgumentos){
                argumentos.children.add(argumento.getArbolVisual())
            }
        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

        var listaTipoArgumentos: ArrayList<String>? = ArrayList()

        for(argumento:Argumento in listaArgumentos){

            var tipoArgumento: String? = ""
            if(argumento.id!=null){
                var simbolo: Simbolo? = tablaSimbolos.buscarSimboloVariable(argumento.id!!,ambito)

                if(simbolo!=null){
                    tipoArgumento= simbolo!!.tipo
                }else{
                    tablaSimbolos.listaErrores.add(Error("No existe el argumento "+argumento.id,tokenId))
                }

            }else{
                tipoArgumento= if(argumento.expresion is ExpresionRelacional) "BOOLEAN" else tipoArgumento
                tipoArgumento= if(argumento.expresion is ExpresionLogica) "BOOLEAN" else tipoArgumento
                tipoArgumento= if(argumento.expresion is ExpresionArtimetica) "NUMBER_Z" else tipoArgumento
                tipoArgumento= if(argumento.expresion is ExpresionCadena) "STRING" else tipoArgumento

            }

            listaTipoArgumentos!!.add(tipoArgumento!!)

        }

        var invFuncion = tablaSimbolos.buscarSimboloFuncion(tokenId.lexema, listaTipoArgumentos!!)

        if(invFuncion==null){
            tablaSimbolos.listaErrores.add(Error("No existe la función "+tokenId.lexema+listaTipoArgumentos,tokenId))
        }

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

}
