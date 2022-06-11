package co.edu.uniquindio.compiladores.modelo.semantica

import co.edu.uniquindio.compiladores.modelo.lexico.Error

class TablaSimbolo() {

    var listaErrores: ArrayList<Error> = ArrayList()
    var listaSimbolos: ArrayList<Simbolo> = ArrayList()
    /**
     * Permite guardar un símbolo de tipo variable en la tabla de símbolos
     */
    fun guardarSimboloVariable(nombre: String, tipo: String,modificable:Boolean, ambito: String, fila: Int,
                               columna: Int): Simbolo? {
        val s = buscarSimboloVariable(nombre, ambito)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo, modificable, ambito, fila, columna )
            listaSimbolos.add(nuevo)
            return nuevo
        } else {

            listaErrores.add(Error("Ya existe la variable: "+nombre,fila,columna))
        }
        return null
    }
    /**
     * Permite guardar un símbolo de tipo función en la tabla de símbolos
     */
    fun guardarSimboloFuncion(nombre: String, tipo: String, tipoParametros: ArrayList<String>,fila:Int, columna: Int): Simbolo? {
        val s = buscarSimboloFuncion(nombre, tipoParametros)
        if (s == null) {
            val nuevo = Simbolo(nombre, tipo, tipoParametros)
            listaSimbolos.add(nuevo)
            return nuevo
        } else {
            listaErrores.add(Error("Ya existe la funcion: "+nombre+tipoParametros, fila,columna))
        }
        return null
    }

    /**
     * Verifica la existencia de la variable dentro de la tabla de simbolos en un ámbito dado
     */
    fun buscarSimboloVariable(nombre: String, ambito:String): Simbolo? {


        for (simbolo in listaSimbolos) {
            if (simbolo.ambito != null) {
                if (nombre == simbolo.nombre) {
                    if(ambito.contains(simbolo.ambito)){
                        return simbolo
                    }
                }
            }
        }
        return null
    }

    /**
     * Verifica la existencia de la funcion dentro de la tabla de simbolos
     */
    fun buscarSimboloFuncion(nombre: String, tiposParametros: ArrayList<String>):
            Simbolo? {
        for (simbolo in listaSimbolos) {
            if (simbolo.tipoParametros != null) {
                if (nombre == simbolo.nombre && tiposParametros ==

                    simbolo.tipoParametros
                ) {
                    return simbolo
                }
            }
        }
        return null
    }

}