package ru.nsu.fit.g13205.zharkova.raytracing.io;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Point3D;
import ru.nsu.fit.g13205.zharkova.raytracing.model.RenderSetting;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Yana on 14.05.16.
 */
public class RenderFileReader {

    private File file;

    public RenderFileReader(File file) {
        this.file = file;
    }

    public RenderSetting read() throws IOException, ParserConfigurationException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] oneLine;
        oneLine = Parser.parse(3, Parser.getNextLine(br));
        Color bg = new Color(Integer.parseInt(oneLine[0]), Integer.parseInt(oneLine[1]), Integer.parseInt(oneLine[2]));
        double gamma = Double.parseDouble(Parser.parse(1, Parser.getNextLine(br))[0]);
        int depth = Integer.parseInt(Parser.parse(1, Parser.getNextLine(br))[0]);
        String quality = Parser.parse(1, Parser.getNextLine(br))[0];

        oneLine = Parser.parse(3, Parser.getNextLine(br));
        Point3D eye = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));

        oneLine = Parser.parse(3, Parser.getNextLine(br));
        Point3D view = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));

        oneLine = Parser.parse(3, Parser.getNextLine(br));
        Point3D up = new Point3D(Double.parseDouble(oneLine[0]), Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]));

        oneLine = Parser.parse(2, Parser.getNextLine(br));
        double zn = Double.parseDouble(oneLine[0]);
        double zf = Double.parseDouble(oneLine[1]);

        oneLine = Parser.parse(2, Parser.getNextLine(br));
        double sw = Double.parseDouble(oneLine[0]);
        double sh = Double.parseDouble(oneLine[1]);

        return new RenderSetting(bg, gamma, depth, quality, eye, view, up, zn, zf, sw, sh);
    }
}