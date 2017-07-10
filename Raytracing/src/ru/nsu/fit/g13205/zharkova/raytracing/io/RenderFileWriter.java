package ru.nsu.fit.g13205.zharkova.raytracing.io;

import ru.nsu.fit.g13205.zharkova.raytracing.model.Camera;
import ru.nsu.fit.g13205.zharkova.raytracing.model.RenderSetting;
import ru.nsu.fit.g13205.zharkova.raytracing.model.Wireframe;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Yana on 29.05.16.
 */
public class RenderFileWriter {

    private File file;

    public RenderFileWriter(File file) {
        this.file = file;
    }

    public void write(RenderSetting renderSetting, double[][] sceneMatrix) throws FileNotFoundException {
        PrintWriter os= new PrintWriter(file.getAbsoluteFile());
        Color color = renderSetting.getColor();
        os.println(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
        os.println(renderSetting.getGamma());
        os.println(renderSetting.getDepth());
        os.println(renderSetting.getQuality());
        Camera camera = renderSetting.getCamera().rotateCamera(sceneMatrix);
        os.println(camera.getEye());
        os.println(camera.getView());
        os.println(camera.getUp());
        Wireframe w = renderSetting.getWireframe();
        os.println(w.getZn() + " " + w.getZf());
        os.println(w.getSw() + " " + w.getSh());
        os.flush();
        os.close();
    }
}
