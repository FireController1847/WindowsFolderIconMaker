package com.visualfiredev.wfim;

import com.visualfiredev.wfim.ui.scene.MainScene;
import com.visualfiredev.wfim.ui.stage.MainStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class WindowsFolderIconMaker extends Application {

    // Singleton
    private static WindowsFolderIconMaker instance;

    // Template Files
    private BufferedImage sixteenx;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(true);
        instance = this;

        Parameters params = this.getParameters();
        List<String> unnamedParams = params.getUnnamed();
        Map<String, String> namedParams = params.getNamed();
        if (unnamedParams.contains("--help") || unnamedParams.contains("/help")) {
            System.out.println("Valid arguments: ");
            System.out.printf("%-10s %4s %s\n", "--noui", "::", "Launches the application without the UI.");
            System.out.printf("%-10s %4s %s\n", "--source", "::", "The input file. Must be PNG, SVG, or ICO. [Required for --noui]");
            System.out.printf("%-10s %4s %s\n", "--out", "::", "The output file. Must end in ICO. [Required for --noui]");
            System.exit(1);
            return;
        }

        if (unnamedParams.contains("--noui") || unnamedParams.contains("/noui")) {
            try {
                if (!namedParams.containsKey("source")) {
                    (new Exception("Invalid source file! Run the program with --help to see CLI usage.")).printStackTrace();
                    Platform.exit();
                    return;
                }
                if (!namedParams.containsKey("out")) {
                    (new Exception("Invalid out file! Run the program with --help to see CLI usage.")).printStackTrace();
                    Platform.exit();
                    return;
                }

                Path sourceFile = Paths.get(namedParams.get("source"));
                Path outFile = Paths.get(namedParams.get("out"));

                if (!outFile.toString().endsWith(".ico")) {
                    (new Exception("Out file must end in .ico!")).printStackTrace();
                    Platform.exit();
                    return;
                }

                System.out.println("Loading...");
                IconCreator creator = new IconCreator();
                creator.loadSource(sourceFile);
                creator.loadTemplates();

                System.out.println("Processing...");
                Path tempFile = creator.createIcon().getKey();

                System.out.println("Saving...");
                Files.write(outFile, Files.readAllBytes(tempFile), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

                System.out.println("Icon successfully created at " + outFile.toAbsolutePath().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Platform.exit();
            return;
        }

        MainStage stage = new MainStage(primaryStage);
        stage.setScene(new MainScene(stage));
        stage.show();
    }

    // Getters
    public static WindowsFolderIconMaker getInstance() {
        return instance;
    }

}
