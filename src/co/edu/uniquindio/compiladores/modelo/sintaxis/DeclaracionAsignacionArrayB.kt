package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class DeclaracionAsignacionArrayB : Sentencia {

    constructor(nombre: String, tipoVariable: String, tipoDato: TipoArrayB, expresion: Expresion)

    override fun getArbolVisual(): TreeItem<String> {
        TODO("Not yet implemented")
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

}
