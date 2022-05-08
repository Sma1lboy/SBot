package Commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class BotCommand extends ListenerAdapter {
    private AudioManager manager;
    private AudioChannel connectedChannel;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        this.manager = e.getGuild().getAudioManager();
        this.connectedChannel = e.getMember().getVoiceState().getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());

        if (e.getName().equals("play")) {
            //TODO have to check some precondition before the music start
            if (checkIfUserNotInChannel()) {
                e.deferReply().queue();
                e.getHook().sendMessage("You are not in the channel").queue();
                return;
            } else {


                OptionMapping url = e.getOption("url");
                if (url != null) {
                    String urlStr = url.getAsString();
                    if (!isUrl(urlStr)) {
                        urlStr = "ytsearch:" + urlStr;
                    }
                    if(!Objects.requireNonNull(e.getGuild().getSelfMember().getVoiceState()).inAudioChannel()) {
                        manager.openAudioConnection(connectedChannel);
                    }
                    //TODO not working with play command in different channel
//                    if(connectedChannel != e.getGuild().getSelfMember().getVoiceState().getChannel()) {
//                        e.deferReply().queue();
//                        e.getHook().sendMessage("You need to stay with your bot").queue();
//                        return;
//                    }
                    PlayerManager.getInstance().loadAndPlay(e.getTextChannel(), urlStr);
                    e.deferReply().queue();
                } else {
                    e.deferReply().queue();
                    e.getHook().sendMessage("please correct use the commmand /play <youtube link>").queue();
                }
            }
        } else if (e.getName().equals("stop")) {
            //TODO precondition, 1. such as command caller should in the same voice channel as bot

            if (!e.getMember().getVoiceState().inAudioChannel()) {
                e.deferReply().queue();
                e.getHook().sendMessage("`Before you using this command, you have to stay in the channel`").queue();
                return;
            }
            if(e.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
                e.deferReply().queue();
                e.getHook().sendMessage("Before you using this command, SBot has to stay `in the channel`").queue();
                return;
            }
            if (connectedChannel != e.getGuild().getSelfMember().getVoiceState().getChannel()) {
                e.deferReply().queue();
                e.getHook().sendMessage("You are not in the same channel with the bot!").queue();
                return;
            }

            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();
            manager.closeAudioConnection();

            e.deferReply().queue();
            e.getHook().sendMessage("The bot has been `stopped!`").queue();
        } else if (e.getName().equals("clear")) {
            //TODO some precondition
            musicManager.scheduler.queue.clear();
        } else if (e.getName().equals("skip")) {
            //TODO some precondition
            AudioPlayer audioPlayer = musicManager.audioPlayer;
            if(audioPlayer.getPlayingTrack() == null) {
                e.deferReply().queue();
                e.getHook().sendMessage("`There is no song playing right now`").queue();
                return;
            }
            musicManager.scheduler.nextTrack();
        } else if ( e.getName().equals("info")) {
            final AudioTrack track = musicManager.audioPlayer.getPlayingTrack();
            if(track == null) {
                e.deferReply().queue();;
                e.getHook().sendMessage("there is not such song currenly playing").queue();
                return;
            }
            final AudioTrackInfo info = track.getInfo();
            e.deferReply();
            e.getHook().sendMessageFormat("Now playing  `%s` by `%s` (link<%s>)", info.title, info.author, info.uri).queue();
        } else if (e.getName().equals("fb")) {
            if(e.getOption("content") == null) {
                e.deferReply().queue();
                e.getHook().sendMessage("you can't send nothing to the bot owner");
                return;
            }
            String content = e.getOption("content").getAsString();
            User user = e.getUser();
            System.out.println(e.getUser().getName());
            user.openPrivateChannel().complete().sendMessage(content).queue();

        }

    }

    /**
     * Check weather it is a user in channel or not.
     *
     * @return true if user not in channel, otherwise false
     */
    private boolean checkIfUserNotInChannel() {
        return connectedChannel == null;
    }

    /**
     * Check if it is a url giving by a string
     *
     * @param s the string that needs to check
     * @return true if it is url, otherwise not
     */
    private boolean isUrl(String s) {
        try {
            new URL(s);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
