package game.triangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import core.com.gameframework.components.SpriteRenderer;
import core.com.gameframework.core.GameFramework;
import core.com.gameframework.core.Physics;
import core.com.gameframework.gameobjects.GameObject;
import core.com.gameframework.scenes.Scene;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameLevel extends Scene{
    private Label label;
    private int score = 0;
    World world;
    Box2DDebugRenderer debugRenderer;

    @Override
    public void onCreate() {
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -9.8f), true);

        System.out.println("My game");
        assetLoader.loadTexture("assets/pack.png");

        BitmapFont font = new BitmapFont();
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = font;
        label = new Label("Score: 0", labelStyle);
        label.setPosition(0, GameFramework.VIRTUAL_HEIGHT - 100);
        application.getStage().addActor(label);

        TextureAtlas atlas = assetLoader.loadAtlas("assets/pack.atlas");
        TextureAtlas.AtlasRegion regionPlayer = atlas.findRegion("icon");

        SpriteRenderer renderPlayer = new SpriteRenderer(regionPlayer, 100, 100);
        GameObject character = instantiate("character", 400, 500);
        character.addComponent(renderPlayer);
        renderPlayer.setPivot(100 / 2, 100 / 2);

        //Physics
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(
            Physics.toMeters(400), 
            Physics.toMeters(500)
        );

        Body body = world.createBody(bodyDef);
        body.setUserData(character);
        
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(
            Physics.toMeters(50), 
            Physics.toMeters(50)
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = new PolygonShape();
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        Fixture fixture = body.createFixture(fixtureDef);
        polygonShape.dispose();


        TextureAtlas.AtlasRegion regionGround = atlas.findRegion("rect1");
        SpriteRenderer renderGround = new SpriteRenderer(regionGround, 100, 100);
        GameObject ground = instantiate("ground", 0, 50);
        ground.addComponent(renderGround);
        renderGround.setPivot(100 / 2, 100 / 2);
        ground.transform.setScale(8, 1);

        //Physics
        BodyDef bodyDefGround = new BodyDef();
        bodyDefGround.type = BodyType.StaticBody;
        bodyDefGround.position.set(
            Physics.toMeters(0),
            Physics.toMeters(50)
        );

        Body bodyGround = world.createBody(bodyDefGround);
        bodyGround.setUserData(ground);
        
        PolygonShape polygonShapeGround = new PolygonShape();
        polygonShapeGround.setAsBox(
            Physics.toMeters(400),
            Physics.toMeters(50)
        );

        FixtureDef fixtureDefGround = new FixtureDef();
        fixtureDefGround.shape = polygonShapeGround;

        Fixture fixtureGround = bodyGround.createFixture(fixtureDefGround);
        polygonShapeGround.dispose();
        

        /*JsonValue map = new JsonReader().parse(Gdx.files.internal("assets/MainScene.dt"));
        JsonValue composite = map.get("composite");
        JsonValue content = composite.get("content");
        JsonValue SimpleImageVO = content.get("SimpleImageVO");*/


        /*for (JsonValue object : SimpleImageVO) {
            System.out.println("Available Mode: " + object.getString("uniqueId"));
            TextureAtlas.AtlasRegion region = atlas.findRegion(object.getString("imageName"));
            
            GameObject gameObject = instantiate(object.getString("uniqueId"), object.getFloat("x"), object.getFloat("y") );
            
            
            SpriteRenderer spriteRenderer = new SpriteRenderer(region, region.originalWidth, region.originalHeight);
            spriteRenderer.setPivot(object.getFloat("originX"), object.getFloat("originY"));
            gameObject.transform.setScale( object.has("scaleX") ? object.getFloat("scaleX"): gameObject.transform.getLocalScale().x, object.has("scaleY") ? object.getFloat("scaleY"): gameObject.transform.getLocalScale().y);
            gameObject.addComponent(spriteRenderer);
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.StaticBody;

            Body body = world.createBody(bodyDef);
            body.setUserData(gameObject);
            if(!object.getString("imageName").equals("icon")) {
                body.setGravityScale(0);
                body.setFixedRotation(true);
            }
            
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(6f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.density = 0.5f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.6f;

            Fixture fixture = body.createFixture(fixtureDef);
            circleShape.dispose();
        }*/
    }

    @Override
    public void onUpdate(float deltaTime) {
        world.step(deltaTime, 6, 2);
    }

    Array<Body> bodies = new Array<Body>();

    @Override
    public void onRender(float deltaTime) {
        Matrix4 debugMatrix = new Matrix4(camera.combined);
        debugMatrix.scale(Physics.PPM, Physics.PPM, 1);
        debugRenderer.render(world, debugMatrix);

        world.getBodies(bodies);
        //System.out.println("bodies: "+bodies.size);
        for (Body b : bodies) {
            // Get the body's user data - in this example, our user
            // data is an instance of the Entity class
            GameObject e = (GameObject) b.getUserData();
            e.transform.setPosition(
                Physics.toPixels(b.getPosition().x), 
                Physics.toPixels(b.getPosition().y)
            );
            e.transform.setRotation(
                b.getAngle() * MathUtils.radiansToDegrees
            );
        }
    }
}
