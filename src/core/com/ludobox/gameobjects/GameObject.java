/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Josué Barbosa da Silva.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.gameobjects;
import com.badlogic.gdx.utils.Array;

import core.com.ludobox.ILifeCycle;

public class GameObject extends Entity implements ILifeCycle {
    //Identity of object
    public String name;
    public String tag     = "";
    public int layer      = 0;
    public boolean active = true;

    //The game object always has a Transform component
    public final Transform transform = new Transform();

    //Components
    private final Array<Component> components      = new Array<>();
    private final Array<Component> componentsToAdd = new Array<>();

    public GameObject(String name) {
        this.name = name;
    }

    public void onCreate() {
    } 

    public void onDestroy() {
    }


    public <T extends Component> T addComponent(T component) {
        component.gameObject = this;
        this.componentsToAdd.add(component);
        component.onCreate();
        return component;
    }

    public <T extends Component> T getComponent(Class<T> type) {
       for (Component c: components) {
            if(type.isInstance(c)) return type.cast(c);
       }
       for (Component c: componentsToAdd) {
            if(type.isInstance(c)) return type.cast(c);
       }
       return null;
    }
    
    public <T extends Component> void removeComponent(Class<T> type) {
        for (int i =0; i < components.size; i++) {
            if(type.isInstance(components.get(i))) {
                components.get(i).onDestroy();
                components.removeIndex(i);
            }
        }
    }

    public <T extends Component> boolean hasComponent(Class<T> type) {
        return getComponent(type) != null;
    }

    /**
     * Called by Scene every frame and commit of pending components
     * Call onUpdate of components
     * @param deltaTime
     */
    public void update(float deltaTime) {
        if(componentsToAdd.size > 0) {
            components.addAll(componentsToAdd);
            componentsToAdd.clear();
        }

        if(!active) return;

        for(int i =0; i < components.size; i++) {
            Component component = components.get(i);
            if(component.enabled) component.onUpdate(deltaTime);
        }

        onUpdate(deltaTime);
    }

    /**
     * Make chields
     */
    public GameObject createChield (String name) {
        GameObject chield = new GameObject(name);
        chield.transform.setParent(this.transform);
        return chield;
    }

    @Override
    public String toString() {
        return "GameObject["+name+"]";
    }

    public void onUpdate(float deltaTime) {}
}
