package co.edu.uniquindio.compiladores.modelo.sintaxis

class InicializacionArrayB : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor(nombre: String, nombreAsignacion: String)

    constructor(nombre: String, invocacionFuncion: InvocacionFuncion)

    constructor(nombre: String, elementosArrayB: MutableList<MutableList<ElementoArray>>)
}