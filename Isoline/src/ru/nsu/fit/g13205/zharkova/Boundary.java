package ru.nsu.fit.g13205.zharkova;

/**
 * Created by Yana on 29.03.16.
 */
public class Boundary {

    private double a;
    private double b;
    private double c;
    private double d;

    public static final double DEFAULT_A = -1.;
    public static final double DEFAULT_B = 1.;
    public static final double DEFAULT_C = -1.;
    public static final double DEFAULT_D = 1.;

    public Boundary(double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Boundary(){
        this.a = DEFAULT_A;
        this.b = DEFAULT_B;
        this.c = DEFAULT_C;
        this.d = DEFAULT_D;
    }

    public Boundary(Boundary boundary) {
        this.a = boundary.a;
        this.b = boundary.b;
        this.c = boundary.c;
        this.d = boundary.d;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getD() {
        return d;
    }
}
