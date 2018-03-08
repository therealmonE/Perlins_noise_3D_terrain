package Form3D;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    private final Group root = new Group();
    private final Xform world = new Xform();
    private final Xform axisGroup = new Xform();

    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();
    private final Xform cameraXform3 = new Xform();

    private Shape3DTerrain terrain = new Shape3DTerrain(new Point3D(-100, 0, -100));
    private Random random = new Random();

    private static final double CAMERA_INITIAL_DISTANCE = -250.0;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP  = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double AXIS_LENGTH = 100.0;

        private static final double CONTROL_MULTIPLIER = 0.1;
        private static final double SHIFT_MULTIPLIER = 10.0;
        private static final double MOUSE_SPEED = 0.1;
        private static final double ROTATION_SPEED = 2.0;
        private static final double TRACK_SPEED = 0.3;

        private double mousePosX;
        private double mousePosY;
        private double mouseOldX;
        private double mouseOldY;
        private double mouseDeltaX;
        private double mouseDeltaY;

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    private void handleMouse(Scene scene) {

        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 0.5;

            if (me.isControlDown()) {
                modifier = CONTROL_MULTIPLIER;
            }
            if (me.isShiftDown()) {
                modifier = SHIFT_MULTIPLIER;
            }
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                        mouseDeltaX*modifier*modifier*ROTATION_SPEED);  //
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                        mouseDeltaY*modifier*modifier*ROTATION_SPEED);  // -
            }
            else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                camera.setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() +
                        mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                cameraXform2.t.setY(cameraXform2.t.getY() +
                        mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
            }
        }); // setOnMouseDragged
    } //handleMouse

    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case P:
                    terrain.addPoint(random.nextInt(200) - 100, random.nextInt(20) - 10, random.nextInt(200) - 100);
                    terrain.initPoints();
                    break;
            }
        });
    }

    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);


        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(true);
        world.getChildren().addAll(axisGroup);
    }

    private void buildTerrain() {
        MeshView meshView = new MeshView(terrain);
        meshView.setVisible(true);
        meshView.setCullFace(CullFace.NONE);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GREY);
        material.setSpecularColor(Color.BLACK);

        meshView.setMaterial(material);

        terrain.initPoints();

        world.getChildren().addAll(meshView);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start()");

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCamera();
        buildAxes();
        buildTerrain();

        AmbientLight light = new AmbientLight();
        light.setTranslateY(15);
        light.setColor(Color.WHITE);
        world.getChildren().addAll(light);

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.BLACK);
        handleMouse(scene);
        handleKeyboard(scene);

        primaryStage.setTitle("Perlin's noize terrain");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(camera);
    }

    public static void main(String[] args) {
        launch(args);
    }



}
