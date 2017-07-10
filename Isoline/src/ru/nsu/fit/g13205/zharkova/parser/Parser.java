package ru.nsu.fit.g13205.zharkova.parser;

import java.io.IOException;

/**
 * Created by Yana on 18.02.16.
 */
public class Parser {

    private Lexer lexer;
    private Lexeme currentLexeme;
    private double x;
    private double y;

    public Parser(Lexer lexer, double x, double y) throws IOException {
        this.x = x;
        this.y = y;
        this.lexer = lexer;
        this.lexer.init();
        currentLexeme = lexer.getLexeme();
    }

    public double parse() throws IOException, ParserException {
        if(currentLexeme == Lexeme.EOF){
            throw new ParserException("empty input");
        }

        double answer = parseExpression();
        if(!(currentLexeme == Lexeme.EOF)){
            throw  new ParserException(currentLexeme.toString());
        }
        return answer;
    }

    private double parseExpression() throws ParserException, IOException {
        double answer = parseTerm();
        Lexeme operation;
        while (currentLexeme == Lexeme.PLUS || currentLexeme == Lexeme.MINUS){
            operation = currentLexeme;
            currentLexeme = lexer.getLexeme();
            double temp = parseTerm();
            switch (operation.getType()){
                case MINUS:
                    answer = answer - temp;
                    break;
                case PLUS:
                    answer = answer + temp;
                    break;
            }
        }
        return answer;
    }


    private double parseTerm() throws ParserException, IOException {
        double answer = parseFactor();
        Lexeme operation;
        while(currentLexeme == Lexeme.MULTI || currentLexeme == Lexeme.DIV){
            operation = currentLexeme;
            currentLexeme = lexer.getLexeme();
            double temp = parseFactor();
            switch (operation.getType()){
                case MULTI:
                    answer = answer*temp;
                    break;
                case DIV:
                    if(temp == 0.0){
                        throw new ArithmeticException("zero division");
                    }
                    else {
                        answer = answer/temp;
                        break;
                    }
            }
        }
        return answer;
    }

    private double parseFactor() throws ParserException, IOException {
        double answer = parsePower();
        if(currentLexeme == Lexeme.POWER){
            currentLexeme = lexer.getLexeme();
            double temp = parseFactor();
            if(temp == 0.0){
                answer = 1.0;
                return  answer;
            }
            answer = Math.pow(answer, temp);
        }
        return answer;
    }

    private double parsePower() throws ParserException, IOException {
        Lexeme operation = currentLexeme;
        if(operation == Lexeme.MINUS){
            currentLexeme = lexer.getLexeme();
        }
        double answer = parseAtom();
        if (operation == Lexeme.MINUS) {
            answer = -answer;
        }
        return answer;
    }

    private double parseAtomOnBrackets() throws IOException, ParserException {
        double answer;
        if(currentLexeme == Lexeme.LEFT_BRK){
            currentLexeme = lexer.getLexeme();
            answer = parseExpression();
            if(!(currentLexeme == Lexeme.RIGHT_BRK)){
                throw new ParserException("expected ')'");
            }
            currentLexeme = lexer.getLexeme();
        }
        else {
            throw new ParserException("expected '('");
        }
        return answer;
    }

    private double parseAtom() throws ParserException, IOException {
        double answer;
        if(currentLexeme == Lexeme.LEFT_BRK){
            currentLexeme = lexer.getLexeme();
            answer = parseExpression();
            if(!(currentLexeme == Lexeme.RIGHT_BRK)){
                throw new ParserException("expected ')'");
            }
            currentLexeme = lexer.getLexeme();
        }
        else if(currentLexeme == Lexeme.SIN){
            currentLexeme = lexer.getLexeme();
            answer = Math.sin(parseAtomOnBrackets());
        }
        else if(currentLexeme == Lexeme.COS){
            currentLexeme = lexer.getLexeme();
            answer = Math.cos(parseAtomOnBrackets());
        }
        else if(currentLexeme == Lexeme.EXP){
            currentLexeme = lexer.getLexeme();
            answer = Math.exp(parseAtomOnBrackets());
        }
        else {
            answer = parseNumber();
        }
        return answer;
    }

    private double parseNumber() throws ParserException, IOException {

            if(currentLexeme==Lexeme.X){
                currentLexeme = lexer.getLexeme();
                return x;
            }
            if(currentLexeme==Lexeme.Y){
                currentLexeme = lexer.getLexeme();
                return y;
            }
            if(currentLexeme.getType().equals(LexemeType.NUMBER)){
                double answer = Double.parseDouble(currentLexeme.getLexeme());
                currentLexeme = lexer.getLexeme();
                return answer;
            }

        throw new ParserException(currentLexeme.toString());
    }
}
