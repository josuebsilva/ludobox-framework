/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */
package game.demo;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Game implements ApplicationListener{
    Texture backgrounTexture;
    Texture bucketTexture;
    Texture dropTexture;
    Sound dropSound;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;
    Music music;

    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite bucketSprite;

    Vector2 touchPos;

    Array<Sprite> dropSprites;
    float dropTimer;

    @Override
    public void create() {
        backgrounTexture = new Texture("assets/background.png");
        bucketTexture = new Texture("assets/bucket.png");
        dropTexture = new Texture("assets/drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("assets/drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("assets/music.mp3"));
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        touchPos = new Vector2();
        dropSprites = new Array<>();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    @Override
    public void resume() {

    }

    private void createDroplet() {
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0, worldWidth - dropWidth));
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {

    }

    private void input() {
        float speed = 4f;
        float deltaTime = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * deltaTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * deltaTime);
        }

        if(Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            bucketSprite.setCenterX(touchPos.x);
        }
    }

    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        float deltaTime = Gdx.graphics.getDeltaTime();
        for (int i =0; i < dropSprites.size - 1; i++) {
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * deltaTime);

            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            if(dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (bucketRectangle.overlaps(dropRectangle)) {
                dropSprites.removeIndex(i);
                dropSound.play();
            }

        }
        
        dropTimer += deltaTime;
        if(dropTimer > 1f) {
            dropTimer = 0;
            createDroplet();
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgrounTexture, 0, 0, worldWidth, worldHeight);
        bucketSprite.draw(spriteBatch);

        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(spriteBatch);
        }

        spriteBatch.end();
    }
}
