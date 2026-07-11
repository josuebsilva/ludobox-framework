/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import core.com.ludobox.core.physics.Physics;
import core.com.ludobox.gameobjects.Component;
import core.com.ludobox.gameobjects.Transform;

public class RigidBody2D extends Component {
    public final Vector2 velocity = new Vector2();
    public boolean useGravity    = false;
    public float gravity         = -800f; // meters/s²
    public float drag            = 0.92f; // multiplicador por frame (0=sem atrito, 1=sem drag)
    public boolean isGrounded    = false;

    private BodyDef bodyDef;
    private Body body = null;
    private World world = null;
    public RigidBody2D(Type type) {
        bodyDef = new BodyDef();
        switch (type) {
            case Dynamic:
                bodyDef.type = BodyType.DynamicBody;
                break;
            case Static:
                bodyDef.type = BodyType.StaticBody;
                break;
            case Kinematic:
                bodyDef.type = BodyType.KinematicBody;
                break;
            default:
                break;
        }
    }

    public void init(World world) {
        if(this.body == null) {
            bodyDef.position.set(
                Physics.toMeters(geTransform().getX()), 
                Physics.toMeters(geTransform().getY())
            );
            
            this.world = world;
            this.body  = world.createBody(bodyDef);
            this.body.setUserData(gameObject);

            SpriteRenderer spriteRenderer = getComponent(SpriteRenderer.class);
            float totalWidth  = spriteRenderer.width * gameObject.transform.getLocalScale().x;
            float totalHeight = spriteRenderer.width * gameObject.transform.getLocalScale().y;

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(
                Physics.toMeters(totalWidth / 2), 
                Physics.toMeters(totalHeight / 2)
            );

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 0.5f;
            fixtureDef.friction = 0.4f;
            fixtureDef.restitution = 0.6f;

            Fixture fixture = this.body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }

    @Override
    public void onUpdate(float deltaTime) {
    }

    @Override
    public void onDestroy() {
        System.out.print("Remove Rigidbody");

        world.destroyBody(body);
    }

    public void syncTransform() {
        Transform transform = geTransform();
        Vector2 pos         = body.getPosition();
        transform.setPosition(Physics.toPixels(pos.x), Physics.toPixels(pos.y));
        transform.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    public void addForce(float fx, float fy) {
        velocity.add(fx, fy);
    }

    public void setHorizontalVelicity(float vx) {
        velocity.x = vx;
    }

    public static enum Type {
      Static(0),
      Kinematic(1),
      Dynamic(2);

      private int value;

      private Type(int value) {
        this.value = value;
      }

      public int getValue() {
        return this.value;
      }
   }
}
