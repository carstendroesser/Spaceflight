package com.carstendroesser.spaceflight.scenes;

import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;

import static com.carstendroesser.spaceflight.managers.SceneManager.SceneType;
import static org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;

/**
 * Created by carstendrosser on 16.05.16.
 */
public class GamePlayingScene extends BaseScene {

    private AutoParallaxBackground mParallaxBackground;

    @Override
    public void create() {
        mEngine.registerUpdateHandler(new FPSLogger());

        mParallaxBackground = new AutoParallaxBackground(0, 0, 0, 40);
        mParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, SCREEN_HEIGHT - mResourceManager.mParallaxBackground.getHeight(), mResourceManager.mParallaxBackground, mVertexBufferObjectManager)));
        setBackground(mParallaxBackground);
    }

    @Override
    public void onBackKeyPressed() {
        mActivity.finish();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAME_PLAYING;
    }

    @Override
    public void disposeScene() {

    }
}
