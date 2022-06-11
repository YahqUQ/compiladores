package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Error
import co.edu.uniquindio.compiladores.modelo.lexico.Token
import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class DeclaracionAsignacionVariable : Sentencia{

    var tokenNombre:Token
    var tipoVariable:String
    var tipoDato:TipoDato? = null
    var expresion:Expresion? =null
    var tokenIDAsignado:Token? =null
    var invocacionFuncion: InvocacionFuncion? =null

    constructor( tokenNombre:Token, tipoVariable: String, tipoDato: TipoDato?,
                valor: Expresion? ){

        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
        if (valor != null) {
            this.expresion=valor
        }
        this.tokenNombre=tokenNombre

    }

    constructor( tokenNombre: Token, tipoVariable: String, tipoDato: TipoDato?,
                 valor:Token ){

        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
        this.tokenIDAsignado=valor
        this.tokenNombre=tokenNombre

    }

    constructor( tokenNombre: Token, tipoVariable: String, tipoDato: TipoDato?,
                 valor: InvocacionFuncion ){

        this.tipoVariable=tipoVariable
        if (tipoDato != null) {
            this.tipoDato=tipoDato
        }
        this.invocacionFuncion=valor
        this.tokenNombre=tokenNombre
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz=TreeItem("Declaración-Asignación Variable")

        if(tokenNombre.lexema!=null&&tipoVariable!=null&&tipoDato!=null){
            raiz.children.add(TreeItem("nombreVar: "+tokenNombre.lexema+", "+"tipoVar: "+tipoVariable+", "+"tipoDato: "+ tipoDato!!.tipo))

            if(expresion!=null){
                raiz.children.add(expresion!!.getArbolVisual())
            }else if(invocacionFuncion!=null){
                raiz.children.add((invocacionFuncion!!.getArbolVisual()))
            }else if(tokenIDAsignado!=null){
                raiz.children.add(TreeItem("Variable Asignada: "+ tokenIDAsignado!!.lexema))
            }
        }

        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

        var modificable: Boolean   = if(tipoVariable=="INMUTABLE") false else true
        tablaSimbolos.guardarSimboloVariable(tokenNombre.lexema, tipoDato!!.tipo,modificable,ambito,tokenNombre.fila,tokenNombre.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String){

        if(tokenIDAsignado!=null){

            val simbolo = tablaSimbolos.buscarSimboloVariable(tokenIDAsignado!!.lexema, ambito)

            if(simbolo==null){
                tablaSimbolos.listaErrores.add(Error("Variable no existente: "+ tokenIDAsignado!!.lexema,tokenIDAsignado!!.fila , tokenIDAsignado!!.columna))
            }else{
                if(simbolo.tipo!=this.tipoDato!!.tipo){
                    tablaSimbolos.listaErrores.add(Error("Variable no es tipo ${tipoDato!!.tipo} ",tokenIDAsignado!!.fila , tokenIDAsignado!!.columna))
                }
            }

        }else if(invocacionFuncion!=null){
                invocacionFuncion!!.analizarSemantica(tablaSimbolos,ambito)

        }else if(expresion!=null){
                expresion!!.analizarSemantica(tablaSimbolos,ambito)

        }

    }


}
