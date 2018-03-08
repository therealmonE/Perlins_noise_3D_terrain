package Form3D;

import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

public class Shape3DTerrain extends TriangleMesh {
    private ArrayList<Point3D> points = new ArrayList<>();
    private ArrayList<Point3D> faces = new ArrayList<>();

    public Shape3DTerrain(Point3D firstPoint) {

        points.add(firstPoint);
        this.getTexCoords().addAll(1,1,1,0,0,0,0,1);

    }

    public void addPoint(float x, float y, float z) {

        int firstPoint = 0;
        Point3D tempPoint = points.get(0);
        Vec3d vec3d = new Vec3d(tempPoint.getX() - x, tempPoint.getY() - y, tempPoint.getZ() - z);

        for (int i = 1; i < points.size(); i++) {
            tempPoint = points.get(i);
            Vec3d tempVec = new Vec3d(tempPoint.getX() - x, tempPoint.getY() - y, tempPoint.getZ() - z);
            if (vec3d.length() > tempVec.length()) {
                vec3d = tempVec;
                firstPoint = i;
            }
        }

        int secondPoint = 0;
        tempPoint = points.get(0);
        vec3d.set(tempPoint.getX() - x, tempPoint.getY() - y, tempPoint.getZ() - z);

        for (int i = 1; i < points.size(); i++) {
            tempPoint = points.get(i);
            Vec3d tempVec = new Vec3d(tempPoint.getX() - x, tempPoint.getY() - y, tempPoint.getZ() - z);
            if(tempVec.length() < vec3d.length() && i != firstPoint) {
                vec3d = tempVec;
                secondPoint = i;
            }
        }

        points.add(new Point3D(x, y, z));
        //texCoord.add(new Point2D(1,1));
        faces.add(new Point3D(firstPoint, secondPoint, points.size() - 1));

    }

    public void initPoints() {

//        System.out.println("initPoints");
//        System.out.println("points: " + points.toString());
//        System.out.println("texCoord: " + texCoord.toString());
//        System.out.println("faces: " + faces.toString());

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

//        System.out.println("initDone");
//        System.out.println("points: " + this.getPoints().toString());
//        System.out.println("texCoord: " + this.getTexCoords().toString());
//        System.out.println("faces: " + this.getFaces().toString());

    }
}
