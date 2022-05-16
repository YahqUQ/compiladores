package co.edu.uniquindio.compiladores.modelo.sintaxis

import co.edu.uniquindio.compiladores.modelo.lexico.Token

/*
<Función> ::= FUNCTION Identificador “(“ [<Lista Parámetros>] “)” “;” (Tipo de Dato | NONE)  “{“ [<Lista Sentencias>] “}”
 */
class Funcion:Elemento {


    constructor( nombre: Token, listaParametros: ArrayList<Parametro>, tipoDato: TipoDato?,
                listaSentencias: ArrayList<Sentencia>)


}
