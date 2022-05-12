package co.edu.uniquindio.compiladores.modelo.sintaxis

class Decision : Sentencia {

    override var sentencia: Sentencia

    init {
        this.sentencia=this

    }

    constructor(condicion:ExpresionLogica,listaSentenciaIF: ArrayList<Sentencia>,listaSentenciaELSE: ArrayList<Sentencia>)
    constructor(condicion:ExpresionLogica,listaSentenciaIF: ArrayList<Sentencia>)



}
