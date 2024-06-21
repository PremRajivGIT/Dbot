import LavaPlayer.GuildMusicManager;
import LavaPlayer.PlayerManager;
import LavaPlayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;
import java.util.Objects;

public class Listener extends ListenerAdapter {
@Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    TrackScheduler trackScheduler = null;
    Message message = event.getMessage();
    if (Objects.equals(event.getButton().getId(), "stop-button")) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        trackScheduler = guildMusicManager.getTrackScheduler();
        trackScheduler.getQueue().clear();
        trackScheduler.getPlayer().stopTrack();
       EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Thanks For Using Me!... See Ya :)");
        net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause").withDisabled(true);
        net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play").withDisabled(true);
        net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop").withDisabled(true);
        net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("skip-button", "skip").withDisabled(true);
        event.editMessageEmbeds(embedBuilder.build()).setActionRow(stopButton, playButton, skipButton).queue();
        AudioManager audioManager = event.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
    } else if (Objects.equals(event.getButton().getId(), "skip-button")) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
        if (guildMusicManager.getTrackScheduler().getPlayer().isPaused()) {
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently playing song");
            embedBuilder.setDescription("NAME: " + info.title);
            embedBuilder.appendDescription("\nAUTHOR: " + info.author);
            embedBuilder.appendDescription("\nURL" + info.uri);
            embedBuilder.setColor(Color.red);
         //   embedBuilder.setThumbnail(info.uri);
            net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
            net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
            net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
            net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("skip-button", "skip");
            event.editMessageEmbeds(embedBuilder.build()).setActionRow(stopButton, playButton, skipButton).queue();
        } else if (!guildMusicManager.getTrackScheduler().getPlayer().isPaused()) {
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently playing song");
            embedBuilder.setDescription("NAME: " + info.title);
            embedBuilder.appendDescription("\nAUTHOR: " + info.author);
            embedBuilder.appendDescription("\nURL" + info.uri);
            embedBuilder.setColor(Color.red);
          //  embedBuilder.setThumbnail(info.uri);
            net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
            net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
            net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
            net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = Button.danger("skip-button", "skip");
            event.replyEmbeds(embedBuilder.build()).addActionRow(stopButton, pauseButton, skipButton).queue();

            if (info == null){event.reply("empty").queue();}


        }
    } else if (Objects.equals(event.getButton().getId(), "pause-button")) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().getPlayer().setPaused(true);
        if (guildMusicManager.getTrackScheduler().getPlayer().isPaused()) {
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently playing song");
            embedBuilder.setDescription("NAME: " + info.title);
            embedBuilder.appendDescription("\nAUTHOR: " + info.author);
            embedBuilder.appendDescription("\nURL" + info.uri);
            embedBuilder.setColor(Color.red);
          
            net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
            net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
            net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
            net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("skip-button", "skip");
            event.editMessageEmbeds(embedBuilder.build()).setActionRow(stopButton, playButton, skipButton).queue();
        }


    } else if (Objects.equals(event.getButton().getId(), "play-button")) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().getPlayer().setPaused(false);
        if (guildMusicManager.getTrackScheduler().getPlayer().isPaused()) {
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently playing song");
            embedBuilder.setDescription("NAME: " + info.title);
            embedBuilder.appendDescription("\nAUTHOR: " + info.author);
            embedBuilder.appendDescription("\nURL" + info.uri);
            embedBuilder.setColor(Color.red);
          //  embedBuilder.setThumbnail(info.uri);
            event.editMessageEmbeds().queue();
            net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
            Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
            Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
            Button skipButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("skip-button", "skip");
            event.replyEmbeds(embedBuilder.build()).addActionRow(stopButton, playButton, skipButton).queue();
        } else if (!guildMusicManager.getTrackScheduler().getPlayer().isPaused()) {
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently playing song");
            embedBuilder.setDescription("NAME: " + info.title);
            embedBuilder.appendDescription("\nAUTHOR: " + info.author);
            embedBuilder.appendDescription("\nURL" + info.uri);
            embedBuilder.setColor(Color.red);
          
            net.dv8tion.jda.api.interactions.components.buttons.Button pauseButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("pause-button", "pause");
            net.dv8tion.jda.api.interactions.components.buttons.Button playButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("play-button", "play");
            net.dv8tion.jda.api.interactions.components.buttons.Button stopButton = net.dv8tion.jda.api.interactions.components.buttons.Button.danger("stop-button", "stop");
            net.dv8tion.jda.api.interactions.components.buttons.Button skipButton = Button.danger("skip-button", "skip");
            event.editMessageEmbeds(embedBuilder.build()).setActionRow(stopButton, pauseButton, skipButton).queue();
        }

    }
}

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if(event.getModalId().equals("name-row")){
            ModalMapping nameValue = event.getValue("name-field");
            assert nameValue != null;
            String namefield = nameValue.getAsString();
            if (namefield.isBlank()) {
                namefield = "N/A";
            }
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle(namefield);
            builder.setDescription("The Description of "+namefield);
            builder.addField("Name",namefield, false);
            event.replyEmbeds(builder.build()).queue();
        }
    }
}
