import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class UIWindow extends JFrame {
    String fieldHandler = """ 
            \t                                                                    \s
            \t                                                                    \s
            \t                                                                    \s
            \t     `7MMF'  `7MMF' .g8""8q. `7MMF'`7MMF'   `7MF'      `7MMF'   `7MF'
            \t       MM      MM .dP'    `YM. MM    MM       M          MM       M \s
            \t       MM      MM dM'      `MM MM    MM       M  pd""b.  MM       M \s
            \t       MMmmmmmmMM MM        MM MM    MM       M (O)  `8b MM       M \s
            \t       MM      MM MM.      ,MP MM    MM       M      ,89 MM       M \s
            \t       MM      MM `Mb.    ,dP' MM    YM.     ,M    ""Yb. YM.     ,M \s
            \t     .JMML.  .JMML. `"bmmd"' .JMML.   `bmmmmd"'       88  `bmmmmd"' \s
            \t                                                (O)  .M'            \s
            \t                                                 bmmmd'             """;
    private JTextArea gameFieldTextArea;
    private JTextArea outputTextArea;

    private final InteractiveKeyListener interactiveKeyListener;
    private final SequentialKeyListener sequentialKeyListener;

    private String inputLine = "";

    public UIWindow() {

        // General window setup
        BufferedImage myAppImage = loadIcon("./resources/PonyKnightV2.jpg");
        if(myAppImage != null)
        {
            Logger.getLogger().logSuccess("Icon loaded");
            setIconImage(myAppImage);
        }
        else {
            Logger.getLogger().logWarning("Icon not loaded");
        }
        setTitle("HOIU3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0,0));
        setUndecorated( true );
        getRootPane().setWindowDecorationStyle(JRootPane.ERROR_DIALOG);
        setBackground(Color.black);




        // Game field display area
        gameFieldTextArea = new JTextArea(19, 63);
        gameFieldTextArea.setBackground(Color.BLACK);
        gameFieldTextArea.setForeground(Color.decode("#39FF14"));
        gameFieldTextArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        gameFieldTextArea.setEditable(false);
//        gameFieldTextArea.setBorder(BorderFactory.createLineBorder(Color.black, 10));
//        gameFieldTextArea.insert(fieldHandler, 0);

        // Output display area
        outputTextArea = new JTextArea(10, 63);
        outputTextArea.setBackground(Color.BLACK);
        outputTextArea.setForeground(Color.decode("#39FF14"));
//        outputTextArea.append("Test text\n");
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        outputTextArea.setEditable(false);


        // Init listeners
        sequentialKeyListener = new SequentialKeyListener(outputTextArea);
        interactiveKeyListener = new InteractiveKeyListener();

//        gameFieldTextArea.setPreferredSize(gameFieldTextArea.getSize());
//        outputTextArea.setPreferredSize(outputTextArea.getSize());
        JScrollPane jp = new JScrollPane(outputTextArea);
        jp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        jp.setViewportBorder(BorderFactory.createLineBorder(Color.black));
        jp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        // Add all components

        add(jp, BorderLayout.PAGE_END);
        add(gameFieldTextArea, BorderLayout.PAGE_START);

        addWindowListener(new WindowListenerSetFocusOn(outputTextArea));

        pack();
        setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        // Запуск в потоке обработки событий
        SwingUtilities.invokeLater(() -> {
            UIWindow window = new UIWindow();
            window.display();
            window.setVisible(true);

        });
    }
    void display() {
        gameFieldTextArea.setText("\uD83E\uDDD9" +
                "\uD83D\uDE00" +
                "\uD83C\uDFF0" +
                "\uD83D\uDC00" +
                "\uD83C\uDF46" +
                "\uD83D\uDC8E" +
                "\uD83C\uDF7B" +
                "\uD83C\uDF7A");
    }

    JTextArea getGameFieldTextArea() {
        return gameFieldTextArea;
    }
    JTextArea getOutputTextField() {
        return outputTextArea;
    }
    private BufferedImage loadIcon(String strPath)
    {
        Logger.getLogger().logInfo("Loading Icon: " + strPath);
        try {
            return ImageIO.read(new File(strPath));
        } catch (IOException e) {
            Logger.getLogger().logWarning("Failed to load icon: " + strPath);
            return null;
        }
    }

    public void getInteractiveInput(Function<Integer, Boolean> listener, ArrayList<KeyEvent> keyEvents) {
        interactiveKeyListener.setKeyEvents(keyEvents);
        outputTextArea.addKeyListener(interactiveKeyListener);
        synchronized(interactiveKeyListener) {
            try {
                interactiveKeyListener.wait();
            } catch (InterruptedException e) {
                Logger.getLogger().logError("Interrupted while waiting for interactive key listener");
                throw new RuntimeException(e);
            }
        }
        removeKeyListener(interactiveKeyListener);
    }

    public void getInteractiveInput(ArrayList<KeyEvent> keyEvents) {
        interactiveKeyListener.setKeyEvents(keyEvents);
        outputTextArea.addKeyListener(interactiveKeyListener);
        synchronized(interactiveKeyListener) {
            try {
                interactiveKeyListener.wait();
            } catch (InterruptedException e) {
                Logger.getLogger().logError("Interrupted while waiting for interactive key listener");
                throw new RuntimeException(e);
            }
        }
        removeKeyListener(interactiveKeyListener);
    }

    public void startInteractiveInput(Function<Integer, Boolean> listener, ArrayList<KeyEvent> keyEvents) {
        interactiveKeyListener.setKeyEvents(keyEvents);
        interactiveKeyListener.setListener(listener);
        outputTextArea.addKeyListener(interactiveKeyListener);
//        synchronized(interactiveKeyListener) {
//            try {
//                interactiveKeyListener.wait();
//            } catch (InterruptedException e) {
//                Logger.getLogger().logError("Interrupted while waiting for interactive key listener");
//                throw new RuntimeException(e);
//            }
//        }
//        removeKeyListener(interactiveKeyListener);
    }
    public void endInteractiveInput() {
        removeKeyListener(interactiveKeyListener);
    }


    public String getLineInput() {
        int inputStartOffset;
        sequentialKeyListener.reset();
        inputStartOffset = outputTextArea.getText().length();
        sequentialKeyListener.setStartOffset(inputStartOffset);
        outputTextArea.addKeyListener(sequentialKeyListener);
        synchronized(sequentialKeyListener) {
            try {
                sequentialKeyListener.wait();
            } catch (InterruptedException e) {
                Logger.getLogger().logError("Interrupted while waiting for sequential key listener");
                throw new RuntimeException(e);
            }
        }
        removeKeyListener(sequentialKeyListener);
        Logger.getLogger().tag("INPUT").logSuccess("Finished waiting for sequential key listener, result: " + sequentialKeyListener.getCurrentString());
        return sequentialKeyListener.getCurrentString();
    }

    public void enableField() {
        gameFieldTextArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
    }

    public void disableField() {
        gameFieldTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
    }

    public void showFieldSlow(String fieldString, int lineDelay) {
        enableField();
        String[] lines = fieldString.split("\n");
        gameFieldTextArea.setText("");
        for (String line : lines) {
            gameFieldTextArea.append(line + "\n");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Logger.getLogger().tag("Interrupted while printing field");
                throw new RuntimeException(e);
            }
        }
//        gameFieldTextArea.setText(fieldString);
    }

    public void showField(String fieldString) {
        enableField();
        gameFieldTextArea.setText(fieldString);
    }

    public void showHelloMessage() {
        disableField();
        gameFieldTextArea.setText(fieldHandler);
    }

    public void debugFocus() {
        Logger.getLogger().tag("FOCUS DEBUG").logInfo("Output text area: " + outputTextArea.toString());
        Logger.getLogger().tag("FOCUS DEBUG").logInfo(getFocusOwner().toString());
    }
}
