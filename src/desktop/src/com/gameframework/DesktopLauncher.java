package desktop.src.com.gameframework;
import core.com.gameframework.core.GameFramework;
import game.demo.GameScene;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
public class DesktopLauncher {
    public static void main(String[] args) throws Exception {
        System.out.println("GameFramework Engine");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GameFramework Engine");
        config.setWindowedMode(GameFramework.VIRTUAL_WIDTH, GameFramework.VIRTUAL_HEIGHT);
        config.setForegroundFPS(60);
        config.useVsync(true);

        new Lwjgl3Application(new GameFramework(new GameScene()), config);
    }
}
