/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.scenes;

import java.util.Comparator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import core.com.ludobox.components.SpriteRenderer;
import core.com.ludobox.core.Ludobox;
import core.com.ludobox.core.physics.PhysicSystem;
import core.com.ludobox.gameobjects.GameObject;
import core.com.ludobox.ILifeCycle;
import core.com.ludobox.assets.AssetLoader;

/**
 * @author Josué Barbosa
 * @apiNote Manager lifecycle of game objects
 */

public abstract class Scene implements ILifeCycle {
    //Dependences, injected by SceneManager
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected OrthographicCamera camera;
    protected AssetLoader assetLoader;
    protected SceneManager sceneManager;

    protected PhysicSystem physicSystem;
    public boolean hasPhysics = true;


    // GameObjects
    private final Array<GameObject> objects        = new Array<>();
    private final Array<GameObject> objectsToSpawn = new Array<>();
    private final Array<SpriteRenderer> renderers  = new Array<>();

    private static final Comparator<SpriteRenderer> SORT_BY_Z = 
        (a, b) -> Integer.compare(a.gameObject.transform.zIndex, 
                                    b.gameObject.transform.zIndex);
    protected Ludobox application;
    
    public final void init(
        SpriteBatch batch, 
        ShapeRenderer shapeRenderer, 
        OrthographicCamera camera, 
        AssetLoader assetLoader, 
        SceneManager sceneManager,
        Ludobox application
    ) {
        this.batch         = batch;
        this.shapeRenderer = shapeRenderer;
        this.camera        = camera;
        this.assetLoader   = assetLoader;
        this.sceneManager  = sceneManager;
        this.application   = application;

        if(hasPhysics) {
            physicSystem = new PhysicSystem();
            physicSystem.init(application.getConfig().physicDebug);
        }

        onCreate();
    }

    private void flushSpawnQueue() {
        if (objectsToSpawn.size == 0) return;

        objects.addAll(objectsToSpawn);
        objectsToSpawn.clear();
    }

    //Game loop
    public final void update(float deltaTime) {
        //Spawn objects in the list
        flushSpawnQueue();

        onUpdate(deltaTime);

        //Update all objects
        for (int i = 0; i < objects.size; i++) {
            GameObject gameObject = objects.get(i);

            if (gameObject.active) {
                gameObject.update(deltaTime);
            }
        }

        if (physicSystem != null) {
            physicSystem.update(deltaTime, objects);
        } else {
            collisionDetect();
        }

        onLateUpdate(deltaTime);

        //Remove objects destroyeds
        removeDestroyedObjects();
    }

    private void removeDestroyedObjects() {
        for (int i = objects.size - 1; i >= 0; i--) {
            if(objects.get(i).isPendingDestroy()) {
                objects.get(i).onDestroy();
                objects.removeIndex(i);
            }
        }
    }

    public final void render(float detalTime) {

        renderers.clear();
        for (GameObject gameObject : objects) {
            if (!gameObject.active) continue;

            SpriteRenderer renderer =
                gameObject.getComponent(SpriteRenderer.class);

            if (renderer != null && renderer.enabled) {
                renderers.add(renderer);
            }
        }

        renderers.sort(SORT_BY_Z);

        //Draw sprites
        batch.begin();
        for (SpriteRenderer renderer : renderers) {
            renderer.draw(batch);
        }
        batch.end();

        onRender(detalTime);

        //Render physic debug
        if (physicSystem != null) {
            physicSystem.render(camera);
        }
    }

    public void resize(int width, int height) { 
        onResize(width, height);
    }
    public void pause()                       { onPause();}
    public void resume()                      { onResume();}

    public final void dispose() {
        onDestroy();
        for (GameObject gameObject: objects) gameObject.onDestroy();

        objects.clear();
        objectsToSpawn.clear();
        renderers.clear();

        if (physicSystem != null) {
            physicSystem.dispose();
        }
    }

    /**
     * This method make a new object and insert on objectsToSpawn to the next frame
     */
    public GameObject instantiate(String name) {
        GameObject gameObject = new GameObject(name);
        objectsToSpawn.add(gameObject);
        gameObject.onCreate();
        return gameObject;
    }

    /**
     * Make a new object with the position
     */

    public GameObject instantiate(String name, float x, float y) {
        GameObject gameObject = new GameObject(name);
        objectsToSpawn.add(gameObject);
        gameObject.transform.setPosition(x, y);
        gameObject.onCreate();
        return gameObject;
    }

    /**
     * Destroy all objects by tag
     */

    public void destroyByTag(String tag) {
        for (GameObject gameObject: objects) {
            if(tag.equals(gameObject.tag)) gameObject.destroy();
        }
    }

    /**
     * find first object by tag
     */
    public GameObject findByTag(String tag) {
        for (GameObject gameObject: objects) {
            if(tag.equals(gameObject.tag)) return gameObject;
        }
        return null;
    }

    /**
     * Find all objects with the tag 
     */
    public Array<GameObject> findAllByTag(String tag) {
        Array<GameObject> result = new Array<>();
        for (GameObject gameObject: objects) {
            if(tag.equals(gameObject.tag)) {
                result.add(gameObject);
            }
        }

        return result;
    }

    //Subclass hooks
    public abstract void onCreate();
    public void onUpdate(float deltaTime)          {}
    protected void onLateUpdate(float deltaTime)   {}
    protected void onRender(float deltaTime)       {}
    protected void onResize(int width, int height) {}
    protected void onPause()                       {}
    protected void onResume()                      {}
    public void onDestroy()                        {}

    /**
     * Detect collision
     */
    private void collisionDetect() {
    }

}
