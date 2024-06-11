import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static javax.management.remote.JMXConnectorFactory.connect;

public class MakeMusic implements ICommand {

    @Override
    public String getName() {
        return "makemusic"; // Command name
    }

    @Override
    public String getDescription() {
        return "Generates and plays music "; // Command description
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "text", "The text input for generating music \n ex: drums with fast pacing").setRequired(true)
        );
        data.add(new OptionData(OptionType.STRING, "duration", "prompts ai to make 30 Sec long audio", false).addChoice("30 Sec","30 Sec"));

         return data;


    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws InterruptedException {
        try {
            if (event.getOption("duration") == null){

                // Execute Python script to generate music
            // Execute Python script to generate music with maxNewTokens argument
            ProcessBuilder processBuilder = new ProcessBuilder("python", "python script path", event.getOption("text").getAsString(), "--max_new_tokens", "600");
            Process process = processBuilder.start();
                 final Message initialMessage = event.getChannel().sendMessage("Mixing Beats... takes a min or two").complete();
                 int exitCode = process.waitFor();
            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();
            Member member = event.getMember();
            GuildVoiceState membervoiceState = member.getVoiceState();
            event.getChannel().editMessageById(initialMessage.getId(),"Done Enjoy Your Mix... for replay use /playgen").queue();

            if (!selfVoiceState.inAudioChannel()) {
                event.getGuild().getAudioManager().openAudioConnection(membervoiceState.getChannel());
            } else {
                if (selfVoiceState.getChannel() != membervoiceState.getChannel()) {
                    event.reply("You need to be in the same channel").queue();
                    return;
                }
            }
                    if (exitCode != 0) {
                        event.reply("Failed to generate music: Process exited with non-zero status").queue();
                        return;

                }

//            if (exitCode == 0 && event.getOption("duration") != null){
//                event.getChannel().editMessageById(initialMessage.getId(),"1Min done... use /playgen to play 30 Sec track").queue();
//                return;
//            }

                // Handle output
                 if (exitCode == 0 && event.getOption("duration") == null) {
                    GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
                    //Member member = event.getMember();
                   // GuildVoiceState membervoiceState = member.getVoiceState();



                    File mp3File = new File("C:\\Users\\Prem Rajiv\\IdeaProjects\\BOTtt\\musicgen_out.mp3");
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
                } else {
                    event.reply("Failed to generate music.").queue();
                }
            }
            if (event.getOption("duration") != null){

                // Execute Python script to generate music
                // Execute Python script to generate music with maxNewTokens argument
                ProcessBuilder processBuilder = new ProcessBuilder("python", "python script path", event.getOption("text").getAsString(), "--max_new_tokens", "1500");
                Process process = processBuilder.start();
                final Message initialMessage = event.getChannel().sendMessage("Mixing Beats... takes a while, drink coffee till then").complete();

                int exitCode = process.waitFor();


                if (exitCode != 0) {
                    event.reply("Failed to generate music: Process exited with non-zero status").queue();
                    return;

                }

                if (exitCode == 0 && event.getOption("duration") != null){
                    event.getChannel().editMessageById(initialMessage.getId(),"1Min done... use /playgen to play 1 Min track").queue();
                    return;
                }


            }
        } catch(IOException | InterruptedException e){
               // event.reply("An error occurred while generating music.").queue();
            try{
                Thread.sleep(180000);
                event.getChannel().sendMessage("Finally done! use /playgen to play").queue();
            } catch (Exception f){
                f.printStackTrace();

            }
                e.printStackTrace();


            }
    }
    }
