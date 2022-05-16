package co.edu.uniquindio.compiladores.modelo.sintaxis

class DeclaracionAsignacionVariable : Sentencia{

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?,
                valor: Expresion? )

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?,
                 valor: String )

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?,
                 valor: InvocacionFuncion )



}
