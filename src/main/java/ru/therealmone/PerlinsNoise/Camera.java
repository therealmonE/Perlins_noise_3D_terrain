package ru.therealmone.PerlinsNoise;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;

class Camera extends PerspectiveCamera{
    final Xform cameraXform = new Xform();
    private final Xform cameraXform2 = new Xform();

    private static final double CAMERA_INITIAL_DISTANCE = -3000;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP  = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;

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

    Camera() {
        super(true);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(this);
        cameraXform2.setRotateZ(180.0);

        this.setNearClip(CAMERA_NEAR_CLIP);
        this.setFarClip(CAMERA_FAR_CLIP);
        this.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    void handleMouse(Scene scene) {
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
                        mouseDeltaX*modifier*modifier*ROTATION_SPEED);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                        mouseDeltaY*modifier*modifier*ROTATION_SPEED);
            }
            else if (me.isSecondaryButtonDown()) {
                double z = getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() +
                        mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                cameraXform2.t.setY(cameraXform2.t.getY() +
                        mouseDeltaY*Camera.MOUSE_SPEED*modifier*Camera.TRACK_SPEED);
            }
        });
    }
}
