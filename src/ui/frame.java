package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class frame extends JFrame {
    private static final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int WINDOW_WIDTH = screen.width/2;
    public static final int WINDOW_HEIGHT = screen.height-80;
    public static final Color BACKGROUND_COLOR = new Color(245, 239, 220);
    private CardLayout cardLayout;
    private JPanel control;
    private homescreenDelegate delegate;

    public frame(homescreenDelegate delegate) {
        this.delegate = delegate;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("cs340 project");
        setBackground(BACKGROUND_COLOR);
        centreOnScreen();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                delegate.close();
                System.exit(0);
            }
        });

        control = new homescreen(this, delegate);
        setContentPane(control);
        setVisible(true);
    }

    public void showCard(String name) {
        cardLayout.show(control, name);
    }

    // EFFECTS: location of frame is set so that the frame is centred on desktop
    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }
}
