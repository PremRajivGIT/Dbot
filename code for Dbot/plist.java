import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class plist implements ICommand {
    @Override
    public String getName() {
        return "playlist";
    }

    @Override
    public String getDescription() {
        return "will get a soundcloud playlist\n for private playlist provide a link";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "name", "Name of any song", true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws InterruptedException {
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
        String name = event.getOption("name").getAsString();
        try {
            new URI(name);
        } catch (URISyntaxException e) {
            name = "scsearch:" + name;
        }
        PlayerManager playerManager = PlayerManager.get();
        playerManager.PlayList(event.getGuild(), name);

        int i = 0;
        try {
            Thread.sleep(1500);
            while (i != 2) {
                if (guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo() != null /*&& guildMusicManager.getTrackScheduler().getQueue().contains(false)*/) {
                    AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Currently playing song");
                    embedBuilder.setDescription("NAME: " + info.title);
                    embedBuilder.appendDescription("\nAUTHOR: " + info.author);
                    embedBuilder.appendDescription("\nURL" + info.uri);
                    embedBuilder.setColor(Color.red);
                  //  embedBuilder.setThumbnail("https://i1.sndcdn.com/artworks-yIdcTwzklnwzNx9b-JtP8yg-t500x500.jpg");
                    net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
                    net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
                    net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
                    net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = Button.danger("skip-button", "skip");
                    //event.replyEmbeds(embedBuilder.build()).addActionRow(stopButton,pauseButton,skipButton).queue();
                    if (i == 1) {
                        event.replyEmbeds(embedBuilder.build()).setActionRow(stopButton, pauseButton, skipButton).queue();
                    }


                }
                i++;
            }
        }catch(NullPointerException e){
            return;
        }
    }
}
