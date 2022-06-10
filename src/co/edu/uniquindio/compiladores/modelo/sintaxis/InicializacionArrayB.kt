package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class InicializacionArrayB : Sentencia {

    var nombre: String
    var nombreAsignacion: String? = null
    var invocacionFuncion: InvocacionFuncion? = null
    var elementosArrayB: MutableList<MutableList<ElementoArray>>? = null

    constructor(nombre: String, nombreAsignacion: String) {
        this.nombre = nombre
        this.nombreAsignacion = nombreAsignacion
    }

    constructor(nombre: String, invocacionFuncion: InvocacionFuncion) {
        this.nombre = nombre
        this.invocacionFuncion = invocacionFuncion
    }

    constructor(nombre: String, elementosArrayB: MutableList<MutableList<ElementoArray>>) {
        this.nombre = nombre
        this.elementosArrayB = elementosArrayB
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Inicializacion Array B")
        if (nombre != null) {
            raiz.children.add(TreeItem("nombre: "+nombre))
            if (nombreAsignacion != null) {
                raiz.children.add(TreeItem("identificador asignado: "+nombreAsignacion))
            } else if (invocacionFuncion != null) {
                raiz.children.add(invocacionFuncion!!.getArbolVisual())
            } else if (elementosArrayB != null) {
                var elementos1 = TreeItem("Elementos")
                raiz.children.add(elementos1)
                for (elems in elementosArrayB!!.toList()) {
                    var elementos2 = TreeItem("Elementos")
                    elementos1.children.add(elementos2)
                    for (elem in elems!!.toList()) {
                        elementos2.children.add(elem.getArbolVisual())
                    }
                }
            }
        }
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {
        TODO("Not yet implemented")
    }
}