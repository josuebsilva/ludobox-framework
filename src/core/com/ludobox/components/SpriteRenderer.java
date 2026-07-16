/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */
package core.com.ludobox.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import core.com.ludobox.gameobjects.Component;
import core.com.ludobox.gameobjects.Transform;

/**
 * SpriteRenderer
 */
public class SpriteRenderer extends Component {
    private TextureRegion textureRegion;
    public float width;
    public float height;
    public final Color tint  = new Color(Color.WHITE);
    public boolean flipX = false;
    public boolean flipY = false;
    public boolean visible = true;
    public float pivotX = 0.0f;
    public float pivotY = 0.0f;


    public SpriteRenderer(TextureRegion textureRegion, float width, float height) {
        this.textureRegion = textureRegion;
        this.width         = width;
        this.height        = height;
    }

    public SpriteRenderer(Texture texture, float width, float height) {
        this.textureRegion = new TextureRegion(texture);
        this.width         = width;
        this.height        = height;
    }

    public void setTexture(Texture texture) {
        this.textureRegion = new TextureRegion(texture);
    }

    public SpriteRenderer(float with, float height) {
        this.width = with;
        this.height = height;
    }

    public void setPivot(float pivotX, float pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
    }

    public void draw(SpriteBatch spriteBatch) {
        if(!visible || textureRegion == null) return;

        Transform transform = geTransform();
        Vector2 worldPos    = transform.getWorldPosition();
        float worldRot      = transform.getWorldRotation();
        Vector2 worldScale  = transform.getWorldScale();

        float originX = pivotX;
        float originY = pivotY;

        spriteBatch.setColor(tint);
        spriteBatch.draw(
            textureRegion,
            worldPos.x - originX,
            worldPos.y - originY,
            originX, originY,
            width, height,
            worldScale.x * (flipX ? -1 : 1),
            worldScale.y * (flipY ? -1 : 1),
            worldRot
        );
        spriteBatch.setColor(Color.WHITE);
    }


    public void setRegion(TextureRegion region) { this.textureRegion = region; }
    public TextureRegion getTextureRegion()     { return textureRegion;}

    public void setTint(Color color)    { tint.set(color);}
    public void setAlpha (float alpha)  { tint.a = alpha;}
}