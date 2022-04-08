package com.languainsta.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.languainsta.lox.TokenType.*;

public class Scanner {

    private final String source; // source file
    private final List<Token> tokens = new ArrayList<>(); // list of tokens
    private int start = 0; // starting position per lexeme
    private int current = 0; // current position in file
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "NULL",null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            case '<'-> addToken(match('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '/' -> {
                // match tells us if next char
                // is also a /, if that is true,
                // we keep eating until the end of line
                if (match('/')){
                    while (peek() != '\n' && !isAtEnd()) advance();
                }else {
                    addToken(SLASH);
                }
            }
            case ' ', '\t', '\r' -> {}
            case '\n' -> line++;
            case '"' -> string();
            default -> {
                if (isDigit(c)){
                    number();
                }else if(isAlpha(c)){
                    identifier();
                }
                else {
                    // where is just for show
                    // remove it later
                    Lox.error(line,"Unexpected character");
                }
            }
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);

        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            // where is just for show
            Lox.error(line,"Undetermined String");
            return;
        }

        advance();
        String value = source.substring(start + 1, current -1);
        addToken(STRING,value);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
//        Because advance is called at starting
//        the current value becomes the next
//        character to be called
//        Hence when we need to look up the value
//        only current is required rather than
//        (current + 1)
        if (source.charAt(current) != expected) return false;
        current++; // this makes it that this one is in the
//        loop of scanToken if a match has been found
        return true;
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1); // this seek 2 character forward
        // that is current + 1
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current); // this just returns a
        // char that is current, current character is always
        // a character forward from character it was invoked
    }

    // alpha == alphabet
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }


    private char advance() {
        char c =  source.charAt(current); // this gets
        // the current character to be read
        current = current + 1; // this moves the cursor to
        // next character which will be retrieved in next advance cycle
        return c;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
