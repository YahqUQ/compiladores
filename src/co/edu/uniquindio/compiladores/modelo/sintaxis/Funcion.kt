package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import javafx.scene.control.TreeItem

/*
<Función> ::= FUNCTION Identificador “(“ [<Lista Parámetros>] “)” “;” (Tipo de Dato | NONE)  “{“ [<Lista Sentencias>] “}”
 */
class Funcion : Elemento {

    var nombre: String
    var tipoRetorno: TipoDato?=null
    var listaParametros: ArrayList<Parametro>
    var listaSentencias: ArrayList<Sentencia>

    constructor(
        nombre: Token, listaParametros: ArrayList<Parametro>, tipoDato: TipoDato?,
        listaSentencias: ArrayList<Sentencia>
    ) {
        this.nombre = nombre.lexema
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
                .add(TreeItem(nombre + ":" + tipoRetorno!!.tipo))
        }


        val params = TreeItem("Parámetros")
        raiz.children.add(params)
        for (parametro in listaParametros) {
            params.children.add(parametro.getArbolVisual())
        }

        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)
        for (sentencia in listaSentencias) {
            sentencias.children.add(sentencia.getArbolVisual())
        }
        return raiz
    }


}
