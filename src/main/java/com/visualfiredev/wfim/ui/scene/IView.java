package com.visualfiredev.wfim.ui.scene;

public interface IView {

    /**
     * Constructs the UI and prepares it for becoming visible.
     */
    void construct();

    /**
     * Registers all components in the order in which this scene needs them.
     */
    void register();

    /**
     * Removes all components in the scene and reconstructs it.
     */
    void refresh();

    /**
     * Cleans up any threads or things that may have been created as this scene is now closing.
     */
    void cleanup();

}
