package com.carstendroesser.spaceflight.scenes;

import com.carstendroesser.spaceflight.activities.MainActivity;
import com.carstendroesser.spaceflight.managers.ResourceManager;
import com.carstendroesser.spaceflight.managers.SceneManager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import static com.carstendroesser.spaceflight.managers.SceneManager.SceneType;

/**
 * Created by carstendrosser on 16.05.16.
 */
public abstract class BaseScene extends Scene {

    protected final int SCREEN_WIDTH = MainActivity.mSceneWidth;
    protected final int SCREEN_HEIGHT = MainActivity.mSceneHeight;

    protected MainActivity mActivity;
    protected Engine mEngine;
    protected Camera mCamera;
    protected VertexBufferObjectManager mVertexBufferObjectManager;
    protected SceneManager mSceneManager;
    protected ResourceManager mResourceManager;

    public BaseScene() {
        mResourceManager = ResourceManager.getInstance();
        mActivity = mResourceManager.getActivity();
        mVertexBufferObjectManager = mActivity.getVertexBufferObjectManager();
        mEngine = mActivity.getEngine();
        mCamera = mEngine.getCamera();
        mSceneManager = SceneManager.getInstance();
        create();
    }

    public abstract void create();

    public abstract void onBackKeyPressed();

    public abstract SceneType getSceneType();

    public abstract void disposeScene();
}
