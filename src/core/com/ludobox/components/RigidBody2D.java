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
    public boolean useGravity     = false;
    public float gravityScale     = 1f; //Multply the wordl gravity 
    public float mass             = 1.0f; //Mass Body
    public float friction         = 0.4f;
    public float bounce           = 0.0f;
    public boolean isGrounded     = false;

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
        if (this.body != null) {
            return;
        }

        Transform transform = geTransform();
        Vector2 worldPosition = transform.getWorldPosition();
        Vector2 worldScale = transform.getWorldScale();
        
        bodyDef.position.set(
            Physics.toMeters(worldPosition.x), 
            Physics.toMeters(worldPosition.y)
        );
        bodyDef.angle = transform.getWorldRotation() * MathUtils.degreesToRadians;
        
        this.world = world;
        this.body  = world.createBody(bodyDef);
        this.body.setUserData(gameObject);
        this.body.setGravityScale(0);

        body.setGravityScale(useGravity ? gravityScale : 0.0f);

        SpriteRenderer spriteRenderer = getComponent(SpriteRenderer.class);
         if (spriteRenderer == null) {
            return;
        }
        
        float scaleX = worldScale.x;
        float scaleY = worldScale.y;

        float totalWidth  = spriteRenderer.width * Math.abs(scaleX);
        float totalHeight = spriteRenderer.height * Math.abs(scaleY);

        float centerOffsetX = (spriteRenderer.width * 0.5f - spriteRenderer.pivotX) * scaleX;

        float centerOffsetY = (spriteRenderer.height * 0.5f - spriteRenderer.pivotY) * scaleY;

        System.out.println("centerOffsetX "+centerOffsetX);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(
            Physics.toMeters(totalWidth * 0.5f),
            Physics.toMeters(totalHeight * 0.5f),
            new Vector2(
                Physics.toMeters(centerOffsetX),
                Physics.toMeters(centerOffsetY)
            ),
            0f
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = this.mass;
        fixtureDef.friction = this.friction;
        fixtureDef.restitution = this.bounce;


        Fixture fixture = this.body.createFixture(fixtureDef);
        polygonShape.dispose();
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
        this.body.applyForce(fx, fy, body.getPosition().x, body.getPosition().y, true);
    }
    

    public void addImpulse(float impulseX, float impulseY) {
        if (this.body == null) {
            return;
        }
        this.body.applyLinearImpulse(
            impulseX,
            impulseY,
            body.getWorldCenter().x,
            body.getWorldCenter().y,
            true
        );
    }

    public void setVelocity(float vx, float vy) {
        if (this.body == null) {
            return;
        }
        this.body.setLinearVelocity(Physics.toMeters(vx), Physics.toMeters(vy));
    }

    public void setVelocityY(float velocityY) {
        if (this.body == null) {
            return;
        }
        Vector2 currentVelocity = body.getLinearVelocity();
        body.setLinearVelocity(
            currentVelocity.x,
            Physics.toMeters(velocityY)
        );
    }

    public void setVelocityX(float velocityX) {
        if (this.body == null) {
            return;
        }
        Vector2 currentVelocity = body.getLinearVelocity();
        body.setLinearVelocity(
            Physics.toMeters(velocityX),
            currentVelocity.y
        );
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
