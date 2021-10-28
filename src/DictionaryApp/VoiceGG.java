package DictionaryApp;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoiceGG {
    public Voice voice;

    public VoiceGG(String name) {
        this.voice = VoiceManager.getInstance().getVoice("kevin16");
        this.voice.allocate();
    }

    public void say(String text) {
        this.voice.speak(text);
    }
}
