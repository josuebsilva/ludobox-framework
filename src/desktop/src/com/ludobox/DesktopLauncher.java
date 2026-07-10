package desktop.src.com.ludobox;
import core.com.ludobox.core.Ludobox;
import game.triangle.GameLevel;

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

        new Lwjgl3Application(new Ludobox(new GameLevel()), config);
    }
}
