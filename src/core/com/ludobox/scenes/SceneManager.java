package core.com.ludobox.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import core.com.ludobox.assets.AssetLoader;
import core.com.ludobox.core.LudoBox;

public class SceneManager {
    private Array<Scene> stackScenes = new Array<>();

    private final SpriteBatch        batch;
    private final ShapeRenderer      shapeRenderer;
    private final OrthographicCamera camera;
    private final AssetLoader        assetLoader;

    private enum Cmd { PUSH, POP, REPLACE, CLEAR}
    private Cmd pendingCmd     = null;
    private Scene pendingScene = null;

    private LudoBox application;

    public SceneManager(SpriteBatch batch, ShapeRenderer shapeRenderer, OrthographicCamera camera, AssetLoader assetLoader, LudoBox application) {
        this.batch         = batch;
        this.shapeRenderer = shapeRenderer;
        this.camera        = camera;
        this.assetLoader   = assetLoader;
        this.application   = application;
    }


    public void push(Scene scene) { pendingCmd = Cmd.PUSH; pendingScene = scene;}
    public void pop() { pendingCmd = Cmd.POP; pendingScene = null;}
    public void replace(Scene scene) { pendingCmd = Cmd.REPLACE; pendingScene = scene;}
    public void clear(Scene scene) { pendingCmd = Cmd.CLEAR; pendingScene = scene;}

    public void update(float deltaTime) {
        applyPending();
        if(!stackScenes.isEmpty()) stackScenes.peek().update(deltaTime);
    }

    public void render(float deltaTime) {
        if(!stackScenes.isEmpty()) stackScenes.peek().render(deltaTime);
    }

    public void resize(int width, int height) { if(!stackScenes.isEmpty()) stackScenes.peek().resize(width, height);}
    public void pause() { if(!stackScenes.isEmpty()) stackScenes.peek().pause();}
    public void resume() { if(!stackScenes.isEmpty()) stackScenes.peek().resume();}

    public void dispose() {
        for(Scene scene: stackScenes) scene.dispose();
        stackScenes.clear();
    }

    private void applyPending() {
        if(pendingCmd == null) return;
        switch (pendingCmd) {
            case PUSH:
                initAndPush(pendingScene);
                break;
            case POP:
                if(!stackScenes.isEmpty()) stackScenes.pop().dispose();
                break;
            case REPLACE:
                if(!stackScenes.isEmpty()) stackScenes.pop().dispose();
                initAndPush(pendingScene);
                break;
            case CLEAR:
                for (Scene scene: stackScenes) scene.dispose();
                stackScenes.clear();
                if(pendingScene != null ) initAndPush(pendingScene);
                break;
        }
        pendingCmd = null;
        pendingScene = null;
    }

    private void initAndPush(Scene scene) {
        application.getStage().clear(); //Clear UI Elements

        scene.init(batch, shapeRenderer, camera, assetLoader, this, application);
        stackScenes.add(scene);
    }
}
