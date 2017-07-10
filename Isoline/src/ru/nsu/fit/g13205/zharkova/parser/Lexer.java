package ru.nsu.fit.g13205.zharkova.parser;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by Yana on 18.02.16.
 */
public class Lexer {

    private String reader;
    private int position;
    private String c;

    public Lexer(String reader) throws IOException {
        this.reader = reader.replaceAll(" ","");
        readNextSymbol();
    }

    public Lexeme getLexeme() throws IOException {
        switch (getCurrentSymbol()) {
            case "+":
                readNextSymbol();
                return Lexeme.PLUS;
            case "-":
                readNextSymbol();
                return Lexeme.MINUS;
            case "*":
                readNextSymbol();
                return Lexeme.MULTI;
            case "/":
                readNextSymbol();
                return Lexeme.DIV;
            case "^":
                readNextSymbol();
                return Lexeme.POWER;
            case "(":
                readNextSymbol();
                return Lexeme.LEFT_BRK;
            case ")":
                readNextSymbol();
                return Lexeme.RIGHT_BRK;
            case "x":
                readNextSymbol();
                return Lexeme.X;
            case "y":
                readNextSymbol();
                return Lexeme.Y;
            case "sin":
                readNextSymbol();
                return Lexeme.SIN;
            case "cos":
                readNextSymbol();
                return Lexeme.COS;
            case "exp":
                readNextSymbol();
                return Lexeme.EXP;
            case "$":
                return Lexeme.EOF;
            default:
                StringBuilder number = new StringBuilder();
                while (Character.isDigit(c.charAt(0))) {
                    number.append(c);
                    readNextSymbol();
                }
                if(Objects.equals(c, ".")){
                    number.append(c);
                    readNextSymbol();
                }
                while (Character.isDigit(c.charAt(0))) {
                    number.append(c);
                    readNextSymbol();
                }
                if (number.toString().isEmpty()) {
                    throw new IOException("unknown symbol " + getCurrentSymbol());
                }
                return new Lexeme(number.toString(), LexemeType.NUMBER);

        }
    }

    private void readNextSymbol() throws IOException {
        if (reader.length() > position) {
            c = reader.substring(position, position + 1);
            try {
                if (c.equals("s") || c.equals("c") || c.equals("e")) {
                    c = reader.substring(position, position + 3);
                    position = position + 3;
                } else {
                    position++;
                }
            } catch (StringIndexOutOfBoundsException e) {
                throw new IOException("unknown symbol "+c);
            }
        } else {
            c = "$";
        }
    }

    private String getCurrentSymbol() {
        return c;
    }

    public void init() throws IOException {
        position = 0;
        c = null;
        readNextSymbol();
    }
}
