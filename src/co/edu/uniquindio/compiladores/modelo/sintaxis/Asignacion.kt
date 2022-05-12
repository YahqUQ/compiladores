package co.edu.uniquindio.compiladores.modelo.sintaxis

class Asignacion : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor(identificador:String,identificadorAsignado: String)
    constructor(identificador: String, expresion:Expresion)
    constructor(identificador: String, invocacionFuncion: InvocacionFuncion)
}
