package me.superischroma.playground.swing;

import me.superischroma.playground.util.Location2;
import me.superischroma.playground.util.PlaygroundUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Application extends JFrame
{
    protected static final Color DEFAULT_LIGHT_BACKGROUND_COLOR = new Color(240, 240, 240);
    protected static final Color DEFAULT_DARK_BACKGROUND_COLOR = new Color(5, 5, 5);
    protected static final Color DEFAULT_LIGHT_TEXT_COLOR = new Color(5, 5, 5);
    protected static final Color DEFAULT_DARK_TEXT_COLOR = new Color(240, 240, 240);
    protected static final Font DEFAULT_FONT = new Font("Montserrat", Font.PLAIN, 16);

    private ApplicationPanel panel;
    private List<ApplicationText> textComponents;

    protected Application(String title, Dimension size, boolean resizeable)
    {
        this.setTitle(title);
        this.setSize(size);
        this.setResizable(resizeable);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.panel = new ApplicationPanel(this);
        this.add(panel);
        this.textComponents = new ArrayList<>();
        this.setVisible(true);
    }

    protected Application(String title, Dimension size)
    {
        this(title, size, false);
    }

    public void setBackground(Color color)
    {
        this.getContentPane().setBackground(color);
    }

    public ApplicationText addText(String string, int x, int y, Color color, Font font)
    {
        ApplicationText text = new ApplicationText(string, new Location2(x, y), color, font);
        textComponents.add(text);
        return text;
    }

    public void removeText(ApplicationText text)
    {
        textComponents.remove(text);
    }

    public void removeText(int index)
    {
        textComponents.remove(index);
    }

    public void removeText(String text)
    {
        PlaygroundUtils.removeIf(textComponents, (t) -> t.getText().equals(text), 1);
    }

    private static class ApplicationPanel extends JPanel
    {
        private final Application parent;

        public ApplicationPanel(Application parent)
        {
            this.parent = parent;
        }

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            this.setOpaque(true);
            this.setBackground(parent.getContentPane().getBackground());
            for (ApplicationText text : parent.textComponents)
            {
                this.setFont(text.getFont());
                this.setForeground(text.getColor());
                g2d.drawString(text.getText(), text.getLocation().getX(), text.getLocation().getY());
            }
        }
    }

    private static class ApplicationText
    {
        private String text;
        private Location2 location;
        private Color color;
        private Font font;

        public ApplicationText(String text, Location2 location, Color color, Font font)
        {
            this.text = text;
            this.location = location;
            this.color = color;
            this.font = font;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public Location2 getLocation()
        {
            return location;
        }

        public void setLocation(Location2 location)
        {
            this.location = location;
        }

        public Color getColor()
        {
            return color;
        }

        public void setColor(Color color)
        {
            this.color = color;
        }

        public Font getFont()
        {
            return font;
        }

        public void setFont(Font font)
        {
            this.font = font;
        }
    }
}