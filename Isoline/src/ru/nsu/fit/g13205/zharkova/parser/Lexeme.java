package ru.nsu.fit.g13205.zharkova.parser;

/**
 * Created by Yana on 18.02.16.
 */
public class Lexeme {
    public static final Lexeme PLUS;
    public static final Lexeme MINUS;
    public static final Lexeme MULTI;
    public static final Lexeme DIV;
    public static final Lexeme POWER;
    public static final Lexeme LEFT_BRK;
    public static final Lexeme RIGHT_BRK;
    public static final Lexeme X;
    public static final Lexeme Y;
    public static final Lexeme SIN;
    public static final Lexeme COS;
    public static final Lexeme EXP;
    public static final Lexeme EOF;
    private String lexeme;
    private LexemeType type;


    static {
        PLUS = new Lexeme("+", LexemeType.PLUS);
        MINUS = new Lexeme("-", LexemeType.MINUS);
        MULTI = new Lexeme("*", LexemeType.MULTI);
        DIV = new Lexeme("/", LexemeType.DIV);
        POWER = new Lexeme("^", LexemeType.POWER);
        LEFT_BRK = new Lexeme("(", LexemeType.LEFT_BKT);
        RIGHT_BRK = new Lexeme(")", LexemeType.RIGHT_BKT);
        X = new Lexeme("x", LexemeType.X);
        Y = new Lexeme("y", LexemeType.Y);
        SIN = new Lexeme("sin", LexemeType.SIN);
        COS = new Lexeme("cos", LexemeType.COS);
        EXP = new Lexeme("exp", LexemeType.EXP);
        EOF = new Lexeme("EOF", LexemeType.EOF);
    }

    public Lexeme(String lexeme, LexemeType type){
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public LexemeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return lexeme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lexeme lexeme1 = (Lexeme) o;

        if (lexeme != null ? !lexeme.equals(lexeme1.lexeme) : lexeme1.lexeme != null) return false;
        if (type != lexeme1.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lexeme != null ? lexeme.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
