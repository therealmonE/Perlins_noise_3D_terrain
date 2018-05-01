package ru.therealmone.PerlinsNoise;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

class Window {
    private Scene scene;
    private Camera camera;

    private final Group root = new Group();
    private final Xform world = new Xform();
    private final Xform axisGroup = new Xform();

    private static final double AXIS_LENGTH = 100.0;

    private void buildCamera() {
        camera = new Camera();
        root.getChildren().add(camera.cameraXform);
        camera.handleMouse(scene);
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

    private void buildLights() {
        AmbientLight light0 = new AmbientLight();
        light0.setColor(Color.BLACK);
        light0.setVisible(true);

        PointLight light1 = new PointLight();
        light1.setTranslateY(800);
        light1.setTranslateX(2000);
        light1.setTranslateZ(500);
        light1.setColor(Color.WHITE);

        PointLight light2 = new PointLight();
        light2.setTranslateY(2000);
        light2.setTranslateX(800);
        light2.setTranslateZ(500);
        light2.setColor(Color.WHITE);

        world.getChildren().addAll(light0, light1, light2);
    }

    private void buildTerrain() {
        Shape3DTerrain terrain = new Shape3DTerrain();
        terrain.generatePoints(200, 20, new Point2D(-2000, -2000));
        MeshView meshView = new MeshView(terrain);
        meshView.setVisible(true);
        meshView.setCullFace(CullFace.NONE);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.SILVER);
        material.setSpecularColor(Color.rgb(255, 255, 150));
        material.setSpecularPower(20);

        meshView.setMaterial(material);

        terrain.initPoints();
        world.getChildren().addAll(meshView);
    }

    Scene getScene() {
        return scene;
    }

    Window() {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.SILVER);

        buildCamera();
        buildTerrain();
        buildLights();

        scene.setCamera(camera);
    }

}
