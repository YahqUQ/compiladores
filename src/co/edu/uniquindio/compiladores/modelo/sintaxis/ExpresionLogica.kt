package co.edu.uniquindio.compiladores.modelo.sintaxis

class ExpresionLogica: Expresion {

    constructor(expresionLogica: String)
    constructor(expresionLogica: ExpresionLogica)
    constructor(operadorLogico:String ,expresionLogica: ExpresionLogica)
    constructor(expresionLogicaIzq: ExpresionLogica,operadorLogico: String,expresionLogicaDer: ExpresionLogica)

}
