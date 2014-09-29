package org.craft.server;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.*;

import javax.swing.*;

import org.spongepowered.api.*;

public class ServerGui extends JFrame
{

    private static final long serialVersionUID = 1217538678572613343L;

    class ServerGuiLogHandler extends Handler
    {

        @Override
        public void publish(LogRecord record)
        {
            String text = getFormatter().format(record);
            console.setText(console.getText() + text);
        }

        @Override
        public void flush()
        {
            ;
        }

        @Override
        public void close() throws SecurityException
        {
            ;
        }

    }

    private JTextArea           console;
    private JTextField          userInput;
    private ServerGuiLogHandler logHandler;
    private Game                game;

    public ServerGui(Game gameInstance, String title)
    {
        game = gameInstance;
        setTitle(title);
        console = new JTextArea();
        console.setFont(new Font("Arial", Font.PLAIN, 13));
        console.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(console, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        add(scrollPane);
        userInput = new JTextField();
        userInput.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String txt = userInput.getText();
                    userInput.setText("");
                    game.broadcastMessage("<Server operator> " + txt);
                }
            }

            @Override
            public void keyPressed(KeyEvent e)
            {

            }
        });
        add(userInput, BorderLayout.SOUTH);
        logHandler = new ServerGuiLogHandler();

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                // TODO: Shutdown server
            }
        });
    }

    public Handler getLogHandler()
    {
        return logHandler;
    }
}
