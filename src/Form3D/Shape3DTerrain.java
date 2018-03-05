package Form3D;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

public class Shape3DTerrain extends TriangleMesh {
    private ArrayList<Point3D> points = new ArrayList<>();
    private ArrayList<Point2D> texCoord = new ArrayList<>();
    private ArrayList<Point3D> faces = new ArrayList<>();

    /**
     * points:
     * 1      3
     *  -------   texture:
     *  |\    |  1,1    1,0
     *  | \   |    -------
     *  |  \  |    |     | \
     *  |   \ |    |     |  \
     *  |    \|    ------ ----
     *  -------  0,1    0,0   0,-1
     * 0      2
     *
     * texture[3] 0,0 maps to vertex 2
     * texture[2] 0,1 maps to vertex 0
     * texture[0] 1,1 maps to vertex 1
     * texture[1] 1,0 maps to vertex 3
     *
     * Two triangles define rectangular faces:
     * p0, t0, p1, t1, p2, t2 // First triangle of a textured rectangle
     * p0, t0, p2, t2, p3, t3 // Second triangle of a textured rectangle
     */

    private Point3D point1;
    private Point3D point2;
    private Point3D point3;
    private Point3D point4;

    public Shape3DTerrain() {
        points.add(new Point3D(-10f, 0f, 10f));
        points.add(new Point3D(-10f, -10f, -10f));
        points.add(new Point3D(10f, 0f, 10f));

        texCoord.add(new Point2D(1, 1));
        texCoord.add(new Point2D(1, 0));
        texCoord.add(new Point2D(0, 1));

        faces.add(new Point3D(2, 1, 0));
    }

    public void addPoint(float x, float y, float z) {
        int closestXIndex = 0;
        int closestZIndex = 0;

        for(int i = 1; i < points.size(); i++) {
            if(Math.abs(x - points.get(i).getX()) < Math.abs(x - points.get(closestXIndex).getX()))
                closestXIndex = i;

            if(Math.abs(z - points.get(i).getZ()) < Math.abs(z - points.get(closestZIndex).getZ()))
                closestZIndex = i;
        }

        Point3D tempX = points.get(closestXIndex);
        Point3D tempZ = points.get(closestZIndex);
        Point2D tempCoordPoint = new Point2D(0,0);

        if(x < tempX.getX())
            tempCoordPoint.add(1,0);
        else
            tempCoordPoint.add(-1,0);

        if(z < tempZ.getZ())
            tempCoordPoint.add(0, -1);
        else
            tempCoordPoint.add(0, 1);

        points.add(new Point3D(x, y, z));
        texCoord.add(tempCoordPoint);
        faces.add(new Point3D(points.size() - 1, closestXIndex, closestZIndex));
    }

    public void initPoints() {
        System.out.println("initPoints");
        System.out.println("points: " + points.toString());
        System.out.println("texCoord: " + texCoord.toString());
        System.out.println("faces: " + faces.toString());
        for(Point3D point: points) {
            this.getPoints().addAll((float)point.getX(), (float)point.getY(), (float)point.getZ());
        }

        for(Point2D texCoord: texCoord) {
            this.getTexCoords().addAll((float)texCoord.getX(), (float)texCoord.getY());
        }

        for(Point3D face: faces) {
            this.getFaces().addAll(
                    (int)face.getX(), (int)face.getX(),
                    (int)face.getY(), (int)face.getY(),
                    (int)face.getZ(), (int)face.getZ());
        }

        System.out.println("initDone");
        System.out.println("points: " + this.getPoints().toString());
        System.out.println("texCoord: " + this.getTexCoords().toString());
        System.out.println("faces: " + this.getFaces().toString());
    }
}
