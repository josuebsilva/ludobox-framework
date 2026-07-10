package game.triangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameScene extends Scene{
    private Label label;
    private int score = 0;

    @Override
    public void onCreate() {
        System.out.println("My game");
        assetLoader.loadTexture("assets/pack.png");

        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = font;
        label = new Label("Score: 0", labelStyle);
        label.setPosition(0, LudoBox.VIRTUAL_HEIGHT - 100);
        application.getStage().addActor(label);

        TextureAtlas atlas = assetLoader.loadAtlas("assets/pack.atlas");

        JsonValue map = new JsonReader().parse(Gdx.files.internal("assets/MainScene.dt"));
        JsonValue composite = map.get("composite");
        JsonValue content = composite.get("content");
        JsonValue SimpleImageVO = content.get("SimpleImageVO");
        for (JsonValue object : SimpleImageVO) {
            System.out.println("Available Mode: " + object.getString("uniqueId"));
            TextureAtlas.AtlasRegion region = atlas.findRegion(object.getString("imageName"));
            
            GameObject gameObject = instantiate(object.getString("uniqueId"), object.getFloat("x"), object.getFloat("y") );
            
            
            SpriteRenderer spriteRenderer = new SpriteRenderer(region, region.originalWidth, region.originalHeight);
            spriteRenderer.setPivot(object.getFloat("originX"), object.getFloat("originY"));
            gameObject.transform.setScale( object.has("scaleX") ? object.getFloat("scaleX"): gameObject.transform.getLocalScale().x, object.has("scaleY") ? object.getFloat("scaleY"): gameObject.transform.getLocalScale().y);
            gameObject.addComponent(spriteRenderer);
        }
    }

    @Override
    public void onUpdate(float deltaTime) {
    }
}
