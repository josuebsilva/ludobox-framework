/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.core.physics;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import core.com.ludobox.components.RigidBody2D;
import core.com.ludobox.components.SpriteRenderer;
import core.com.ludobox.gameobjects.GameObject;

public class PhysicSystem {
    private static final float TIME_STEP = 1f / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private float accumulator = 0f;
    private World world;
    private boolean debugEnabled = false;
    private Box2DDebugRenderer debugRenderer;
    private float gravity = -9.8f;
    private Vector2 gravityScale;
    Matrix4 debugMatrix;
    private final Array<GameObject> objects = new Array<>();

    public void setDebug(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;

        if(debugEnabled == true && this.debugRenderer == null) {
            this.debugRenderer = new Box2DDebugRenderer();
        } else {
            this.debugRenderer = null;
        }
    }

    public void setGravityScale(float gravity) {
        this.gravityScale.y = gravity;
        this.world.setGravity( this.gravityScale);
    }

    public void init(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        this.gravityScale = new Vector2(0, this.gravity);

        Box2D.init();
        world = new World(gravityScale, true);
        if(debugEnabled) {
            debugRenderer = new Box2DDebugRenderer();
            debugMatrix = new Matrix4();
        }
    }

    public void update(float deltaTime, Array<GameObject> objects) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            world.step(
                TIME_STEP,
                VELOCITY_ITERATIONS,
                POSITION_ITERATIONS
            );

            accumulator -= TIME_STEP;
        }

        for (GameObject gameObject: objects) {
            if (!gameObject.active) continue;
            RigidBody2D rigidBody2D = gameObject.getComponent(RigidBody2D.class);
            
            if (rigidBody2D != null && rigidBody2D.enabled) {
                rigidBody2D.init(world);
                rigidBody2D.syncTransform();
            }
        }
    }

    public void render(Camera camera) {
        if(this.debugEnabled) {
            //Debug render 
            debugMatrix.set(camera.combined);
            debugMatrix.scale(Physics.PPM, Physics.PPM, 1);
            debugRenderer.render(world, debugMatrix);
        }
    }

    public void destroy() {
    }
}
