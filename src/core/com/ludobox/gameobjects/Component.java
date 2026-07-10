/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Josué Barbosa da Silva.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.gameobjects;

import core.com.ludobox.ILifeCycle;

public class Component implements ILifeCycle{
    public GameObject gameObject;
    public boolean enabled = true;

    public void onCreate() {}

    public void onUpdate(float deltaTime) {}

    public void onDestroy() {}

    public <T extends Component> T getComponent(Class<T> type) {
        return gameObject.getComponent(type);
    }

    public Transform geTransform() {
        return gameObject.transform;
    }
}
