package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token

class Parametro() {
    constructor(nombre: Token, tipoDato: TipoDato?) :this()
    constructor(nombre: Token, tipoArray: TipoArray?) : this()

}
