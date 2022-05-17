package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class InvocacionFuncion : Sentencia {

    var id:String
    var listaArgumentos:ArrayList<Argumento>

    constructor(nombre:String,listaArgumentos:ArrayList<Argumento>){
        this.id=nombre
        this.listaArgumentos=listaArgumentos
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz= TreeItem("Invocación Función")

        if (id!=null){
            raiz.children.add(TreeItem("id Funcion: "+id))

        }

        if(listaArgumentos!=null){
            val argumentos=TreeItem("Agumentos")
            raiz.children.add(argumentos)

            for(argumento in listaArgumentos){
                argumentos.children.add(argumento.getArbolVisual())
            }
        }

        return raiz
    }

}
