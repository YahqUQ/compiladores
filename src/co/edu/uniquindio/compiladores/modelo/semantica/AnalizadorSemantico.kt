package co.edu.uniquindio.compiladores.modelo.semantica

import co.edu.uniquindio.compiladores.modelo.sintaxis.UnidadDeCompilacion
import co.edu.uniquindio.compiladores.modelo.lexico.Error

class AnalizadorSemantico( var uc: UnidadDeCompilacion) {

    var tablaSimbolos: TablaSimbolo = TablaSimbolo()
    var erroresSemanticos: ArrayList<Error> = tablaSimbolos.listaErrores

    fun llenarTablaSimbolos() {
        uc.llenarTablaSimbolos(tablaSimbolos)
    }
    fun analizarSemantica() {
        uc.analizarSemantica(tablaSimbolos)
    }

}