import Commands.HelpCommand;
import Commands.music.BotCommand;
import Commands.MessageRecvied;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;


public class Main {
    private final static String BOT_TOKEN = "OTcxMjQxODAzMjIxMTc2NDEw.G8ukTa.yx9WnxdU1pT8l_i0qKp-OC7ivEBe1qgAnhB98s";
    public static void main(String[] args) throws Exception {

        JDA jda = JDABuilder.createDefault(BOT_TOKEN)
                .setActivity(Activity.listening("Testing"))
                .addEventListeners(new HelpCommand())
                .addEventListeners(new MessageRecvied())
                .addEventListeners(new BotCommand())
                .build();

        jda.upsertCommand("help", "help command promptout").queue();
        jda.upsertCommand("play", "play song").queue();
        jda.upsertCommand("join", "Join the discord if the bot hasn't yet").queue();
        jda.upsertCommand("leave", "leave the voice channel").queue();



    }
}
