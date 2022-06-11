package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token

class TipoArrayB {

    var tipo:String
    constructor(tokenActual: Token) {
        this.tipo=tokenActual.categoria.name
    }
}