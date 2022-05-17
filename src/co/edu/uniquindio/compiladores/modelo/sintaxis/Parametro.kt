package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import javafx.scene.control.TreeItem

class Parametro {

    var nombre: String
    var tipoVar: TipoDato?=null
    var tipoVar2: TipoArray?=null

    constructor(nombre: Token, tipoDato: TipoDato?){
        this.nombre=nombre.lexema
        if (tipoDato != null) {
            this.tipoVar=tipoDato
        }
    }
    constructor(nombre: Token, tipoArray: TipoArray?){
        this.nombre=nombre.lexema
        if (tipoArray != null) {
            this.tipoVar2=tipoArray
        }
    }

    fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Parámetro")
        if (nombre != null) {
            if(tipoVar !=null) {
                raiz.children
                    .add(TreeItem(nombre + ":" + tipoVar!!.tipo))
            }else{
                if(tipoVar2!=null){
                    raiz.children
                        .add(TreeItem(nombre + ":" + tipoVar2!!.tipo))
                }
            }
        }

        return raiz
    }
}
