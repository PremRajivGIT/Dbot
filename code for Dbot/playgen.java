import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.File;
import java.util.List;

public class playgen implements ICommand{
    @Override
    public String getName() {
        return "playgen";
    }

    @Override
    public String getDescription() {
        return "Plays MusicGens Recent Track";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws InterruptedException {
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        Member member = event.getMember();
        GuildVoiceState membervoiceState = member.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(membervoiceState.getChannel());
        } else {
            if (selfVoiceState.getChannel() != membervoiceState.getChannel()) {
                event.reply("You need to be in the same channel").queue();
                return;
            }
        }
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        //Member member = event.getMember();
        // GuildVoiceState membervoiceState = member.getVoiceState();



        File mp3File = new File("mp3 file path");
        if (!mp3File.exists()) {
            event.reply("Generated music not found.").queue();
            return;
        }

        AudioPlayerManager playerManager = PlayerManager.get().getAudioPlayerManager();
        playerManager.loadItem(mp3File.getAbsolutePath(), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                guildMusicManager.getTrackScheduler().queue(audioTrack);
                // event.reply("Track added to the queue: " + audioTrack.getInfo().title).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                // Handle playlist loaded
            }

            @Override
            public void noMatches() {
                event.reply("No track found.").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.reply("Failed to load track: " + e.getMessage()).queue();
            }
        });
        event.reply("Enjoy your custom mix").queue();
    }
}
