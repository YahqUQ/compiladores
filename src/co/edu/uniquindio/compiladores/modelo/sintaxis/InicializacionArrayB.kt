package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class InicializacionArrayB : Sentencia {

    constructor(nombre: String, nombreAsignacion: String)

    constructor(nombre: String, invocacionFuncion: InvocacionFuncion)

    constructor(nombre: String, elementosArrayB: MutableList<MutableList<ElementoArray>>)

    override fun getArbolVisual(): TreeItem<String> {
        TODO("Not yet implemented")
    }
}