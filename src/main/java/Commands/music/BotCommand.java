package Commands.music;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class BotCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        AudioManager manager = e.getGuild().getAudioManager();
        System.out.println(e.getName());
        if(e.getName().equals("join")) {
            AudioChannel connectedChannel = e.getMember().getVoiceState().getChannel();
            if(connectedChannel == null) {
                e.deferReply().queue();
                e.getHook().sendMessage("You are not in the channel").queue();
            } else {
                manager.openAudioConnection(connectedChannel);
                e.deferReply().queue();
                e.getHook().sendMessage("SBot already in the channel!").queue();
            }
        } else if (e.getName().equals("leave")) {
            manager.closeAudioConnection();
            e.deferReply().queue();
            e.getHook().sendMessage("SBot already close!").queue();
        }

    }
}
