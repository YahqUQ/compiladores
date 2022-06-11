package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class AsignacionVariable : Sentencia {

    var id:String
    var idAsignado:String?=null
    var expresion:Expresion?=null
    var invocacionFuncion:InvocacionFuncion?=null

    constructor(identificador:String,identificadorAsignado: String){
        this.id=identificador
        this.idAsignado=identificadorAsignado
    }
    constructor(identificador: String, expresion:Expresion){
        this.id=identificador
        this.expresion=expresion
    }
    constructor(identificador: String, invocacionFuncion: InvocacionFuncion){
        this.id=identificador
        this.invocacionFuncion=invocacionFuncion
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Asignación Variable")

        if(id!=null){
            raiz.children.add(TreeItem("id: "+id))
            if(idAsignado!=null){
                raiz.children.add(TreeItem("Variable Asignada: "+idAsignado))
            }else if(expresion!=null){
                raiz.children.add(expresion!!.getArbolVisual())
            }else if(invocacionFuncion!=null){
                raiz.children.add(invocacionFuncion!!.getArbolVisual())
            }

        }

        return raiz
    }
}
