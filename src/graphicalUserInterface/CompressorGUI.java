package graphicalUserInterface;

import compressionManager.Compressor;
import compressionManager.Decompressor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;

import com.formdev.flatlaf.FlatDarculaLaf;


public class CompressorGUI {
    private JFrame frame;
    private JPanel optionPanel;
    private JPanel optionFileSubPanel;
    private JPanel optionCompressSubPanel;
    private JPanel fileContentPanel;
    private JLabel fileContentTitle;
    private JTextArea fileContentArea;
    private JScrollPane fileContentScrollPane;
    private JTextField filePathInputField;
    private JButton filePathInputButton;
    private JButton compressButton;
    private JButton decompressButton;

    private final int margin;
    private final int borderMargin;

    Compressor compressor;
    Decompressor decompressor;
    private fileContentReader fileReader;

    public CompressorGUI()
    {
        margin = 5;
        borderMargin = 10;

        compressor = new Compressor();
        decompressor = new Decompressor();
        fileReader = new fileContentReader();

        FlatDarculaLaf.setup();

        startWindow();
    }

    private void startWindow() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Huffman compressor");
        frame.setMinimumSize(new Dimension(700, 350));

        // Panel opcji
        optionPanel = new JPanel();
        optionPanel.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));
        optionPanel.setLayout(new BorderLayout());
        frame.add(optionPanel, BorderLayout.NORTH);

        // SubPanel opcji - plik
        optionFileSubPanel = new JPanel();
        optionFileSubPanel.setLayout(new BorderLayout());
        optionPanel.add(optionFileSubPanel, BorderLayout.NORTH);

        optionPanel.add(Box.createVerticalStrut(margin));

        // SubPanel opcji - kompresja
        optionCompressSubPanel = new JPanel();
        optionCompressSubPanel.setLayout(new BoxLayout(optionCompressSubPanel, BoxLayout.X_AXIS));
        optionPanel.add(optionCompressSubPanel, BorderLayout.SOUTH);

        // Panel pliku
        fileContentPanel = new JPanel();
        fileContentPanel.setLayout(new BorderLayout());
        fileContentPanel.setBorder(new EmptyBorder(0, borderMargin, 0, borderMargin));
        frame.add(fileContentPanel);

        // File content area
        fileContentArea = new JTextArea();
        fileContentArea.setEditable(false);
        fileContentArea.setLineWrap(true);
        fileContentArea.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));
        fileContentScrollPane = new JScrollPane(fileContentArea);
        fileContentPanel.add(fileContentScrollPane, BorderLayout.CENTER);

        fileContentTitle = new JLabel("Choose file");
        fileContentTitle.setHorizontalAlignment(JLabel.CENTER);
        fileContentTitle.setBorder(new EmptyBorder(0, 0, margin, 0));
        fileContentPanel.add(fileContentTitle, BorderLayout.SOUTH);

        // File path input button
        filePathInputButton = new MyButton("Choose file...");
        filePathInputButton.addActionListener(e -> showFileDialog());
        optionFileSubPanel.add(filePathInputButton, BorderLayout.WEST);

        // File path input
        filePathInputField = new JTextField();
        filePathInputField.setFont(new Font("Consolas", Font.PLAIN, 12));
        optionFileSubPanel.add(filePathInputField, BorderLayout.CENTER);

        // Compress button
        compressButton = new MyButton("Compress");
        compressButton.addActionListener(e -> Compress());
        compressButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, compressButton.getMaximumSize().height));
        optionCompressSubPanel.add(compressButton);

        optionCompressSubPanel.add(Box.createHorizontalStrut(margin));

        // Decompress button
        decompressButton = new MyButton("Decompress");
        decompressButton.addActionListener(e -> Decompress());
        decompressButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, decompressButton.getMaximumSize().height));
        optionCompressSubPanel.add(decompressButton);

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
                    fileContentArea.setText(fileReader.readFile(filePathInputField.getText()));

                    if (fileReader.isCPSFile()) {
                        fileContentTitle.setText("CPS File dictionary");
                    }
                    else {
                        fileContentTitle.setText("File content");
                    }
                }
                catch (IOException e)
                {
                    fileContentArea.setText("");
                    fileContentTitle.setText("Choose file");
                }
            }
        });
    }

    private void showFileDialog() {
        FileDialog fileDialog = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fileDialog.setDirectory("%userprofile%");
        fileDialog.setVisible(true);

        if (fileDialog.getDirectory() != null && fileDialog.getFile() != null) {
            String filename = fileDialog.getDirectory() + fileDialog.getFile();
            filePathInputField.setText(filename);
        }
    }

    private void Compress() {
        try {
            compressor.compress(filePathInputField.getText());

            int sizeOld = fileReader.getFileSizeKB(filePathInputField.getText());
            int sizeNew = fileReader.getFileSizeKB(filePathInputField.getText().replace(".txt", ".cps"));

            String message = "Poprawnie skompresowano plik.\n\n";
            message += "Rozmiar przed kompresją: " + sizeOld + " KB\n";
            message += "Rozmiar po kompresji: " + sizeNew + " KB\n";

            JOptionPane.showMessageDialog(frame, message, "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Decompress() {
        try {
            decompressor.decompress(filePathInputField.getText());
            JOptionPane.showMessageDialog(frame, "Poprawnie zdekompresowano plik.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}