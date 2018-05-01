package ru.therealmone.PerlinsNoise;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;
import java.util.ArrayList;
import static ru.therealmone.PerlinsNoise.Noise.noise;

class Shape3DTerrain extends TriangleMesh {
    private ArrayList<Point3D> points = new ArrayList<>();
    private ArrayList<Point3D> faces = new ArrayList<>();

    private double xoff_interval = 0.04;
    private double yoff_interval = 0.04;
    private double seed = 0.01;

    Shape3DTerrain() {
        this.getTexCoords().addAll(1,1,1,0,0,0,0,1);
    }

    void generatePoints(int lines, int interval, Point2D startPoint) {
        double xoff = seed;
        double yoff = seed;

        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < lines; j++) {
                this.points.add(new Point3D(
                        j * interval + startPoint.getX(),
                        (float)noise(xoff, yoff) * 100,
                        i * interval + startPoint.getY()));
                xoff += xoff_interval;
            }
            xoff = 0.01;
            yoff += yoff_interval;
        }

        for (int i = 0; i < lines - 1; i++) {
            for (int j = 0; j < lines - 1; j++) {
                this.faces.add(new Point3D(
                        j + lines * i,
                        j + lines * (i + 1),
                        (j + 1) + lines * i));
                this.faces.add(new Point3D(
                        j + lines *(i + 1),
                        (j + 1) + lines * i,
                        (j + 1) + lines * (i + 1)));
            }
        }
    }

    void deletePoints() {
        this.points.clear();
        this.faces.clear();
    }

    void addPoint(float x, float y, float z) {
        this.points.add(new Point3D(x, y, z));
    }

    void addFace(int pointIndex1, int pointIndex2, int pointIndex3) {
        faces.add(new Point3D(pointIndex1, pointIndex2, pointIndex3));
    }

    void setXOFF(double value) {
        this.xoff_interval = value;
    }

    void setYOFF(double value) {
        this.yoff_interval = value;
    }

    void setSeed(double value) {
        this.seed = value;
    }

    void initPoints() {
        this.getPoints().clear();
        this.getFaces().clear();

        for(Point3D point: points) {
            this.getPoints().addAll((float)point.getX(), (float)point.getY(), (float)point.getZ());
        }

        for(Point3D face: faces) {
            this.getFaces().addAll(
                    (int)face.getX(), 1,
                    (int)face.getY(), 1,
                    (int)face.getZ(), 1);
        }
    }
}
