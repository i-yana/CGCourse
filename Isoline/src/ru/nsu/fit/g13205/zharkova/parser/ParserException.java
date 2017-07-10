package ru.nsu.fit.g13205.zharkova.parser;

/**
 * Created by Yana on 19.02.16.
 */
public class ParserException extends Exception {

    public ParserException(String reason){
        super("syntax error: " + reason);
    }


}
