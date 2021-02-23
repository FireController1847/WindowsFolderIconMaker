package com.visualfiredev.wfim.ui.stage;

import com.visualfiredev.wfim.ui.scene.CommonScene;

public interface IStageWrapper {

    // Custom Methods
    void construct();
    void cleanup();

    // Wrapped Methods
    CommonScene<?> getScene();
    void setScene(CommonScene<?> scene);
    void show();
    void hide();
    boolean isShowing();
    void toFront();

}
