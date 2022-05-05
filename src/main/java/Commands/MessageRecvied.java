package Commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lavaplayer.AudioSendHandler;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class MessageRecvied extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if(e.getName().equals("play")) {
            Guild guild = e.getGuild();
            assert guild != null;
//            VoiceChannel channel = guild.getVoiceChannelsByName("music", true).get(0);
            AudioChannel channel2 = e.getMember().getVoiceState().getChannel();
            AudioManager manager = guild.getAudioManager();
//            manager.setSendingHandler(new AudioSendHandler());
            manager.openAudioConnection(channel2);
        }
    }
}
