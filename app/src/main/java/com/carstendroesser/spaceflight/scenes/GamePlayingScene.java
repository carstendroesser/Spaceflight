package com.carstendroesser.spaceflight.scenes;

import com.carstendroesser.spaceflight.managers.ResourceManager;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;

import static com.carstendroesser.spaceflight.managers.SceneManager.SceneType;
import static org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;

/**
 * Created by carstendrosser on 16.05.16.
 */
public class GamePlayingScene extends BaseScene implements IOnSceneTouchListener {

    private AutoParallaxBackground mParallaxBackground;
    private Sprite mSpaceship;
    private int mTimerCounter = 0;
    private int mGap = 0;
    private ArrayList<Sprite> mEnemies;
    private boolean mInputEnabled;


    private static final float TOUCH_POSITION_DELTA = 100;
    private static final int ENEMY_GAP_BETWEEN_WAVES = 2;
    private static final int ENEMIES_PER_WAVE = 4;

    @Override
    public void create() {
        mEngine.registerUpdateHandler(new FPSLogger());

        mEnemies = new ArrayList<Sprite>();

        mInputEnabled = true;

        mParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        mParallaxBackground.attachParallaxEntity(
                new ParallaxEntity(
                        -5.0f,
                        new Sprite(0,
                                SCREEN_HEIGHT - mResourceManager.mParallaxBackground.getHeight(),
                                mResourceManager.mParallaxBackground,
                                mVertexBufferObjectManager)));
        setBackground(mParallaxBackground);

        mSpaceship = new Sprite(
                SCREEN_WIDTH / 2 - mResourceManager.mTextureRegionSpaceship.getWidth() / 2,
                SCREEN_HEIGHT / 2 - mResourceManager.mTextureRegionSpaceship.getHeight() / 2,
                mResourceManager.mTextureRegionSpaceship,
                mVertexBufferObjectManager);
        mSpaceship.setZIndex(10);
        attachChild(mSpaceship);

        setOnSceneTouchListener(this);

        registerUpdateHandler(new TimerHandler(0.8f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                if (mGap > 0) {
                    mGap--;
                    return;
                }

                mTimerCounter++;
                if (mTimerCounter % ENEMIES_PER_WAVE == 0) {
                    mGap = ENEMY_GAP_BETWEEN_WAVES;
                }

                spawnEnemy();
            }
        }));

        registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                checkCollisions();
            }

            @Override
            public void reset() {

            }
        });
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
        if (mInputEnabled) {
            handleMove(pScene, pSceneTouchEvent);
        }
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

    private void spawnEnemy() {
        Sprite enemy = new Sprite(
                0,
                0,
                mResourceManager.mTextureRegionEnemy,
                mVertexBufferObjectManager);
        enemy.setZIndex(9);

        mEnemies.add(enemy);

        float x = SCREEN_WIDTH;
        float y = (float) (Math.random() * (SCREEN_HEIGHT - enemy.getHeight()));

        enemy.setPosition(x, y);

        MoveXModifier moveXModifier = new MoveXModifier(5, enemy.getX(), -enemy.getWidth());
        moveXModifier.addModifierListener(new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                // empty
            }

            @Override
            public void onModifierFinished(IModifier<IEntity> pModifier, final IEntity pItem) {
                ResourceManager.getInstance().getActivity().getEngine().runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        mEnemies.remove(pItem);
                        detachChild(pItem);
                    }
                });
            }
        });

        enemy.registerEntityModifier(moveXModifier);

        attachChild(enemy);
    }

    private void checkCollisions() {
        for (Sprite enemy : mEnemies) {
            if (mSpaceship.collidesWith(enemy)) {
                onSpaceShipCollision(enemy);
            }
        }
    }

    private void onSpaceShipCollision(final Sprite pEnemy) {
        ResourceManager.getInstance().getActivity().getEngine().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                pEnemy.clearEntityModifiers();
                mEnemies.remove(pEnemy);
                detachChild(pEnemy);
                mInputEnabled = false;
            }
        });
    }

}
