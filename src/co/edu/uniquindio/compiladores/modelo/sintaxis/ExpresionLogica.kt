package co.edu.uniquindio.compiladores.modelo.sintaxis

class ExpresionLogica: Expresion {

    lateinit var expresionLogica: String
    lateinit var expresionLogicaIzq: ExpresionLogica
    lateinit var operadorLogico: String
    lateinit var expresionLogicaDer: ExpresionLogica

    constructor(expresionLogica: String){
        this.expresionLogica=expresionLogica
    }
    constructor(expresionLogica: ExpresionLogica){
        this.expresionLogicaIzq=expresionLogica
    }

    constructor(operadorLogico:String ,expresionLogica: ExpresionLogica){
        this.operadorLogico=operadorLogico
        this.expresionLogicaIzq=expresionLogica
    }
    constructor(expresionLogicaIzq: ExpresionLogica,operadorLogico: String,expresionLogicaDer: ExpresionLogica){
        this.expresionLogicaIzq=expresionLogicaIzq
        this.operadorLogico=operadorLogico
        this.expresionLogicaDer=expresionLogicaDer
    }

    override fun toString(): String {
        return "ExpresionLogica(expresionLogica='$expresionLogica', expresionLogicaIzq=$expresionLogicaIzq, operadorLogico='$operadorLogico', expresionLogicaDer=$expresionLogicaDer)"
    }

}
