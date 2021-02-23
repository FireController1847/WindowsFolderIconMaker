package com.visualfiredev.wfim.ui.scene;

import com.visualfiredev.wfim.IconCreator;
import com.visualfiredev.wfim.ui.stage.IStageWrapper;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MainScene extends CommonScene<VBox> {

    // Logic Variables
    private Path sourceFile = null; // The source picture
    private Path outputFile = null; // The temporary output file
    private BufferedImage[] generatedSizes = null; // The generated sizes

    // File Chooser
    private HBox contSourceFile;
    private Label lblSourceFile;
    private TextField textCurrSourceFile;
    private Button btnChooseSourceFile;

    // Separator
    private VBox separator1;

    // Create Icon
    private HBox contCreateIcon;
    private Button btnCreateIcon;

    // Preview Icon
    private HBox contPreviewIcon;
    private ImageView igvPreviewIcon;

    // Separator
    private VBox separator2;

    // Save Button
    private HBox contSaveIcon;
    private Button btnSaveIcon;

    // Status
    private HBox contStatus;
    private Label lblStatus;

    // Constructor
    public MainScene(IStageWrapper owner) {
        super(owner, new VBox());
        this.construct();
    }

    @Override
    public void construct() {
        this.constructSourceFile();
        this.constructRest();
        super.construct();
    }

    private void constructSourceFile() {
        contSourceFile = new HBox();
        contSourceFile.setId("cont-source-file");
        contSourceFile.setAlignment(Pos.CENTER);

        lblSourceFile = new Label("Source Image: ");
        lblSourceFile.setId("label-source-file");
        HBox.setHgrow(lblSourceFile, Priority.NEVER);

        textCurrSourceFile = new TextField();
        textCurrSourceFile.setId("text-curr-source-file");
        textCurrSourceFile.setDisable(true);
        HBox.setHgrow(textCurrSourceFile, Priority.SOMETIMES);

        btnChooseSourceFile = new Button("Choose Source File...");
        btnChooseSourceFile.setId("btn-choose-source-file");
        btnChooseSourceFile.setDefaultButton(true);
        btnChooseSourceFile.setOnAction(this::onChooseSourceFile);
    }

    private void constructRest() {
        contCreateIcon = new HBox();
        contCreateIcon.setId("cont-create-icon");
        contCreateIcon.setAlignment(Pos.CENTER);

        btnCreateIcon = new Button("Create Icon...");
        btnCreateIcon.setId("btn-create-icon");
        btnCreateIcon.setOnAction(this::onCreateIcon);

        separator1 = new VBox();
        separator1.setId("sep1");
        VBox.setVgrow(separator1, Priority.ALWAYS);

        contPreviewIcon = new HBox();
        contPreviewIcon.setId("cont-preview-icon");
        contPreviewIcon.setAlignment(Pos.CENTER);

        igvPreviewIcon = new ImageView("/icons/256px.png");
        igvPreviewIcon.setId("igv-preview-icon");
        igvPreviewIcon.setPreserveRatio(true);
        igvPreviewIcon.setFitHeight(256);

        separator2 = new VBox();
        separator2.setId("sep2");
        VBox.setVgrow(separator2, Priority.ALWAYS);

        contSaveIcon = new HBox();
        contSaveIcon.setId("cont-save-icon");
        contSaveIcon.setAlignment(Pos.CENTER);

        btnSaveIcon = new Button("Save Icon...");
        btnSaveIcon.setId("btn-save-icon");
        btnSaveIcon.setOnAction(this::onSaveIcon);

        contStatus = new HBox();
        contStatus.setId("cont-status");
        contStatus.setAlignment(Pos.CENTER);

        lblStatus = new Label();
        lblStatus.setId("label-status");
    }

    @Override
    public void stylize() {
        this.loadStylesheet("Colors.css");
        this.loadStylesheet("Components.css");
        this.loadStylesheet("scene/MainScene.css");
    }

    @Override
    public void register() {
        // Source File
        contSourceFile.getChildren().addAll(lblSourceFile, textCurrSourceFile, btnChooseSourceFile);

        // Create Icon
        contCreateIcon.getChildren().add(btnCreateIcon);

        // Preview Image
        contPreviewIcon.getChildren().addAll(igvPreviewIcon);

        // Save Icon
        contSaveIcon.getChildren().add(btnSaveIcon);

        // Status
        contStatus.getChildren().add(lblStatus);

        // Layout
        layout.getChildren().addAll(contSourceFile, contCreateIcon, separator1, contPreviewIcon, separator2, contSaveIcon, contStatus);
    }

    @Override
    public void refresh() {
        // Clear
        layout.getChildren().clear();
        this.getStylesheets().clear();
        super.refresh();

        // Reconstruct
        this.construct();
    }

    @Override
    public void cleanup() {

    }

    // Handles the "choose source file" button event
    private void onChooseSourceFile(ActionEvent event) {
        // Create File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Source File...");

        // Set Home
        File home;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            home = new File(System.getProperty("user.home") + "\\Desktop").getAbsoluteFile();
        } else {
            home = new File(System.getProperty("user.home")).getAbsoluteFile();
        }
        fileChooser.setInitialDirectory(home);

        // Create Extension Filter
        fileChooser.getExtensionFilters().clear();
        FileChooser.ExtensionFilter filterPng = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(filterPng);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SVG files (*.svg)", "*.svg"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ICO files (*.ico)", "*.ico"));
        fileChooser.setSelectedExtensionFilter(filterPng);

        // Show Dialog
        File temp = fileChooser.showOpenDialog(this.getWindow());
        if (temp == null) {
            return;
        }
        sourceFile = temp.toPath();

        // Update Text Dialog
        textCurrSourceFile.setText(sourceFile.toAbsolutePath().toString());
    }

    // Handles the "save icon" button event
    private void onSaveIcon(ActionEvent event) {
        if (outputFile == null) {
            lblStatus.setText("There is nothing to save!");
            lblStatus.setTextFill(Color.RED);
            return;
        }
        btnSaveIcon.setDisable(true);

        // Create File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Icon File...");

        // Set Home
        File home;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            home = new File(System.getProperty("user.home") + "\\Desktop").getAbsoluteFile();
        } else {
            home = new File(System.getProperty("user.home")).getAbsoluteFile();
        }
        fileChooser.setInitialDirectory(home);

        // Create Extension Filter
        fileChooser.getExtensionFilters().clear();
        FileChooser.ExtensionFilter filterIco = new FileChooser.ExtensionFilter("ICO files (*.ico)", "*.ico");
        fileChooser.getExtensionFilters().add(filterIco);
        fileChooser.setSelectedExtensionFilter(filterIco);

        // Set Default Name
        fileChooser.setInitialFileName("icon.ico");

        // Show Dialog
        File temp = fileChooser.showSaveDialog(this.getWindow());
        if (temp == null) {
            // Update Button Text
            Platform.runLater(() -> {
                btnSaveIcon.setDisable(false);
            });
            return;
        }
        Path finalOutputFile = temp.toPath();

        // Write File
        new Thread(() -> {
            try {
                Files.write(finalOutputFile, Files.readAllBytes(outputFile), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (Exception e) {
                Platform.runLater(() -> {
                    this.onCreateIconErrored(e);
                });
                return;
            }

            // Update Button Text
            Platform.runLater(() -> {
                lblStatus.setText("File saved to: " + finalOutputFile.toAbsolutePath().toString());
                lblStatus.setTextFill(Color.GREEN);
                btnSaveIcon.setDisable(false);
            });
        }).start();
    }

    // Handles the "create icon" button event
    private void onCreateIcon(ActionEvent event) {
        if (sourceFile == null) {
            lblStatus.setText("Invalid source file!");
            lblStatus.setTextFill(Color.RED);
            return;
        }

        btnCreateIcon.setDisable(true);
        lblStatus.setText("Loading...");
        lblStatus.setTextFill(Color.BLACK);

        (new Thread(() -> {
            // Loading...
            IconCreator creator = new IconCreator();
            try {
                creator.loadSource(sourceFile);
                creator.loadTemplates();
            } catch (Exception e) {
                Platform.runLater(() -> onCreateIconErrored(e));
                return;
            }

            // Processing...
            Platform.runLater(() -> {
                lblStatus.setText("Processing...");
            });
            try {
                Pair<Path, BufferedImage[]> temp = creator.createIcon();
                outputFile = temp.getKey();
                generatedSizes = temp.getValue();
            } catch (Exception e) {
                Platform.runLater(() -> this.onCreateIconErrored(e));
                return;
            }

            // Complete!
            Platform.runLater(() -> {
                lblStatus.setText("Done!");
                lblStatus.setTextFill(Color.BLACK);
                btnCreateIcon.setDisable(false);
                igvPreviewIcon.setImage(SwingFXUtils.toFXImage(generatedSizes[generatedSizes.length - 1], null));
            });
        })).start();
    }

    // Handles if creating the icon errored
    private void onCreateIconErrored(Exception e) {
        e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Severe Internal Error");
        alert.setHeaderText("There was a severe internal error");
        alert.setContentText(
            "There was a severe internal error and the program will now close. Please contact the developer.\n\n" +
            e.toString() + "\n     at " +
            Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n     at ")));
        alert.setResizable(true);
        alert.setResult(ButtonType.OK);
        alert.getDialogPane().setMinWidth(600);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setOnCloseRequest(event -> {
            Platform.exit();
        });
        alert.show();

        Toolkit.getDefaultToolkit().beep();
    }

}
