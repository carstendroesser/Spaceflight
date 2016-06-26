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
import org.andengine.entity.sprite.AnimatedSprite;
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
    private boolean mCollisionEnabled;
    private AnimatedSprite mExplosion;

    private static final float TOUCH_POSITION_DELTA = 100;
    private static final int ENEMY_GAP_BETWEEN_WAVES = 2;
    private static final int ENEMIES_PER_WAVE = 4;

    @Override
    public void create() {
        mEngine.registerUpdateHandler(new FPSLogger());

        // list for all enemies
        mEnemies = new ArrayList<Sprite>();

        // enable input and collision
        mInputEnabled = true;
        mCollisionEnabled = true;

        // create a auto-scrolling background
        mParallaxBackground = new AutoParallaxBackground(0, 0, 0, 10);
        mParallaxBackground.attachParallaxEntity(
                new ParallaxEntity(
                        -5.0f,
                        new Sprite(0,
                                SCREEN_HEIGHT - mResourceManager.mParallaxBackground.getHeight(),
                                mResourceManager.mParallaxBackground,
                                mVertexBufferObjectManager)));
        setBackground(mParallaxBackground);

        // create a spaceship and place it in the middle of the screen
        mSpaceship = new Sprite(
                SCREEN_WIDTH / 2 - mResourceManager.mTextureRegionSpaceship.getWidth() / 2,
                SCREEN_HEIGHT / 2 - mResourceManager.mTextureRegionSpaceship.getHeight() / 2,
                mResourceManager.mTextureRegionSpaceship,
                mVertexBufferObjectManager);
        mSpaceship.setZIndex(10);
        attachChild(mSpaceship);

        // register a touchlistener
        setOnSceneTouchListener(this);

        // spawn enemies each 0.8s
        registerUpdateHandler(new TimerHandler(0.8f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                // check if there shall be a gap instead of an enemy now
                if (mGap > 0) {
                    mGap--;
                    return;
                }

                mTimerCounter++;
                if (mTimerCounter % ENEMIES_PER_WAVE == 0) {
                    mGap = ENEMY_GAP_BETWEEN_WAVES;
                }

                // add an enemy to the scene!
                spawnEnemy();
            }
        }));

        // listen to the GameLoop
        registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                // check for collision on each cycle
                checkCollisions();
            }

            @Override
            public void reset() {

            }
        });

        // create an explosion to show it when the spaceship collides
        mExplosion = new AnimatedSprite(0, 0, mResourceManager.mTextureRegionExplosion, mVertexBufferObjectManager);

        if (!mResourceManager.mMusic.isPlaying()) {
            mResourceManager.mMusic.play();
        }
    }

    @Override
    public void onBackKeyPressed() {
        // when the backkey has pressed
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
        // if the input shall be received
        // we know that the spaceship didnt collide yet
        if (mInputEnabled) {
            handleMove(pScene, pSceneTouchEvent);
        }
        return false;
    }

    /**
     * Checks the touchevent for its action-type and updates
     * the position of the spaceship.
     *
     * @param pScene           the Scene
     * @param pSceneTouchEvent the touchevent
     */
    private void handleMove(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {
            // add px to the x-position
            setPositionWithinBounds(mSpaceship, pSceneTouchEvent.getX() + TOUCH_POSITION_DELTA, pSceneTouchEvent.getY() - mSpaceship.getHeight() / 2);
        }
    }

    /**
     * Updates the position of the spaceship. Checks if position
     * would be out of the screenboundaries.
     *
     * @param object    the sprite to update the position for
     * @param positionX the new x position
     * @param positionY the new y position
     */
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

    /**
     * Spawns an enemy at a random position.
     */
    private void spawnEnemy() {
        // create the enemy outside the screen
        Sprite enemy = new Sprite(
                0,
                0,
                mResourceManager.mTextureRegionEnemy,
                mVertexBufferObjectManager);
        enemy.setZIndex(9);

        // add an exhaust animatedsprite to the enemy
        AnimatedSprite exhaustSprite = new AnimatedSprite(
                enemy.getWidth() - 5,
                enemy.getHeight() / 2 - mResourceManager.mTextureRegionExhaust.getHeight() / 2,
                mResourceManager.mTextureRegionExhaust,
                mVertexBufferObjectManager);
        exhaustSprite.setZIndex(10);
        exhaustSprite.animate(150, true);

        // attach the exhaust to the enemy
        enemy.attachChild(exhaustSprite);

        // save the enemy within the enemies
        mEnemies.add(enemy);

        // calculate the positions
        float x = SCREEN_WIDTH;
        float y = (float) (Math.random() * (SCREEN_HEIGHT - enemy.getHeight()));

        // update the position of the enemy
        enemy.setPosition(x, y);

        // create a moveXModifier
        MoveXModifier moveXModifier = new MoveXModifier(5, enemy.getX(), -enemy.getWidth() * 2);

        // listen to the start and end of the modifier
        moveXModifier.addModifierListener(new IEntityModifier.IEntityModifierListener() {
            @Override
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
                // empty
            }

            @Override
            public void onModifierFinished(IModifier<IEntity> pModifier, final IEntity pItem) {
                // the enemy is out of the screen!


                ResourceManager.getInstance().getActivity().getEngine().runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        // remove the enemy from the list
                        mEnemies.remove(pItem);
                        // remove the enemy from the scene
                        detachChild(pItem);
                    }
                });
            }
        });

        // add the modifier to the enemy
        enemy.registerEntityModifier(moveXModifier);

        // attach the enemy to the scene
        attachChild(enemy);
    }

    /**
     * Checks for collisions between the spaceship and all enemies.
     */
    private void checkCollisions() {
        // if the collision is disabled
        if (!mCollisionEnabled) {
            return;
        }

        // else..
        // check for the collisions!
        for (Sprite enemy : mEnemies) {
            if (mSpaceship.collidesWith(enemy)) {
                onSpaceShipCollision(enemy);
            }
        }
    }

    /**
     * Call this as soon as a collision has been detected.
     * Will disable all further collisiondetection and lets
     * the spaceship explode.
     *
     * @param pEnemy the enemy the spaceship collided with
     */
    private void onSpaceShipCollision(final Sprite pEnemy) {
        ResourceManager.getInstance().getActivity().getEngine().runOnUpdateThread(new Runnable() {
            @Override
            public void run() {
                //pEnemy.clearEntityModifiers();
                mCollisionEnabled = false;
                explode();
            }
        });
    }

    /**
     * The spaceship explodes.
     */
    private void explode() {
        // the spaceship exploded
        // no further input needed
        mInputEnabled = false;

        float[] centerCoordinates = mSpaceship.getSceneCenterCoordinates();
        mExplosion.setPosition(centerCoordinates[0] - mExplosion.getWidth() / 2, centerCoordinates[1] - mExplosion.getHeight() / 2);

        // replace the spaceship with an explosion
        detachChild(mSpaceship);
        attachChild(mExplosion);

        mResourceManager.mSoundExplosion.play();

        // start the animtedsprite!
        mExplosion.animate(100, false, new AnimatedSprite.IAnimationListener() {
            @Override
            public void onAnimationStarted(AnimatedSprite pAnimatedSprite, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite, int pOldFrameIndex, int pNewFrameIndex) {

            }

            @Override
            public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite, int pRemainingLoopCount, int pInitialLoopCount) {

            }

            @Override
            public void onAnimationFinished(final AnimatedSprite pAnimatedSprite) {
                ResourceManager.getInstance().getActivity().getEngine().runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        detachChild(pAnimatedSprite);
                    }
                });
            }
        });
    }

}
