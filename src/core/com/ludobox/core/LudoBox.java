package core.com.ludobox.core;
import org.lwjgl.opengl.GL20;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import core.com.ludobox.assets.AssetLoader;
import core.com.ludobox.scenes.Scene;
import core.com.ludobox.scenes.SceneManager;

public class LudoBox implements ApplicationListener{
    public static final int VIRTUAL_WIDTH  = 1280;
    public static final int VIRTUAL_HEIGHT = 720;

    private SpriteBatch        batch;
    private Viewport           viewport;
    private ShapeRenderer      shapeRenderer;
    private OrthographicCamera camera;
    private AssetLoader        assetLoader;
    private SceneManager       sceneManager;
    private Stage stage;

    private Scene initialScene;

    public LudoBox(Scene initialScene) {
        this.initialScene = initialScene;
    }

    @Override
    public void create() {
        stage          = new Stage(new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        
        batch          = new SpriteBatch();
        shapeRenderer  = new ShapeRenderer();
        camera         = new OrthographicCamera();
        viewport       = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();

        assetLoader  = new AssetLoader();
        sceneManager = new SceneManager(batch, shapeRenderer, camera, assetLoader, this);
        sceneManager.push(initialScene);
    }

    @Override
    public void render() {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1f / 30f);

        Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        sceneManager.update(deltaTime);
        sceneManager.render(deltaTime);

        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
        sceneManager.resize(width, height);
    }

    @Override
    public void resume() {
        sceneManager.resume();
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        assetLoader.dispose();
        batch.dispose();
        shapeRenderer.dispose();

        stage.clear();
        stage.dispose();
    }

    @Override
    public void pause() {
        sceneManager.pause();
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public Stage getStage() {
        return this.stage;
    } 
}
