/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package desktop.src.com.ludobox;
import core.com.ludobox.core.Config;
import core.com.ludobox.core.Ludobox;
import game.demo.GameScene;
import game.triangle.GameLevel;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
public class DesktopLauncher {
    public static void main(String[] args) throws Exception {
        System.out.println("Ludobox Engine");

        Lwjgl3ApplicationConfiguration configApplication = new Lwjgl3ApplicationConfiguration();
        configApplication.setTitle("Ludobox Engine");
        configApplication.setWindowedMode(Config.WIDTH, Config.HEIGHT);
        configApplication.setForegroundFPS(60);
        configApplication.useVsync(true);

        Config config = new Config();
        //config.physicDebug = true;
        new Lwjgl3Application(new Ludobox(new GameLevel(), config), configApplication);
    }
}
