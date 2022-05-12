package co.edu.uniquindio.compiladores.modelo.sintaxis

class Retorno : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor( expresion: Expresion)
    constructor( identificador: String)

}


