/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.core;
import com.badlogic.gdx.graphics.GL20;

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

public class Ludobox implements ApplicationListener{
    
    private SpriteBatch        batch;
    private Viewport           viewport;
    private ShapeRenderer      shapeRenderer;
    private OrthographicCamera camera;
    private AssetLoader        assetLoader;
    private SceneManager       sceneManager;
    private Stage stage;

    private Scene initialScene;
    private Config config;

    public Ludobox(Scene initialScene) {
        this.initialScene = initialScene;
        this.config = new Config();
    }

    public Ludobox(Scene initialScene, Config config) {
        this.initialScene = initialScene;
        this.config = config;
    }

    public Config getConfig() {
        return this.config;
    }

    @Override
    public void create() {
        batch          = new SpriteBatch();

        stage          = new Stage(new StretchViewport(Config.WIDTH, Config.HEIGHT), batch);
        Gdx.input.setInputProcessor(stage);
        
        shapeRenderer  = new ShapeRenderer();
        camera         = new OrthographicCamera();
        viewport       = new FitViewport(Config.WIDTH, Config.HEIGHT, camera);
        viewport.apply(true);

        assetLoader  = new AssetLoader();
        sceneManager = new SceneManager(batch, shapeRenderer, camera, assetLoader, this);
        sceneManager.push(initialScene);
    }

    @Override
    public void render() {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1f / 60f);

        Gdx.gl.glClearColor(0.00f, 0.00f, 0.00f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //Update game objects, physics and camera
        sceneManager.update(deltaTime);
        stage.act(deltaTime);

        //Recalcule the camera matriz after camera moved
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        //Rende with camera updated
        sceneManager.render(deltaTime);

        //UI over ther worl
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
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
