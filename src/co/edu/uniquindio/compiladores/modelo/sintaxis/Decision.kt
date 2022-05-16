package co.edu.uniquindio.compiladores.modelo.sintaxis

class Decision : Sentencia {

    constructor(condicion:ExpresionLogica,listaSentenciaIF: ArrayList<Sentencia>,listaSentenciaELSE: ArrayList<Sentencia>)
    constructor(condicion:ExpresionLogica,listaSentenciaIF: ArrayList<Sentencia>)

    constructor(condicion:ExpresionRelacional,listaSentenciaIF: ArrayList<Sentencia>,listaSentenciaELSE: ArrayList<Sentencia>)
    constructor(condicion:ExpresionRelacional,listaSentenciaIF: ArrayList<Sentencia>)



}
