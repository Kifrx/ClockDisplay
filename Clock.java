import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
* A very simple GUI (graphical user interface) for the clock display.
* In this implementation, time runs at about 3 minutes per second, so that
* testing the display is a little quicker.
*
* @author Michael Kölling and David J. Barnes
* @version 2016.02.29
*/
public class Clock
{
    private JFrame frame;
    private JLabel label;
    private ClockDisplay clock;
    private boolean clockRunning = false;
    private TimerThread timerThread;
    private JLabel dateLabel;
    private JLabel tempLabel;
    
/**
* Constructor for objects of class Clock
*/
public Clock(int hour, int minute, int second)
{
    makeFrame();
    clock = new ClockDisplay(hour, minute, second);
    label.setText(clock.getTime()); // langsung tampilkan jam di GUI
    
     // ambil content pane (bisa diakses setelah makeFrame dipanggil)
        JPanel contentPane = (JPanel) frame.getContentPane();

        // buat panel NORTH yg berisi tanggal dan suhu (vertical)
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));

        dateLabel = new JLabel("Senin, 01 Januari 2025", SwingConstants.CENTER);
        dateLabel.setFont(dateLabel.getFont().deriveFont(24.0f));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tempLabel = new JLabel("Suhu: 27°C", SwingConstants.CENTER);
        tempLabel.setFont(tempLabel.getFont().deriveFont(24.0f));
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        northPanel.add(dateLabel);
        northPanel.add(tempLabel);

        contentPane.add(northPanel, BorderLayout.NORTH);

        // pack ulang supaya ukuran menyesuaikan komponen baru, lalu pusatkan frame
        frame.pack();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
}


/**
*
*/
private void start()
{
    clockRunning = true;
    timerThread = new TimerThread();
    timerThread.start();
}

/**
*
*/
private void stop()
{
    clockRunning = false;
}

/**
*
*/
private void step() {
    clock.timeTick();
        label.setText(clock.getTime());

        // tanggal & hari (pakai Java Time API)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFmt =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id"));
        dateLabel.setText(now.format(dateFmt));

        // suhu (dummy random contoh)
        int temp = 25 + (int)(Math.random() * 6); // 25-30 derajat
        tempLabel.setText("Suhu: " + temp + "°C");
    }


/**
* 'About' function: show the 'about' box.
*/
private void showAbout()
{
    JOptionPane.showMessageDialog (frame,
    "Clock Version 1.0n" +
    "A simple interface for the 'Objects First' clock display project",
    "About Clock",
    JOptionPane.INFORMATION_MESSAGE);
}

/**
* Quit function: quit the application.
*/
private void quit()
{
    System.exit(0);
}

/**
* Create the Swing frame and its content.
*/
private void makeFrame()
{
    frame = new JFrame("Clock");
    JPanel contentPane = (JPanel)frame.getContentPane();
    contentPane.setBorder(new EmptyBorder(1, 60, 1, 60));
    makeMenuBar(frame);
    // Specify the layout manager with nice spacing
    contentPane.setLayout(new BorderLayout(12, 12));
    // Create the image pane in the center
    label = new JLabel("00:00:00", SwingConstants.CENTER);
    Font displayFont = label.getFont().deriveFont(96.0f);
    label.setFont(displayFont);
    //imagePanel.setBorder(new EtchedBorder());
    contentPane.add(label, BorderLayout.CENTER);
    // Create the toolbar with the buttons
    JPanel toolbar = new JPanel();
    toolbar.setLayout(new GridLayout(1, 0));
    JButton startButton = new JButton("Start");
    startButton.addActionListener(e -> start());
    toolbar.add(startButton);
    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(e -> stop());
    toolbar.add(stopButton);
    JButton stepButton = new JButton("Step");
    stepButton.addActionListener(e -> step());
    toolbar.add(stepButton);
    // Add toolbar into panel with flow layout for spacing
    JPanel flow = new JPanel();
    flow.add(toolbar);
    contentPane.add(flow, BorderLayout.SOUTH);
    // building is done - arrange the components
    frame.pack();
    // place the frame at the center of the screen and show
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
    frame.setVisible(true);
}


/**
* Create the main frame's menu bar.
*
* @param frame The frame that the menu bar should be added to.
*/
private void makeMenuBar(JFrame frame)
{
    final int SHORTCUT_MASK =
    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    JMenuBar menubar = new JMenuBar();
    frame.setJMenuBar(menubar);
    JMenu menu;
    JMenuItem item;
    // create the File menu
    menu = new JMenu("File");
    menubar.add(menu);
    item = new JMenuItem("About Clock...");
    item.addActionListener(e -> showAbout());
    menu.add(item);
    menu.addSeparator();
    item = new JMenuItem("Quit");
    item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
    item.addActionListener(e -> quit());
    menu.add(item);
}
    class TimerThread extends Thread
{
    public void run()
    {
    while (clockRunning) {
        step();
        pause();
    }
}

     void pause()
{
    try {
        Thread.sleep(1000); // pause for 300 milliseconds
    }
    catch (InterruptedException exc) {
    }
}

}

}
