package com.languainsta.lox;

// datatype token created using records
record Token(TokenType type, String lexeme, Object literal, int line) {
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }

    @Override
    public String lexeme() {
        return lexeme;
    }
}
