package co.edu.uniquindio.compiladores.modelo.sintaxis

class Impresion : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor( expresionCadena:ExpresionCadena)

}


