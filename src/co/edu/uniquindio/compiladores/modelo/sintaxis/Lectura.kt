package co.edu.uniquindio.compiladores.modelo.sintaxis

class Lectura : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor( cadenaCaracteres :String)

}
