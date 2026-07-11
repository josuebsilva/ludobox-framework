/**
 * @author       Josué Barbosa <contato.josuebarbosa@gmail.com>
 * @copyright    2026 Ludobox Studio.
 * @license      {@link https://opensource.org/licenses/MIT|MIT License}
 */

package core.com.ludobox.components;
import com.badlogic.gdx.audio.Music;

import core.com.ludobox.gameobjects.Component;

public class MusicSource extends Component {
    public Music music;

    public MusicSource(Music music) {
        this.music = music;
    }

    @Override
    public void onDestroy() {
        if(music != null) {
            music.dispose();   
        }
    }
}
