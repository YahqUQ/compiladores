package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class InicializacionArray : Sentencia {

    var nombre: String
    var nombreAsignacion: String? = null
    var invocacionFuncion: InvocacionFuncion? = null
    var listaElementos: MutableList<ElementoArray>? = null

    constructor(nombre: String, nombreAsig: String) {
        this.nombre = nombre
        this.nombreAsignacion = nombreAsig
    }

    constructor(nombre: String, invocacionFuncion: InvocacionFuncion) {
        this.nombre = nombre
        this.invocacionFuncion = invocacionFuncion
    }

    constructor(nombre: String, listaElementos: MutableList<ElementoArray>) {
        this.nombre = nombre
        this.listaElementos = listaElementos
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Inicializacion Array")
        if (nombre != null) {
            raiz.children.add(TreeItem("nombre: "+nombre))
            if (nombreAsignacion != null) {
                raiz.children.add(TreeItem("identificador asignado: "+nombreAsignacion))
            } else if (invocacionFuncion != null) {
                raiz.children.add(invocacionFuncion!!.getArbolVisual())
            } else if (listaElementos != null) {
                var elementos = TreeItem("Elementos")
                raiz.children.add(elementos)
                for (elem in listaElementos!!.toList()) {
                    elementos.children.add(elem.getArbolVisual())
                }
            }
        }
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {
        if (tablaSimbolos.buscarSimboloVariable(nombre, ambito) == null) {
            // Reportar error
        } else {
            if (nombreAsignacion != null) {
                if (tablaSimbolos.buscarSimboloVariable(nombreAsignacion!!, ambito) == null) {
                    // Reportar error
                }
            } else {
                if (invocacionFuncion != null) {
                    invocacionFuncion!!.analizarSemantica(tablaSimbolos, ambito)
                } else {
                    if (listaElementos == null) {
                        // Reportar error
                    }
                }
            }
        }
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

    }
}