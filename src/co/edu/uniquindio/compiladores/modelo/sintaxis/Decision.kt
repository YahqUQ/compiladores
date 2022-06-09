package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.semantica.TablaSimbolo
import javafx.scene.control.TreeItem

class Decision : Sentencia {

    var condicionL: ExpresionLogica? = null
    var condicionR: ExpresionRelacional? = null
    var listaSentenciaIF: ArrayList<Sentencia>? = null
    var listaSentenciaELSE: ArrayList<Sentencia>? = null

    constructor(
        condicion: ExpresionLogica,
        listaSentenciaIF: ArrayList<Sentencia>,
        listaSentenciaELSE: ArrayList<Sentencia>
    ) {
        this.condicionL = condicion
        this.listaSentenciaIF = listaSentenciaIF
        this.listaSentenciaELSE = listaSentenciaELSE
    }

    constructor(condicion: ExpresionLogica, listaSentenciaIF: ArrayList<Sentencia>) {
        this.condicionL = condicion
        this.listaSentenciaIF = listaSentenciaIF
    }

    constructor(
        condicion: ExpresionRelacional,
        listaSentenciaIF: ArrayList<Sentencia>,
        listaSentenciaELSE: ArrayList<Sentencia>
    ) {
        this.condicionR = condicion
        this.listaSentenciaIF = listaSentenciaIF
        this.listaSentenciaELSE = listaSentenciaELSE
    }

    constructor(condicion: ExpresionRelacional, listaSentenciaIF: ArrayList<Sentencia>) {
        this.condicionR = condicion
        this.listaSentenciaIF = listaSentenciaIF
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("DECISION")

        if (condicionL != null) {
            raiz.children.add(condicionL!!.getArbolVisual())
        } else if (condicionR != null) {
            raiz.children.add(condicionR!!.getArbolVisual())
        }


        val sentenciasIF = TreeItem("Sentencias IF")
        raiz.children.add(sentenciasIF)

        if (listaSentenciaIF != null) {
            for (sentencia in listaSentenciaIF!!) {
                sentenciasIF.children.add(sentencia.getArbolVisual())
            }
        }



        if (listaSentenciaELSE != null) {

            val sentenciasELSE = TreeItem("Sentencias ELSE")
            raiz.children.add(sentenciasELSE)
            for (sentencia in listaSentenciaELSE!!) {
                sentenciasELSE.children.add(sentencia.getArbolVisual())
            }
        }


        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolo, ambito: String) {

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolo, ambito: String) {

        for(sentencia:Sentencia in listaSentenciaIF!!){
            sentencia.llenarTablaSimbolos(tablaSimbolos,ambito+"IF:"+condicionL!!+condicionR+"/")
        }
    }


}
