package co.edu.uniquindio.compiladores.modelo.sintaxis

class While : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor(condicion:ExpresionLogica,listaSentencias: ArrayList<Sentencia>)


}
