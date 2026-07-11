/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.gameobjects;
public class Entity {
    private boolean pendingDestroy;
    public void destroy() {
        this.pendingDestroy = true;
    }

    public boolean isPendingDestroy() {
        return pendingDestroy;
    }
}
