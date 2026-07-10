package core.com.ludobox.components;

import com.badlogic.gdx.math.Vector2;

import core.com.ludobox.gameobjects.Component;

public class Rigidbody extends Component {
    public final Vector2 velocity = new Vector2();
    public boolean useGravity    = false;
    public float gravity         = -800f; // pixels/s²
    public float drag            = 0.92f; // multiplicador por frame (0=sem atrito, 1=sem drag)
    public boolean isGrounded    = false;

    @Override
    public void onUpdate(float deltaTime) {
        if(useGravity && !isGrounded) {
            velocity.y += gravity * deltaTime;
        }

        //Apply horizonal drag
        velocity.x *= drag;

        //Move transform
        geTransform().translate(velocity.x * deltaTime, velocity.y * deltaTime);
    }

    public void addForce(float fx, float fy) {
        velocity.add(fx, fy);
    }

    public void setHorizontalVelicity(float vx) {
        velocity.x = vx;
    }
}