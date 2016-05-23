package com.carstendroesser.spaceflight.scenes;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;

import static com.carstendroesser.spaceflight.managers.SceneManager.SceneType;
import static org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;

/**
 * Created by carstendrosser on 16.05.16.
 */
public class GamePlayingScene extends BaseScene implements IOnSceneTouchListener {

    private AutoParallaxBackground mParallaxBackground;
    private Sprite mSpaceship;

    private static final float TOUCH_POSITION_DELTA = 100;

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

        setOnSceneTouchListener(this);
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


    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        handleMove(pScene, pSceneTouchEvent);
        return false;
    }


    private void handleMove(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
            setPositionWithinBounds(mSpaceship, pSceneTouchEvent.getX() + TOUCH_POSITION_DELTA, pSceneTouchEvent.getY() - mSpaceship.getHeight() / 2);
        }
    }

    private void setPositionWithinBounds(Sprite object, float positionX, float positionY) {

        // check for right edge
        if (positionX >= SCREEN_WIDTH - object.getWidth()) {
            positionX = SCREEN_WIDTH - object.getWidth();
        }

        // check for bottom edge
        if (positionY >= SCREEN_HEIGHT - object.getHeight()) {
            positionY = SCREEN_HEIGHT - object.getHeight();
        }

        // check for top edge
        if (positionY < 0) {
            positionY = 0;
        }

        object.setPosition(positionX, positionY);
    }

}
