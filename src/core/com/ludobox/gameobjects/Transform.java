/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.gameobjects;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Transform extends Component{

    // ───────────────── LOCAL ─────────────────

    private final Vector2 localPosition = new Vector2();
    private float localRotation = 0f;
    private final Vector2 localScale = new Vector2(1f, 1f);

    // ───────────────── WORLD (CACHE) ─────────────────

    private final Vector2 worldPosition = new Vector2();
    private float worldRotation = 0f;
    private final Vector2 worldScale = new Vector2(1f, 1f);

    // ───────────────── HIERARQUIA ─────────────────

    private Transform parent;
    private final Array<Transform> children = new Array<>();

    // ───────────────── CONTROLE ─────────────────

    private boolean dirty = true;

    public int zIndex = 0;

    // ==================================================
    // POSITION
    // ==================================================

    public void setPosition(float x, float y) {
        localPosition.set(x, y);
        markDirty();
    }

    public void translate(float dx, float dy) {
        localPosition.add(dx, dy);
        markDirty();
    }

    public Vector2 getLocalPosition() {
        return localPosition;
    }

    public float getX() {
        return localPosition.x;
    }

    public float getY() {
        return localPosition.y;
    }

    // ==================================================
    // ROTATION
    // ==================================================

    public void setRotation(float degrees) {
        localRotation = degrees;
        markDirty();
    }

    public void rotate(float degrees) {
        localRotation += degrees;
        markDirty();
    }

    public float getLocalRotation() {
        return localRotation;
    }

    // ==================================================
    // SCALE
    // ==================================================

    public void setScale(float x, float y) {
        localScale.set(x, y);
        markDirty();
    }

    public void setScale(float scale) {
        setScale(scale, scale);
    }

    public Vector2 getLocalScale() {
        return localScale;
    }

    // ==================================================
    // WORLD TRANSFORMS
    // ==================================================

    public Vector2 getWorldPosition() {
        updateWorldTransform();
        return worldPosition;
    }

    public float getWorldRotation() {
        updateWorldTransform();
        return worldRotation;
    }

    public Vector2 getWorldScale() {
        updateWorldTransform();
        return worldScale;
    }

    private void updateWorldTransform() {

        if (!dirty)
            return;

        if (parent == null) {

            worldPosition.set(localPosition);
            worldRotation = localRotation;
            worldScale.set(localScale);

        } else {

            // Garante que o pai já está atualizado
            parent.updateWorldTransform();

            Vector2 pPos = parent.worldPosition;
            Vector2 pScale = parent.worldScale;

            float pRot = parent.worldRotation;

            float cos = MathUtils.cosDeg(pRot);
            float sin = MathUtils.sinDeg(pRot);

            // Rotação local em relação ao pai
            float rx = localPosition.x * cos - localPosition.y * sin;
            float ry = localPosition.x * sin + localPosition.y * cos;

            // Posição mundial
            worldPosition.set(
                pPos.x + rx * pScale.x,
                pPos.y + ry * pScale.y
            );

            // Rotação mundial
            worldRotation = pRot + localRotation;

            // Escala mundial
            worldScale.set(
                pScale.x * localScale.x,
                pScale.y * localScale.y
            );
        }

        dirty = false;
    }

    // ==================================================
    // HIERARQUIA
    // ==================================================

    public void setParent(Transform newParent) {

        if (parent == newParent)
            return;

        // Remove do pai antigo
        if (parent != null) {
            parent.children.removeValue(this, true);
        }

        parent = newParent;

        // Adiciona ao novo pai
        if (parent != null) {
            parent.children.add(this);
        }

        markDirty();
    }

    public Transform getParent() {
        return parent;
    }

    public Array<Transform> getChildren() {
        return children;
    }

    public boolean hasParent() {
        return parent != null;
    }

    // ==================================================
    // DIRTY FLAG
    // ==================================================

    private void markDirty() {

        if (dirty)
            return;

        dirty = true;

        for (Transform child : children) {
            child.markDirty();
        }
    }
}
