package ru.nsu.fit.g13205.zharkova.wf.model;

/**
 * Created by Yana on 15.04.16.
 */
public class ViewParameters {

    private double zn,zf,sw,sh;

    public ViewParameters(double zn,double zf,double sw,double sh){
        this.zf = zf;
        this.sh = sh;
        this.sw = sw;
        this.zn = zn;
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

    public void setZF(double ZF) {
        this.zf = ZF;
    }

    public void setZN(double ZN) {
        this.zn = ZN;
    }

    public void setSW(double SW) {
        this.sw = SW;
    }

    public void setSH(double SH) {
        this.sh = SH;
    }

    public void addZN(double v) {
        this.zn=zn+v;
    }
}
