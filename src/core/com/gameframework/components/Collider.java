/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Josué Barbosa da Silva.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.gameframework.components;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import core.com.gameframework.gameobjects.Component;
import core.com.gameframework.gameobjects.GameObject;

public class Collider extends Component {
    public final Rectangle bounds = new Rectangle();
    public boolean trigger = false;
    public float offsetX = 0f;
    public float offsetY = 0f;
    public float width;
    public float height;

    private CollisionListener listener;

    public Collider(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Collider(float width, float height, float offsetX, float offsetY) {
        this(width, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void onUpdate(float deltaTime) {
        Vector2 worldPos = geTransform().getWorldPosition();
        bounds.set(
            worldPos.x - width * 0.5f + offsetX,
            worldPos.y - height * 0.5f + offsetY,
            width, height
        );
    }

    public boolean overlaps(Collider other) {
        return bounds.overlaps(other.bounds);
    }

    public Collider setTrigger(boolean trigger) {
        this.trigger = trigger;
        return this;
    }

    public Collider setListener(CollisionListener listener) {
        this.listener = listener;
        return this;
    }

    public void fireContact(GameObject other) {
        if(listener != null) {
            if(this.trigger) {
                listener.onTrigger(gameObject, other);
            } else {
                listener.onCollider(gameObject, other);
            }
        }
    }

    //Callback function
    @FunctionalInterface
    public interface CollisionListener {
        default void onCollider(GameObject self, GameObject other){}
        void onTrigger(GameObject self, GameObject other);
    }
}
