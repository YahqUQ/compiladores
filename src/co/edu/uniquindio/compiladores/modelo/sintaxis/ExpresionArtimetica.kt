package co.edu.uniquindio.compiladores.modelo.sintaxis

class ExpresionArtimetica:Expresion {

    lateinit var expresionA:String
    lateinit var expresionArtimeticaIzq: ExpresionArtimetica
    lateinit var operadorArtimetico: String
    lateinit var expresionAritmeticaDer: ExpresionArtimetica

    constructor(expresionArtimetica: String){
        expresionA=expresionArtimetica
    }
    constructor(expresionArtimetica: ExpresionArtimetica){
        expresionArtimeticaIzq=expresionArtimetica
    }
    constructor(expresionArtimeticaIzq: ExpresionArtimetica,operadorArtimetico: String,expresionAritmeticaDer: ExpresionArtimetica){
        this.expresionArtimeticaIzq=expresionArtimeticaIzq
        this.operadorArtimetico=operadorArtimetico
        this.expresionAritmeticaDer=expresionAritmeticaDer
    }

    override fun toString(): String {
        if(expresionA!=null){
            return "ExpresionArtimetica(expresionA='$expresionA')"
        }

        if(expresionArtimeticaIzq!=null&&operadorArtimetico==null){
            return "ExpresionArtimetica(expresionA='$expresionArtimeticaIzq')"
        }else{
            return "ExpresionArtimetica(expresionArtimeticaIzq=$expresionArtimeticaIzq, operadorArtimetico='$operadorArtimetico', expresionAritmeticaDer=$expresionAritmeticaDer)"
        }
    }
}
