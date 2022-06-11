package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class DeclaracionAsignacionArrayB : Sentencia {

    var nombre: String
    var token: Token
    var tipoVariable: String
    var tipoDato: TipoArrayB
    var expresion: Expresion

    constructor(nombre: String, tipoVariable: String, tipoDato: TipoArrayB, expresion: Expresion, token: Token) {
        this.nombre = nombre
        this.token = token
        this.tipoVariable = tipoVariable
        this.tipoDato = tipoDato
        this.expresion = expresion
    }

    override fun getArbolVisual(): TreeItem<String> {
        return TreeItem()
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        if (tablaSimbolos.buscarSimboloVariable(nombre, ambito) != null) {
            // Reportar problema, la variable no puede existir ya en el mismo ámbito
        } else {
            expresion.analizarSemantica(tablaSimbolos, ambito)
        }
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        tablaSimbolos.guardarSimboloVariable(nombre, tipoDato.tipo, tipoVariable == "MUTABLE", ambito, token.fila, token.columna)
    }

}
