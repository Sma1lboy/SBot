package Commands.music;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class BotCommand extends ListenerAdapter {
    private AudioManager manager;
    private AudioChannel connectedChannel;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        this.manager = e.getGuild().getAudioManager();
        this.connectedChannel = e.getMember().getVoiceState().getChannel();
        if(e.getName().equals("join")) {
            if(checkIfUserNotInChannel()) {
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
        } else if(e.getName().equals("play")) {
            if(checkIfUserNotInChannel()){
                e.deferReply().queue();
                e.getHook().sendMessage("You are not in the channel").queue();
                return;
            } else {
                String url = e.getOption("url").getAsString();
                if(url != null) {
                    PlayerManager.getInstance().loadAndPlay(e.getTextChannel(), url);
                }
            }
//            if(!connectedChannel.equals(e.getVoiceChannel())) {
//                e.deferReply().queue();
//                e.getHook().sendMessage("You have to stay some voice channel with me!").queue();
//                return;
//            }
            //TODO here

        }
    }

    /**
     * Check weather it is a user in channel or not.
     * @return true if user not in channel, otherwise false
     */
    private boolean checkIfUserNotInChannel(){
        return connectedChannel == null;
    }
}
