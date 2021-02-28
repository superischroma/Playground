package me.superischroma.playground.swing;

import java.awt.*;

public class SSDEditorApplication extends Application
{
    public SSDEditorApplication()
    {
        super("Structured Storage Data Editor", new Dimension(720, 480));
        this.setBackground(DEFAULT_DARK_BACKGROUND_COLOR);
        this.addText("Browse for SSD files...", 5, 50,
                DEFAULT_DARK_TEXT_COLOR, DEFAULT_FONT.deriveFont(Font.PLAIN, 48));
    }
}