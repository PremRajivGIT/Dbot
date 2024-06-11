import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.swing.*;
import java.util.List;

public class help implements ICommand{
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Lists all commands";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws InterruptedException {

        event.getChannel().sendMessage("**Commands:** \n***/play*** - adds a song track and play's it.  \n***/playlist*** - provide a playlist link from soundcloud. \n***/prompt*** - you can chat with the bot its funny try it out! \n***/makemusic*** - provide a prompt for music generation, example = electronic drums fast paced.\n***/playgen*** - plays recently generated music by makemusic command.").queue();
    }
}
