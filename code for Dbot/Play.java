import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Play implements ICommand {
    private final AudioPlayerManager playerManager;

    public Play() {
        this.playerManager = new DefaultAudioPlayerManager();
       AudioSourceManagers.registerLocalSource(playerManager);

    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "plays music you desire";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> options = new ArrayList<>();
    
        options.add(new OptionData(OptionType.STRING, "name", "Name of any song", true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws InterruptedException {
        TextChannel channel = event.getChannel().asTextChannel();
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        Member member = event.getMember();
        GuildVoiceState membervoiceState = member.getVoiceState();

        if (!membervoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel").queue();
            return;
        }
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(membervoiceState.getChannel());
        } else {
            if (selfVoiceState.getChannel() != membervoiceState.getChannel()) {
                event.reply("You need to be in the same channel").queue();
                return;
            }
        }
        String input = event.getOption("name").getAsString();
        String trackUrl = null;

        try {
            new URL(input); // If this doesn't throw MalformedURLException, it's a direct URL
            trackUrl = input;
        } catch (MalformedURLException e) {
            trackUrl = "scsearch:" + input; // If it's not a direct URL, consider it as a search query
        }

        PlayerManager.get().play(channel, trackUrl);



        int i = 0;
        Thread.sleep(1500);
        try {
            while (i != 2) {
                if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo() != null ) {
                    //AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
                    AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
                    AudioTrack nextTrack = null;
                    BlockingQueue<AudioTrack> queue = guildMusicManager.getTrackScheduler().getQueue(); // Assuming a getQueue method exists

                    Iterator<AudioTrack> iterator = queue.iterator();
                    if (iterator.hasNext()) { // Skip the first element (currently playing song)
                        nextTrack = iterator.next();
                    }
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    if (nextTrack != null) { // Check if there's a next song
                        embedBuilder.addField("Next in queue:", nextTrack.getInfo().title + " by " + nextTrack.getInfo().author, false);
                    }
                    embedBuilder.setTitle("Currently playing song");
                    embedBuilder.setDescription("NAME: " + info.title);
                    embedBuilder.appendDescription("\nAUTHOR: " + info.author);
                    embedBuilder.appendDescription("\nURL" + info.uri);
                    embedBuilder.setColor(Color.red);
                    net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
                    net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
                    net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
                    net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = Button.danger("skip-button", "skip");
                    if (i == 1) {
                        event.replyEmbeds(embedBuilder.build()).setActionRow(stopButton, pauseButton, skipButton).queue();
                    }


                }
                i++;
            }
        } catch (NullPointerException e) {
            return;
        }

    }
