package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class DeclaracionArray : Sentencia {

    var nombre: String
    var nombreToken: Token
    var tipoVariable: String
    var tipoDato: TipoArray

    constructor(nombre: String, tipoVariable: String, tipoDato: TipoArray, token: Token) {
        this.nombre = nombre
        this.nombreToken = token
        this.tipoVariable = tipoVariable
        this.tipoDato = tipoDato
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Declaración Array")
        if (nombre != null && tipoVariable != null && tipoDato != null) {
            raiz.children.add(TreeItem("nombreVar: "+nombre+", "+"tipoVar: "+tipoVariable+", "+"tipoDato: "+tipoDato.tipo))
        }
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        if (tablaSimbolos.buscarSimboloVariable(nombre, ambito) != null) {
            // Reportar error
        }
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(nombre, tipoDato.tipo, true, ambito, nombreToken.fila , nombreToken.columna)
    }
}