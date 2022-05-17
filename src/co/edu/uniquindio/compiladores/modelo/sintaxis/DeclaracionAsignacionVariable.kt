package co.edu.uniquindio.compiladores.modelo.sintaxis

import javafx.scene.control.TreeItem

class DeclaracionAsignacionVariable : Sentencia{

    var idVariable:String
    var tipoVariable:String
    var tipoDato:TipoDato? = null
    var expresion:Expresion? =null
    var idAsignado:String? =null
    var invocacionFuncion: InvocacionFuncion? =null

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?,
                valor: Expresion? ){
        this.idVariable=nombre
        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
        if (valor != null) {
            this.expresion=valor
        }
        idAsignado=""


    }

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?,
                 valor: String ){
        this.idVariable=nombre
        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
        this.idAsignado=valor

    }

    constructor( nombre: String, tipoVariable: String, tipoDato: TipoDato?,
                 valor: InvocacionFuncion ){
        this.idVariable=nombre
        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
        this.invocacionFuncion=valor
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Declaración-Asignación Variable")

        if(idVariable!=null&&tipoVariable!=null&&tipoDato!=null){
            raiz.children.add(TreeItem("nombreVar: "+idVariable+", "+"tipoVar: "+tipoVariable+", "+"tipoDato: "+ tipoDato!!.tipo))

            if(expresion!=null){
                raiz.children.add(expresion!!.getArbolVisual())
            }else if(invocacionFuncion!=null){
                raiz.children.add((invocacionFuncion!!.getArbolVisual()))
            }else if(idAsignado!=null){
                raiz.children.add(TreeItem("Variable Asignada: "+idAsignado))
            }
        }

        return raiz
    }


}
