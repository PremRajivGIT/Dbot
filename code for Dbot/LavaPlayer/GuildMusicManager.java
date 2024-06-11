package LavaPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuildMusicManager {
    private TrackScheduler trackScheduler;
    private AudioForwarder audioForwarder;
    private AudioTrack currentTrack;

    public GuildMusicManager(AudioPlayerManager manager){
    AudioPlayer player = manager.createPlayer();
    trackScheduler = new TrackScheduler(player);
    player.addListener(trackScheduler);
    audioForwarder = new AudioForwarder(player);

 }

    public TrackScheduler getTrackScheduler() {
        return trackScheduler;
    }

    public AudioForwarder getAudioForwarder() {
        return audioForwarder;
    }
    public void setCurrentTrack(AudioTrack track) {
        this.currentTrack = track;

        // Create and send the embed when the track starts playing
        if (track != null) {
            AudioTrackInfo info = track.getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently playing song");
            embedBuilder.setDescription("NAME: " + info.title);
            embedBuilder.appendDescription("\nAUTHOR: " + info.author);
            embedBuilder.appendDescription("\nURL: " + info.uri);
            embedBuilder.setColor(Color.RED);
            // Add other customization as needed
            // Send the embed to the desired channel
        }
    }
}
