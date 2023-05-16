package gui;

import compressionmanager.Compress;
import compressionmanager.Decompress;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CompressorGUI {
    private JFrame frame;
    private JPanel panel;
    private JLabel nameLabel;
    private JButton compressButton;
    private JButton decompressButton;

    public CompressorGUI() {
        frame = new JFrame();
        frame.setResizable(false);

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(175,175,175,175));
        panel.setLayout(new GridLayout(0, 1));
        frame.add(panel, BorderLayout.CENTER);

        nameLabel = new JLabel("Kompresor");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(nameLabel);

        compressButton = new JButton("Skompresuj plik");
        compressButton.setPreferredSize(new Dimension(150,50));
        compressButton.setFont(new Font("Arial", Font.PLAIN, 14));
        compressButton.addActionListener(e -> showFileChooser(0));
        panel.add(compressButton);

        decompressButton = new JButton("Zdekompresuj plik");
        decompressButton.setPreferredSize(new Dimension(150,50));
        decompressButton.setFont(new Font("Arial", Font.PLAIN, 14));
        decompressButton.addActionListener(e -> showFileChooser(1));
        panel.add(decompressButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Kompresor");
        frame.pack();
        frame.setVisible(true);

    }

    private void showFileChooser(int userChoice) {
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Kuba\\IdeaProjects\\decompressor\\src");
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            if(userChoice == 0)
                initCompression(filePath);
            else
                initDecompression(filePath);
        }
    }

    private void initCompression(String filePath){
        Compress compressor = new Compress();
        try {
            compressor.compress(filePath);
            JOptionPane.showMessageDialog(frame, "Poprawnie skompresowano plik", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e, "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initDecompression(String filePath){
        Decompress decompressor = new Decompress();
        try {
            decompressor.decompress(filePath);
            JOptionPane.showMessageDialog(frame, "Poprawnie zdekompresowano plik", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e, "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
