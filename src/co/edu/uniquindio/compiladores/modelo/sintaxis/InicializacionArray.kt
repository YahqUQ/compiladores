package co.edu.uniquindio.compiladores.modelo.sintaxis

class InicializacionArray : Sentencia {
    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor(nombre: String, nombreAsig: String)

    constructor(nombre: String, invocacionFuncion: InvocacionFuncion)

    constructor(nombre: String, listaElementos: MutableList<ElementoArray>)
}