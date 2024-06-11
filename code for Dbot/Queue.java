import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Queue implements ICommand{
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "queues the next song";
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
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Next in queue");
        if(queue.isEmpty()){
            embedBuilder.setDescription("Queue is empty");
        }
        for (int i =0; i < queue.size(); i++){
            AudioTrackInfo info = queue.get(i).getInfo();
            embedBuilder.addField(i+1 + ":", info.title,true);

        }
        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
