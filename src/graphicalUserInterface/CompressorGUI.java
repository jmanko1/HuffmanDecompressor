package graphicalUserInterface;

import compressionManager.Compressor;
import compressionManager.Decompressor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CompressorGUI {
    private JFrame frame;
    private JPanel optionPanel;
    private JPanel optionFileSubPanel;
    private JPanel optionCompressSubPanel;
    private JPanel fileContentPanel;
    private JLabel fileContentLabel;
    private JTextField filePathInputField;
    private JButton filePathInputButton;
    private JButton compressButton;
    private JButton decompressButton;

    private final int margin;
    private final int borderMargin;

    private fileContentReader myReader;

    public CompressorGUI()
    {
        margin = 5;
        borderMargin = 10;

        myReader = new fileContentReader();

        startWindow();
    }

    private void startWindow() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Huffman compressor");
        frame.setMinimumSize(new Dimension(600, 600));

        // Panel opcji
        optionPanel = new JPanel();
        optionPanel.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));
        optionPanel.setLayout(new BorderLayout());
        optionPanel.setBackground(Color.CYAN);
        frame.add(optionPanel, BorderLayout.NORTH);

        // SubPanel opcji - plik
        optionFileSubPanel = new JPanel();
        optionFileSubPanel.setLayout(new BorderLayout());
        optionFileSubPanel.setBackground(Color.CYAN);
        optionPanel.add(optionFileSubPanel, BorderLayout.NORTH);

        optionPanel.add(Box.createVerticalStrut(margin));

        // SubPanel opcji - kompresja
        optionCompressSubPanel = new JPanel();
        optionCompressSubPanel.setLayout(new BoxLayout(optionCompressSubPanel, BoxLayout.X_AXIS));
        optionCompressSubPanel.setBackground(Color.CYAN);
        optionPanel.add(optionCompressSubPanel, BorderLayout.SOUTH);

        // Panel pliku
        fileContentPanel = new JPanel();
        fileContentPanel.setLayout(new GridLayout(0, 1));
        fileContentPanel.setBackground(Color.RED);
        frame.add(fileContentPanel);

        fileContentLabel = new JLabel("Choose file!");
        fileContentPanel.add(fileContentLabel);

        // File path input button
        filePathInputButton = new MyButton("Choose file...");
        //filePathInputButton.setMinimumSize(new Dimension(50, 50));
        //filePathInputButton.setMaximumSize(new Dimension(100, 50));
        filePathInputButton.addActionListener(e -> showFileChooser(1));
        optionFileSubPanel.add(filePathInputButton, BorderLayout.WEST);

        // File path input
        filePathInputField = new JTextField();
        filePathInputField.setFont(new Font("Consolas", Font.PLAIN, 12));
        optionFileSubPanel.add(filePathInputField, BorderLayout.CENTER);

        // Compress button
        decompressButton = new MyButton("Compress");
        decompressButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, decompressButton.getMaximumSize().height));
        optionCompressSubPanel.add(decompressButton);

        optionCompressSubPanel.add(Box.createHorizontalStrut(margin));

        // Decompress button
        compressButton = new MyButton("Decompress");
        compressButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, compressButton.getMaximumSize().height));
        optionCompressSubPanel.add(compressButton);

        frame.pack();
        frame.setVisible(true);

        filePathInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleInputChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleInputChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleInputChange();
            }

            private void handleInputChange() {
                try {
                    fileContentLabel.setText(myReader.readFileContent(filePathInputField.getText()));
                }
                catch (IOException e)
                {
                    fileContentLabel.setText("lala");
                }
            }
        });
    }

    private void showFileChooser(int userChoice) {
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Kuba\\IdeaProjects\\decompressor\\src");

        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            filePathInputField.setText(filePath);
        }
    }

    private void initCompression(String filePath){
        Compressor compressor = new Compressor();
        try {
            compressor.compress(filePath);
            JOptionPane.showMessageDialog(frame, "Poprawnie skompresowano plik", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e, "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initDecompression(String filePath){
        Decompressor decompressor = new Decompressor();
        try {
            decompressor.decompress(filePath);
            JOptionPane.showMessageDialog(frame, "Poprawnie zdekompresowano plik", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e, "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}