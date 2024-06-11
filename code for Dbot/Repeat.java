import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Repeat implements ICommand{
    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getDescription() {
        return "repeats the song";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState membervoiceState = member.getVoiceState();
        if (!membervoiceState.inAudioChannel()){
            event.reply("You need to be in a voice channel").queue();
            return;
        }
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if(!selfVoiceState.inAudioChannel()){
            event.reply("I am not in a Audio Channel").queue();
            return;
        }
        if(selfVoiceState.getChannel() != membervoiceState.getChannel()){
            event.reply("You ain't in the same channel").queue();
            return;

        }
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        boolean isRepeat = !guildMusicManager.getTrackScheduler().isRepeat();
        guildMusicManager.getTrackScheduler().setRepeat(isRepeat);
        event.reply("Repeat is now" + isRepeat).queue();

    }

}
