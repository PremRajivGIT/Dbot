import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import LavaPlayer.TrackScheduler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class Stop implements ICommand{
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "stops the song";
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
        TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.getQueue().clear();
        trackScheduler.getPlayer().stopTrack();
        AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
        event.reply("Stopped").queue();
    }
}
