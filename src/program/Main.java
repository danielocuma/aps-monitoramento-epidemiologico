package program;

import gui.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on"); // <- aqui
        System.setProperty("swing.aatext", "true");              // <- aqui

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}