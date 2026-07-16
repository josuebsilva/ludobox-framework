/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package game.triangle;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import core.com.ludobox.components.RigidBody2D;
import core.com.ludobox.components.SpriteRenderer;
import core.com.ludobox.core.Config;
import core.com.ludobox.core.Ludobox;
import core.com.ludobox.core.physics.Physics;
import core.com.ludobox.gameobjects.GameObject;
import core.com.ludobox.scenes.Scene;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameLevel extends Scene{
    private Label label;
    private int score = 0;
    GameObject player;
    GameObject bg;

    @Override
    public void onCreate() {

        System.out.println("My game");
        assetLoader.loadTexture("assets/pack.png");

        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = font;
        label = new Label("Score: 0", labelStyle);
        label.setPosition(0, Config.HEIGHT - 100);
        application.getStage().addActor(label);

        SpriteRenderer bgSprite = new SpriteRenderer(assetLoader.loadTexture("assets/bg_rect.png"), Config.WIDTH, Config.HEIGHT);
        bgSprite.setPivot(Config.WIDTH / 2, Config.HEIGHT / 2);
        bg = instantiate("background",0, 0);
        bg.transform.zIndex = 0;
        bg.addComponent(bgSprite);

        TextureAtlas atlas = assetLoader.loadAtlas("assets/pack.atlas");
        

        TextureAtlas.AtlasRegion regionPlayer = atlas.findRegion("triagle");
        SpriteRenderer renderPlayer = new SpriteRenderer(regionPlayer, 100, 100);
        player = instantiate("character",199, 200);
        player.addComponent(renderPlayer);
        renderPlayer.setPivot(100 / 2, 100 / 2);

        //Physics
        RigidBody2D playerRigidBody2D = new RigidBody2D(RigidBody2D.Type.Dynamic);
        playerRigidBody2D.useGravity = true;
        playerRigidBody2D.mass = 3f;
        player.addComponent(playerRigidBody2D);

        JsonValue map = new JsonReader().parse(Gdx.files.internal("assets/MainScene.dt"));
        JsonValue composite = map.get("composite");
        JsonValue content = composite.get("content");
        JsonValue SimpleImageVO = content.get("SimpleImageVO");

        for (JsonValue object : SimpleImageVO) {
            if(!object.has("itemIdentifier")) {
                TextureAtlas.AtlasRegion region = atlas.findRegion(object.getString("imageName"));
            
                GameObject gameObject = instantiate(object.getString("uniqueId"), object.getFloat("x"), object.getFloat("y") );

                SpriteRenderer spriteRenderer = new SpriteRenderer(region, region.originalWidth, region.originalHeight);
                gameObject.transform.setScale( object.has("scaleX") ? object.getFloat("scaleX"): gameObject.transform.getLocalScale().x, object.has("scaleY") ? object.getFloat("scaleY"): gameObject.transform.getLocalScale().y);
                spriteRenderer.setPivot(object.getFloat("originX"), object.getFloat("originY"));
                
                gameObject.addComponent(spriteRenderer);
                if(object.has("physics")) {
                    RigidBody2D goundRigid = new RigidBody2D(RigidBody2D.Type.Static);
                    gameObject.addComponent(goundRigid);
                }
            }
        }
    }

    @Override
    public void onUpdate(float deltaTime) {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            player.getComponent(RigidBody2D.class).addImpulse(0, 320);
        }
        player.getComponent(RigidBody2D.class).setVelocityX(270);
    }

    @Override
    public void onLateUpdate(float deltaTime) {
        camera.position.x += (player.transform.getX() - camera.position.x) * 5 * deltaTime;
        camera.position.y += (player.transform.getY() - camera.position.y) * 5 * deltaTime;

        bg.transform.setPosition(camera.position.x, camera.position.y);
    }

    Array<Body> bodies = new Array<Body>();

    @Override
    public void onRender(float deltaTime) {
    }
}
