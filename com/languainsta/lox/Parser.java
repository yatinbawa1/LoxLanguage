package com.languainsta.lox;

import java.util.List;

import static com.languainsta.lox.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    // current refers to the token
    // next in line to be used
    private int current = 0;

    // Initialize List of tokens
    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type() == EOF;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type() == type;
    }

    private Token previous() {
        return  tokens.get(current - 1);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
    // match check if current token
    // is something that
    // matches any of the given
    // TokenTypes
    private boolean match(TokenType... types) {
        for (TokenType type: types) {
            if (check(type)) {
//                if it does match
//                we advance our current
//                and then return true
                advance();
                return true;
            }
        }
        return false;
    }


//   "25*36-45 <= 850 == true" -> Binary(Binary(Binary(Binary(25,STAR,36),MINUS,45),LESS_EQUAL,850),EQUAL_EQUAL,TRUE)
//                                              EQUAL_EQUAL
//                                                  ^
//                                      LESS_EQUAL    TRUE
//                                        ^   <-
//                                      MINUS 850
//                                        ^   <-
//                                      STAR  45
//                                       ^    <-
//                                      25    36
//   -----------------------------------------------------------------------------------------------------------------
    private Expr expression() {
        return equality();
    }

//    example 25*36-45 <= 850 == true
//    expr = comparison() -> Binary(Binary(Binary(25,STAR,36),MINUS,45),LESS_EQUAL,850)
//    matches EQUAL_EQUAL
//    right = comparison() -> term -> factor -> unary() -> primary() -> TRUE
//    returns -> Binary(Binary(Binary(Binary(25,STAR,36),MINUS,45),LESS_EQUAL,850),EQUAL_EQUAL,TRUE)
    private Expr equality() {
        Expr expr = comparison();
        while (match(BANG_EQUAL,EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr,operator,expr);
        }
        return expr;
    }

//    example 25*36-45 <= 850
//    expr = term() -> factor -> Binary(Binary(25,STAR,36),MINUS,45)
//    matches LESS_EQUAL
//    right = term() -> factor() -> unary() -> primary() -> 850
//    returns Binary(Binary(Binary(25,STAR,36),MINUS,45),LESS_EQUAL,850)
    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER,GREATER_EQUAL,LESS,LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr,operator,expr);
        }

        return expr;
    }
//    example 25*36-45
//    expr = factor() -> Binary(25,STAR,36)
//    matched minus
//    right = factor() -> unary() - primary() -> 45
//    returns -> Binary(Binary(25,STAR,36),MINUS,45)
    private Expr term() {
        Expr expr = factor();

        while (match(MINUS,PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr,operator,expr);
        }

        return expr;
    }
    // example 25*36
    // expr = unary() -> primary() -> 25
    // matched star
    // right = unary() -> primary() -> 36
    // returns Binary(25,STAR,36)
    private Expr factor() {
        Expr expr = unary();

        while (match(STAR,SLASH)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr,operator,expr);
        }

        return expr;
    }

    // an example -25
    // minus is matched
    // by match()
    // currentValue = 25;
    // when right = Unary() -> return primary();
    // primary() -> Literal 25
    private Expr unary() {
        if (match(BANG,MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator,right);
        }
        return primary();
    }

    private Expr primary() {

        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal());
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(),"Error");
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(),message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token,message);
        return new ParseError();
    }
}
