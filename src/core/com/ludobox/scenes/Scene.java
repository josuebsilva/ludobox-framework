package core.com.ludobox.scenes;

import java.util.Comparator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import core.com.ludobox.components.Collider;
import core.com.ludobox.components.SpriteRenderer;
import core.com.ludobox.core.LudoBox;
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

    // GameObjects
    private final Array<GameObject> objects        = new Array<>();
    private final Array<GameObject> objectsToSpawn = new Array<>();
    private final Array<SpriteRenderer> renderers  = new Array<>();

    private static final Comparator<SpriteRenderer> SORT_BY_Z = 
        (a, b) -> Integer.compare(a.gameObject.transform.zIndex, 
                                    b.gameObject.transform.zIndex);

    private Stage stage;
    protected LudoBox application;
    
    public final void init(
        SpriteBatch batch, 
        ShapeRenderer shapeRenderer, 
        OrthographicCamera camera, 
        AssetLoader assetLoader, 
        SceneManager sceneManager,
        LudoBox application
    ) {
        this.batch         = batch;
        this.shapeRenderer = shapeRenderer;
        this.camera        = camera;
        this.assetLoader   = assetLoader;
        this.sceneManager  = sceneManager;
        this.application   = application;
        
        stage = new Stage(new ScreenViewport());

        onCreate();
    }

    //Game loop
    public final void update(float detalTime) {
        //Spawn objects in the list
        if(objectsToSpawn.size > 0) {
            objects.addAll(objectsToSpawn);
            objectsToSpawn.clear();
        }

        onUpdate(detalTime);

        //Update all objects
        for (int i =0; i < objects.size; i++) {
            objects.get(i).update(detalTime);
        }

        //Collision detect
        collisionDetect();

        //Remove objects destroyeds
        for (int i = objects.size - 1; i >= 0; i--) {
            if(objects.get(i).isPendingDestroy()) {
                objects.get(i).onDestroy();
                objects.removeIndex(i);
            }
        }
    }

    public final void render(float detalTime) {
        renderers.clear();
        for (GameObject gameObject: objects) {
            if (!gameObject.active) continue;
            SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
            if(spriteRenderer != null && spriteRenderer.enabled) renderers.add(spriteRenderer);
        }
        renderers.sort(SORT_BY_Z);

        //Draw sprites
        batch.begin();
        for (SpriteRenderer spriteRenderer: renderers) spriteRenderer.draw(batch);
        batch.end();

        stage.draw();


        onRender(detalTime);
    }

    public void resize(int width, int height) { 
        stage.getViewport().update(width, height, true);
        onResize(width, height);
    }
    public void pause()                       { onPause();}
    public void resume()                      { onResume();}

    public final void dispose() {
        onDestroy();
        for (GameObject gameObject: objects) gameObject.onDestroy();

        stage.dispose();
        objects.clear();
        objectsToSpawn.clear();
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
        GameObject gameObject =  instantiate(name);
        gameObject.transform.setPosition(x, y);
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
    protected void onRender(float deltaTime)       {}
    protected void onResize(int width, int height) {}
    protected void onPause()                       {}
    protected void onResume()                      {}
    public void onDestroy()                        {}

    /**
     * Detect collision
     */
    private void collisionDetect() {
        Array<GameObject> withCollider = new Array<>();
        for (GameObject gameObject: objects) {
            if(gameObject.active && gameObject.hasComponent(Collider.class)) {
                withCollider.add(gameObject);
            }
        }

        for (int i =0; i < withCollider.size; i++) {
            for (int j = i + 1; j < withCollider.size; j++) {
                GameObject a = withCollider.get(i);
                GameObject b = withCollider.get(j);
                Collider ca = a.getComponent(Collider.class);
                Collider cb = b.getComponent(Collider.class);
                if(ca.overlaps(cb)) {
                    ca.fireContact(b);
                    cb.fireContact(a);
                }
            }
        }
    }

}
