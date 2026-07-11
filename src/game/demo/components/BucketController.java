/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */
package game.demo.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import core.com.ludobox.components.SpriteRenderer;
import core.com.ludobox.core.Config;
import core.com.ludobox.gameobjects.Component;

public class BucketController extends Component {
    public boolean collided = false;
    private float timeCollided = 0;
    @Override
    public void onCreate() {
    }

    @Override
    public void onUpdate(float deltaTime) {
        if(Gdx.input.isTouched()) {
            float xPos = MathUtils.clamp(Gdx.input.getX(), 0, Config.WIDTH - gameObject.getComponent(SpriteRenderer.class).width);
            gameObject.transform.setPosition(xPos, gameObject.transform.getY());
        }

        if(collided) {
            timeCollided += deltaTime;
            if(timeCollided >= 0.1f) {
                timeCollided = 0;
                collided = false;
                getComponent(SpriteRenderer.class).setAlpha(1);
            }
        }
    }
}
