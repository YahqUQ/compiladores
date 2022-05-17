package co.edu.uniquindio.compiladores.modelo.sintaxis


import javafx.scene.control.TreeItem


class Argumento {

    var id:String? = null
    var expresion:Expresion? =null

    constructor(identificador:String){
        this.id=identificador
    }
    constructor(expresion: Expresion?){
        if (expresion != null) {
            this.expresion=expresion
        }
    }

    fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Argumento")
        if (id != null) {
            raiz.children.add(TreeItem("id Argumento: "+id))
        }else if(expresion!=null){
            raiz.children.add(expresion!!.getArbolVisual())
        }
        return raiz
    }
}
