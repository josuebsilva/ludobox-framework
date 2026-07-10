package core.com.ludobox.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import core.com.ludobox.core.Physics;
import core.com.ludobox.gameobjects.Component;

public class RigidBody2D extends Component {
    public final Vector2 velocity = new Vector2();
    public boolean useGravity    = false;
    public float gravity         = -800f; // meters/s²
    public float drag            = 0.92f; // multiplicador por frame (0=sem atrito, 1=sem drag)
    public boolean isGrounded    = false;

    private BodyDef bodyDef;
    private Body body;
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
        bodyDef.position.set(
            Physics.toMeters(geTransform().getX()), 
            Physics.toMeters(geTransform().getY())
        );
    }

    @Override
    public void onUpdate(float deltaTime) {
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
