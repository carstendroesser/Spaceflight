package com.carstendroesser.spaceflight.managers;

import com.carstendroesser.spaceflight.scenes.BaseScene;
import com.carstendroesser.spaceflight.scenes.GamePlayingScene;

/**
 * Created by carstendrosser on 16.05.16.
 */
public class SceneManager {

    public enum SceneType {
        MENU,
        GAME_PLAYING,
        GAME_OVER
    }

    private static final SceneManager instance = new SceneManager();

    private BaseScene mCurrentScene;
    private SceneType mCurrentSceneType;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        return instance;
    }

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

    private void setScene(BaseScene pScene) {
        ResourceManager.getInstance().getActivity().getEngine().setScene(pScene);
        mCurrentScene = pScene;
        mCurrentSceneType = pScene.getSceneType();
    }

    public SceneType getCurrentSceneType() {
        return mCurrentSceneType;
    }

    public BaseScene getCurrentScene() {
        return mCurrentScene;
    }

}
