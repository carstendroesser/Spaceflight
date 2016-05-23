package com.carstendroesser.spaceflight.scenes;

import org.andengine.entity.scene.Scene;
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
    private Sprite mSpaceship;

    @Override
    public void create() {
        mEngine.registerUpdateHandler(new FPSLogger());

        mParallaxBackground = new AutoParallaxBackground(0, 0, 0, 40);
        mParallaxBackground.attachParallaxEntity(
                new ParallaxEntity(
                        -5.0f,
                        new Sprite(0,
                                SCREEN_HEIGHT - mResourceManager.mParallaxBackground.getHeight(),
                                mResourceManager.mParallaxBackground,
                                mVertexBufferObjectManager)));
        setBackground(mParallaxBackground);

        mSpaceship = new Sprite(
                SCREEN_WIDTH / 2 - mResourceManager.mSpaceshipTextureRegion.getWidth() / 2,
                SCREEN_HEIGHT / 2 - mResourceManager.mSpaceshipTextureRegion.getHeight() / 2,
                mResourceManager.mSpaceshipTextureRegion,
                mVertexBufferObjectManager);
        mSpaceship.setZIndex(10);
        attachChild(mSpaceship);

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
