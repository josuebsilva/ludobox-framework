package desktop.src.com.ludobox;
import core.com.ludobox.core.LudoBox;
import game.triangle.GameScene;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
public class DesktopLauncher {
    public static void main(String[] args) throws Exception {
        System.out.println("Ludobox Engine");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Ludobox Engine");
        config.setWindowedMode(LudoBox.VIRTUAL_WIDTH, LudoBox.VIRTUAL_HEIGHT);
        config.setForegroundFPS(60);
        config.useVsync(true);

        new Lwjgl3Application(new LudoBox(new GameScene()), config);
    }
}
