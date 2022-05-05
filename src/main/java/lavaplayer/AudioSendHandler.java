package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class AudioSendHandler implements net.dv8tion.jda.api.audio.AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;
    public AudioSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);

    }
    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        return  (ByteBuffer) (this.buffer.flip());
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
