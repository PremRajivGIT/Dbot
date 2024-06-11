import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Skip implements ICommand
{
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "skip's song";
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
        guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
        event.reply("skipped").queue();
    }
}
