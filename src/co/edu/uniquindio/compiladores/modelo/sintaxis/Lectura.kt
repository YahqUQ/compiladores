package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class Lectura : Sentencia {

    var cadenaLeida:String

    constructor( cadenaCaracteres :String){
        this.cadenaLeida=cadenaCaracteres
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Lectura")

        if(cadenaLeida!=null){
            raiz.children.add(TreeItem("cadena leida: "+cadenaLeida))
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
