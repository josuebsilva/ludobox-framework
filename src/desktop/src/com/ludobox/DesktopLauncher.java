/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package desktop.src.com.ludobox;
import core.com.ludobox.core.Ludobox;
import game.demo.GameScene;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
public class DesktopLauncher {
    public static void main(String[] args) throws Exception {
        System.out.println("Ludobox Engine");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Ludobox Engine");
        config.setWindowedMode(Ludobox.VIRTUAL_WIDTH, Ludobox.VIRTUAL_HEIGHT);
        config.setForegroundFPS(60);
        config.useVsync(true);

        new Lwjgl3Application(new Ludobox(new GameScene()), config);
    }
}
