package core.com.ludobox.components;
import com.badlogic.gdx.audio.Sound;

import core.com.ludobox.gameobjects.Component;

public class SoundSource extends Component {
    public Sound sound;

    public SoundSource(Sound sound) {
        this.sound = sound;
    }

    @Override
    public void onDestroy() {
        if(sound != null) {
            sound.dispose();   
        }
    }
}
