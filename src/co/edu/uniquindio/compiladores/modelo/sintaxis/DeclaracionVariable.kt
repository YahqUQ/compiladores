package co.edu.uniquindio.compiladores.modelo.sintaxis

class DeclaracionVariable: Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?)

}