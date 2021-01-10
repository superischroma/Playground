package me.superischroma.playground.command;

@Command(description = "get help with commands", aliases = "commands, cmds")
public class HelpCommand extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        StringBuilder builder = new StringBuilder()
                .append("commands:");
        for (AbstractCommand ac : CommandController.getAttachedCommands())
        {
            Command command = ac.getCommand();
            builder.append("\n - ").append(ac.getName());
            if (!command.description().isEmpty())
                builder.append("\n   description: ").append(command.description());
            builder.append("\n   usage: ").append(command.usage().replaceFirst("<command>", ac.getName()));
            if (!command.aliases().isEmpty())
                builder.append("\n   aliases: ").append(command.aliases());
        }
        send(builder.toString());
    }
}