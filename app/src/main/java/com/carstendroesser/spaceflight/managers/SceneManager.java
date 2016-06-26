package com.carstendroesser.spaceflight.managers;

import com.carstendroesser.spaceflight.scenes.BaseScene;
import com.carstendroesser.spaceflight.scenes.GamePlayingScene;

/**
 * Created by carstendrosser on 16.05.16.
 */
public class SceneManager {

    /**
     * Enum to define types of Scenes.
     */
    public enum SceneType {
        MENU,
        GAME_PLAYING,
        GAME_OVER
    }

    // single instance
    private static final SceneManager instance = new SceneManager();

    private BaseScene mCurrentScene;
    private SceneType mCurrentSceneType;

    /**
     * Private constructor to have it a singleton.
     */
    private SceneManager() {
        // empty!
    }

    /**
     * Returns the single instance of the SceneManager.
     *
     * @return SceneManager the single instance
     */
    public static SceneManager getInstance() {
        return instance;
    }

    /**
     * Sets the currently displayed scenetype.
     *
     * @param pSceneType the type of the current scene
     */
    public void setScene(SceneType pSceneType) {
        switch (pSceneType) {
            case MENU:
                //TODO
                break;
            case GAME_PLAYING:
                setScene(new GamePlayingScene());
                break;
            case GAME_OVER:
                //TODO
                break;
        }
    }

    /**
     * Sets a scene to display it by the engine.
     *
     * @param pScene the scene to display
     */
    private void setScene(BaseScene pScene) {
        ResourceManager.getInstance().getActivity().getEngine().setScene(pScene);
        mCurrentScene = pScene;
        mCurrentSceneType = pScene.getSceneType();
    }

    /**
     * Gets the type of the currently displayed scene.
     *
     * @return SceneType the type of the scene
     */
    public SceneType getCurrentSceneType() {
        return mCurrentSceneType;
    }

    /**
     * Gets the currently displayed Scene.
     *
     * @return Scene the scene
     */
    public BaseScene getCurrentScene() {
        return mCurrentScene;
    }

}
