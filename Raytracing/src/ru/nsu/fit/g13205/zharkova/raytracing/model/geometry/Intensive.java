package ru.nsu.fit.g13205.zharkova.raytracing.model.geometry;

/**
 * Created by Yana on 30.05.16.
 */
public class Intensive {

    public float red,green,blue;

    public Intensive(float r, float g, float b){
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public Intensive add(Intensive a) {
        return new Intensive(this.red+a.red, this.green+a.green, this.blue+a.blue);
    }

    public Intensive mul(Intensive a) {
        return new Intensive(this.red*a.red, this.green*a.green, this.blue*a.blue);
    }

    public Intensive mul(float koeff) {
        return new Intensive(this.red*koeff, this.green*koeff, this.blue*koeff);
    }

    @Override
    public String toString() {
        return "Intensive{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Intensive intensive = (Intensive) o;

        if (Float.compare(intensive.blue, blue) != 0) return false;
        if (Float.compare(intensive.green, green) != 0) return false;
        if (Float.compare(intensive.red, red) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (red != +0.0f ? Float.floatToIntBits(red) : 0);
        result = 31 * result + (green != +0.0f ? Float.floatToIntBits(green) : 0);
        result = 31 * result + (blue != +0.0f ? Float.floatToIntBits(blue) : 0);
        return result;
    }
}
