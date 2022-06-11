package co.edu.uniquindio.compiladores.modelo.sintaxis

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

}
