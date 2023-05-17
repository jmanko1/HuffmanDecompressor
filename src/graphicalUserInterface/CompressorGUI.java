package graphicalUserInterface;

import compressionManager.Compressor;
import compressionManager.Decompressor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;

public class CompressorGUI {
    private JFrame frame;
    private JPanel optionPanel;
    private JPanel optionFileSubPanel;
    private JPanel optionCompressSubPanel;
    private JPanel fileContentPanel;
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
    private fileContentReader myReader;

    public CompressorGUI()
    {
        margin = 5;
        borderMargin = 10;

        compressor = new Compressor();
        decompressor = new Decompressor();
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

        // File content area
        fileContentArea = new JTextArea();
        fileContentArea.setEditable(false);
        fileContentArea.setLineWrap(true);
        fileContentArea.setBorder(new EmptyBorder(borderMargin, borderMargin, borderMargin, borderMargin));
        fileContentScrollPane = new JScrollPane(fileContentArea);
        fileContentPanel.add(fileContentScrollPane);

        // File path input button
        filePathInputButton = new MyButton("Choose file...");
        filePathInputButton.addActionListener(e -> showFileChooser());
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
                    fileContentArea.setText(myReader.readFileContent(filePathInputField.getText()));
                }
                catch (IOException e)
                {
                    fileContentArea.setText("");
                }
            }
        });
    }

    private void showFileChooser() {
        FileDialog fileChooser = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
        fileChooser.setDirectory("%userprofile%");
        fileChooser.setVisible(true);

        if (fileChooser.getDirectory() != null && fileChooser.getFile() != null) {
            String filename = fileChooser.getDirectory() + fileChooser.getFile();
            filePathInputField.setText(filename);
        }
    }

    private void Compress(){
        try {
            compressor.compress(filePathInputField.getText());
            JOptionPane.showMessageDialog(frame, "Poprawnie skompresowano plik", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Decompress(){
        try {
            decompressor.decompress(filePathInputField.getText());
            JOptionPane.showMessageDialog(frame, "Poprawnie zdekompresowano plik", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}