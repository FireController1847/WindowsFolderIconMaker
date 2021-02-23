package com.visualfiredev.wfim.ui.stage;

import com.visualfiredev.wfim.ui.scene.CommonScene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainStage implements IStageWrapper {

    // Stage Size
    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT = 550;
    private static final int DEF_WIDTH = 650;
    private static final int DEF_HEIGHT = 600;

    // Stage to Wrap
    private Stage stage;

    // Constructor
    public MainStage(Stage stage) {
        this.stage = stage;
        this.construct();
    }

    @Override
    public void construct() {
        this.stage.setTitle("WindowsFolderIconMaker");
        String[] icons = new String[] { "16", "20", "24", "32", "40", "48", "64", "256" };
        for (String icon : icons) {
            this.stage.getIcons().add(new Image("icons/" + icon + "px.png"));
        }
        this.stage.setMinWidth(MIN_WIDTH);
        this.stage.setMinHeight(MIN_HEIGHT);
        this.stage.setWidth(DEF_WIDTH);
        this.stage.setHeight(DEF_HEIGHT);
        this.stage.setOnHidden(event -> cleanup());
    }

    @Override
    public void cleanup() {
        this.getScene().cleanup();
        System.gc();
    }

    @Override
    public CommonScene<?> getScene() {
        return (CommonScene<?>) stage.getScene();
    }

    @Override
    public void setScene(CommonScene<?> scene) {
        if (this.getScene() != null) {
            this.getScene().cleanup();
            System.gc();
        }
        stage.setScene(scene);
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void hide() {
        stage.hide();
    }

    @Override
    public boolean isShowing() {
        return stage.isShowing();
    }

    @Override
    public void toFront() {
        stage.toFront();
    }

}
