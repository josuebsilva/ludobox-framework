/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Josué Barbosa da Silva.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.gameobjects;

/**
 * Layer — camadas de objetos para renderização e filtragem de colisão.
 *
 * Uso:
 *   player.layer = Layer.PLAYER;
 *   coin.layer   = Layer.PICKUP;
 *
 *   // Na GameScene, verificar colisão apenas entre layers específicos:
 *   if (collisionSystem.checkLayer(a, Layer.PLAYER, b, Layer.ENEMY)) { ... }
 */
public final class Layer {
    private Layer() {}

    public static final int BACKGROUND = 0;
    public static final int DEFAULT    = 1;
    public static final int PLAYER     = 2;
    public static final int ENEMY      = 3;
    public static final int PICKUP     = 4;
    public static final int PROJECTILE = 5;
    public static final int UI         = 10;
}
