package com.visualfiredev.wfim.ui.scene;

import com.visualfiredev.wfim.WindowsFolderIconMaker;
import com.visualfiredev.wfim.ui.stage.IStageWrapper;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public abstract class CommonScene<L extends Parent> extends Scene implements IView {

    // Application Reference
    protected WindowsFolderIconMaker application;

    // Stage Reference
    protected IStageWrapper stage;

    // Layout
    protected L layout;

    // Constructor
    @SuppressWarnings("unchecked")
    public CommonScene(IStageWrapper owner, L root) {
        super(root);

        // Set Application Reference
        this.application = WindowsFolderIconMaker.getInstance();

        // Set Stage
        this.stage = owner;

        // Set Layout
        this.layout = (L) this.getRoot();
        this.layout.setId("layout");

        // Refresh Keybind
        this.getAccelerators().put(new KeyCodeCombination(KeyCode.F5, KeyCombination.SHIFT_DOWN), this::refresh);
    }

    // Stylize and register during construction
    @Override
    public void construct() {
        stylize();
        register();
    }

    /**
     * Loads in the stylesheets (or manually applies styles) needed for this scene.
     * TODO: I really need to cache these
     */
    public abstract void stylize();

    // Cleanup during refresh
    @Override
    public void refresh() {
        cleanup();
    }

    /**
     * Utility function for loading a stylesheet.
     * @param fileName The file name of the stylesheet to be loaded.
     */
    protected void loadStylesheet(String fileName) {
        this.getStylesheets().add(this.getClass().getResource("/styles/" + fileName).toExternalForm());
    }

    // Getters
    public L getLayout() {
        return layout;
    }

}
