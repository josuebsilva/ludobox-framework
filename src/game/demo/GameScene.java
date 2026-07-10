package game.demo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import core.com.ludobox.components.Collider;
import core.com.ludobox.components.MusicSource;
import core.com.ludobox.components.Collider.CollisionListener;
import core.com.ludobox.components.Rigidbody;
import core.com.ludobox.components.SoundSource;
import core.com.ludobox.components.SpriteRenderer;
import core.com.ludobox.core.LudoBox;
import core.com.ludobox.gameobjects.GameObject;
import core.com.ludobox.scenes.Scene;
import game.demo.components.BucketController;

public class GameScene extends Scene{
    float timeInstance = 0;
    private Label label;
    private int score = 0;

    @Override
    public void onCreate() {
        System.out.println("My game");
        assetLoader.loadTexture("assets/background.png");
        assetLoader.loadMusic("assets/music.mp3");
        assetLoader.loadSound("assets/drop.mp3");

        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = font;
        label = new Label("Score: 0", labelStyle);
        label.setPosition(100, LudoBox.VIRTUAL_HEIGHT - 100);
        application.getStage().addActor(label);


        GameObject background = instantiate("background", 0, 0);
        SpriteRenderer sprite = new SpriteRenderer(assetLoader.loadTexture("assets/background.png"), LudoBox.VIRTUAL_WIDTH, LudoBox.VIRTUAL_HEIGHT);
        sprite.setAlpha(0.2f);
        sprite.setPivot(0, 0);
        background.addComponent(sprite);
        
        GameObject bucket = instantiate("bucket");
        SpriteRenderer spriteBucket = new SpriteRenderer(assetLoader.getFile(Texture.class, "assets/bucket.png"), 100, 100);
        bucket.addComponent(spriteBucket);

        SoundSource soundSource = new SoundSource(assetLoader.loadSound("assets/drop.mp3"));
        BucketController bucketController = new BucketController();
        bucket.addComponent(bucketController);
        bucket.addComponent(soundSource);
        Collider collider = bucket.addComponent(new Collider(spriteBucket.width, spriteBucket.height));
        collider.setListener(new CollisionListener() {
            @Override
            public void onCollider(GameObject self, GameObject other) {
                System.out.println("Collider detect");
                self.getComponent(SoundSource.class).sound.play();
                self.getComponent(BucketController.class).collided = true;
                self.getComponent(SpriteRenderer.class).setAlpha(0.5f);

                score++;
                label.setText("Score: "+score);
                other.destroy();
            }

            @Override
            public void onTrigger(GameObject self, GameObject other) {
                System.out.println("Trigger detect");
            }
        });

        GameObject gameController = instantiate("gameController", 0, 0);
        MusicSource musicSource = new MusicSource(assetLoader.loadMusic("assets/music.mp3"));
        gameController.addComponent(musicSource);
        gameController.getComponent(MusicSource.class).music.play();
    }

    @Override
    public void onUpdate(float deltaTime) {
        timeInstance += deltaTime;
        if(timeInstance >= 1f) {
            createDroplet();
            timeInstance = 0;
        }
    }

    public void createDroplet() {
        GameObject drop = instantiate("drop");
        SpriteRenderer spriteRenderer = new SpriteRenderer(100, 100);
        spriteRenderer.setTexture(assetLoader.getFile(Texture.class, "assets/drop.png"));

        float randPosX = MathUtils.random(0, LudoBox.VIRTUAL_WIDTH - spriteRenderer.width);
        Collider collider = new Collider(spriteRenderer.width, spriteRenderer.height);
        drop.addComponent(spriteRenderer);
        drop.addComponent(collider);
        Rigidbody rigidbody = new Rigidbody();
        rigidbody.useGravity = true;
        drop.addComponent(rigidbody);
        
        drop.transform.setPosition(randPosX, LudoBox.VIRTUAL_HEIGHT - spriteRenderer.height);
        drop.transform.zIndex = 1;
    }
}
