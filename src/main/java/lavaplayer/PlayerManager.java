package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManager;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManager = new HashMap<>();
        this.audioPlayerManager =  new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }
    public GuildMusicManager getMusicManager(Guild g) {
        return this.musicManager.computeIfAbsent(g.getIdLong(), (guildID) -> {
            final GuildMusicManager guildMusicManager =  new GuildMusicManager(this.audioPlayerManager);
            g.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }
    public void loadAndPlay(TextChannel channel, String url) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                channel.sendMessage("Playing track ")
                        .append(track.getInfo().title).queue();
            }
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
//                channel.sendMessage("from playlist ").append(playlist.getName()).append(""+tracks.size()).queue();
//                for(AudioTrack track : tracks) {
//                    channel.sendMessage(track.getInfo().title).queue();
//                     musicManager.scheduler.queue(track);
//                }
                musicManager.scheduler.queue(tracks.get(0));
                channel.sendMessage("Playing track ")
                        .append(tracks.get(0).getInfo().title).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }
    public static PlayerManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
