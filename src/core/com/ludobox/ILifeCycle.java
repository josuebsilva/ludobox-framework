/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */
package core.com.ludobox;

public interface ILifeCycle {
    public void onCreate();
    public void onUpdate(float deltaTime);
    public void onDestroy();
}
