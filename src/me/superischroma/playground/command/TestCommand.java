package me.superischroma.playground.command;

import me.superischroma.playground.timage.Timage;

@Command(description = "test lol")
public class TestCommand extends AbstractCommand
{
    @Override
    public void run(String[] args)
    {
        send(Timage.getMonthName() + " " + Timage.getYear());
        send(Timage.getMonth() + "/" + Timage.getDay() + "/" + Timage.getYear());
        send(Timage.getHours() + ":" + (Timage.getMinutes() > 9 ? Timage.getMinutes() : "0" + Timage.getMinutes()));
    }
}