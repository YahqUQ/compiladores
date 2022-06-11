package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import javafx.scene.control.TreeItem

class Parametro {

    var nombre: String
    var tipoVar: TipoDato?=null
    var tipoVarA: TipoArray?=null
    var tipoVarB: TipoArrayB?=null

    constructor(nombre: Token, tipoDato: TipoDato?){
        this.nombre=nombre.lexema
        if (tipoDato != null) {
            this.tipoVar=tipoDato
        }
    }
    constructor(nombre: Token, tipoArray: TipoArray?){
        this.nombre=nombre.lexema
        if (tipoArray != null) {
            this.tipoVarA=tipoArray
        }
    }

    constructor(nombre: Token, tipoArray: TipoArrayB?){
        this.nombre=nombre.lexema
        if (tipoArray != null) {
            this.tipoVarB=tipoArray
        }
    }



    fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Parámetro")
        if (nombre != null) {
            if(tipoVar !=null) {
                raiz.children
                    .add(TreeItem(nombre + ":" + tipoVar!!.tipo))
            }else{
                if(tipoVarA!=null){
                    raiz.children
                        .add(TreeItem(nombre + ":" + tipoVarA!!.tipo))
                }
            }
        }

        return raiz
    }

    override fun toString(): String {
        if(tipoVar!=null){
            return "${tipoVar!!.tipo}"
        }else if (tipoVarA!=null){
            return "${tipoVarA!!.tipo}"
        }else{
            return "$tipoVarB"
        }

    }


}
