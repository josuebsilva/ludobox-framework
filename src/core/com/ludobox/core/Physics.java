/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.core;

public final class Physics {
    public static final float PPM = 32f;

    public static float toMeters(float pixels) {
        return pixels / PPM;
    }

    public static float toPixels( float meters) {
        return meters * PPM;
    }
}
