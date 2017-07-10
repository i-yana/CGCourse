package ru.nsu.fit.g13205.zharkova.raytracing.io;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Yana on 14.05.16.
 */
public class Parser {

    public static String[] parse(int count, String line) throws IOException {
        if(line.contains("//")){
            line = line.substring(0, line.indexOf("//"));
        }
        if(line.split(" ").length != count){
            throw new IOException(line);
        }
        return line.split(" ");
    }

    static String getNextLine(BufferedReader br) throws IOException, ParserConfigurationException {
        String line;
        try {
            while (true) {
                line = br.readLine();
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.length() == 1) {
                    break;
                }
                if (line.substring(0, 2).equals("//")) {
                    continue;
                }
                break;
            }
            return line;
        }catch (NullPointerException e){
            throw new ParserConfigurationException("Invalid render file");
        }
    }

}
