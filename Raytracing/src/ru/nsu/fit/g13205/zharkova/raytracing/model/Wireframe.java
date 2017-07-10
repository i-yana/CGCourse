package ru.nsu.fit.g13205.zharkova.raytracing.model;

import ru.nsu.fit.g13205.zharkova.raytracing.model.geometry.Function;

/**
 * Created by Yana on 14.05.16.
 */
public class Wireframe {

    private double zn, zf, sw, sh;
    private Function f;

    public Wireframe(double zn, double zf, double sw, double sh){
        this.zn = zn;
        this.zf = zf;
        this.sw = sw;
        this.sh = sh;
    }

    public double getZn() {
        return zn;
    }

    public double getZf() {
        return zf;
    }

    public double getSw() {
        return sw;
    }

    public double getSh() {
        return sh;
    }

    public void setZN(double ZN) {
        this.zn = ZN;
    }

    public void setZF(double ZF) {
        this.zf = ZF;
    }

    public void setSW(double SW) {
        this.sw = SW;
    }

    public void setSH(double SH) {
        this.sh = SH;
    }

    public void mulZN(double v){
        zn=zn*v;
    }

    public void divZN(double v) {
        zn = zn/v;
    }

    public void setFunction(Function f) {
        this.f = f;
    }

    public Function getF() {
        return f;
    }
}
