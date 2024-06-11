import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main
{
    public static void  main(String [] args)
    {
        JDA jda = JDABuilder.createDefault("Your Discord Bots Token").build();
       jda.addEventListener(new Listener());
        CommandManager manager = new CommandManager();
        manager.add(new Sum());
        jda.addEventListener(manager);
        manager.add(new Play());
        manager.add(new Skip());
        manager.add(new Stop());
        manager.add(new NowPlaying());
        manager.add(new Queue());
        manager.add(new plist());
        manager.add(new PromptChat());
        manager.add(new MakeMusic());
        manager.add(new playgen());
        manager.add(new help());

    }
}
