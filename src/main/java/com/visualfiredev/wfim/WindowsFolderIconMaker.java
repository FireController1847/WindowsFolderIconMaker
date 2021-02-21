package com.visualfiredev.wfim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class WindowsFolderIconMaker extends Application {

    // Template Files
    private BufferedImage sixteenx;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("!! WARNING: YOU ARE RUNNING A DEVELOPMENT BUILD !!");

        Parameters params = this.getParameters();
        Map<String, String> namedParams = params.getNamed();
        if (namedParams.containsKey("file")) {
            String fileParam = namedParams.get("file");
            if (fileParam.isEmpty() || fileParam.isBlank()) {
                throw new Exception("Invalid file parameter!");
            }

            System.out.println("Loading...");

            IconCreator creator = new IconCreator();
            creator.loadTemplates();
            creator.loadSource(new File(namedParams.get("file")));

            System.out.println("Processing...");

            creator.createIcon();

            System.out.println("Icon successfully created at run directory named icon.ico");
            Platform.exit();
        } else {
            // TODO: Launch UI
            throw new Exception("The UI for this program has not been implemented. Please use a CLI!");
        }
    }

}
