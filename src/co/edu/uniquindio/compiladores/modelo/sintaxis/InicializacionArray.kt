package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class InicializacionArray : Sentencia {

    constructor(nombre: String, nombreAsig: String)

    constructor(nombre: String, invocacionFuncion: InvocacionFuncion)

    constructor(nombre: String, listaElementos: MutableList<ElementoArray>)

    override fun getArbolVisual(): TreeItem<String> {
        TODO("Not yet implemented")
    }
}