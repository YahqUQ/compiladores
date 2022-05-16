package co.edu.uniquindio.compiladores.modelo.sintaxis

class ExpresionArtimetica:Expresion {

    constructor(expresionArtimetica: String)
    constructor(expresionArtimetica: ExpresionArtimetica)
    constructor(expresionArtimeticaIzq: ExpresionArtimetica,operadorArtimetico: String,expresionAritmeticaDer: ExpresionArtimetica)
}
