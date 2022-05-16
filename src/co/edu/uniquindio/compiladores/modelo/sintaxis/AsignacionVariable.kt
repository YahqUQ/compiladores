package co.edu.uniquindio.compiladores.modelo.sintaxis

class AsignacionVariable : Sentencia {

    constructor(identificador:String,identificadorAsignado: String)
    constructor(identificador: String, expresion:Expresion)
    constructor(identificador: String, invocacionFuncion: InvocacionFuncion)
}
