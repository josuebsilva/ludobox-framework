/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.core.physics;
import com.badlogic.gdx.math.Vector2;
public final class Physics {
    public static final float PPM = 32f;

    public static float toMeters(float pixels) {
        return pixels / PPM;
    }

    public static float toPixels( float meters) {
        return meters * PPM;
    }

    public static Vector2 toMeters(Vector2 pixels) {
        return new Vector2(
            pixels.x / PPM,
            pixels.y / PPM
        );
    }

    public static Vector2 toPixels(Vector2 meters) {
        return new Vector2(
            meters.x * PPM,
            meters.y * PPM
        );
    }
}
