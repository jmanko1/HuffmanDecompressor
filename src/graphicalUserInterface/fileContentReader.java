package graphicalUserInterface;

import compressionManager.Decompressor;
import compressionManager.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/* Klasa czyta zawartość plików
* W przypadku zwykłego pliku zwraca bajty w postaci ciągu znaków,
* dla plików .cps zwracany jest ciąg znaków reprezentujący słownik. */
public class fileContentReader {
    private final Decompressor decompressor;
    private boolean isCPSFile;

    public fileContentReader() {
        decompressor = new Decompressor();
        isCPSFile = false;
    }

    /* Zwraca zawartość plików */
    private String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    /* Zwraca słownik pliku .cps */
    private String readFileDictionary(String filePath) throws IOException {
        ArrayList<Node> nodes = (ArrayList<Node>) decompressor.readDictionary(filePath);
        StringBuilder content = new StringBuilder();

        content.append("  ASCII\tBinary code\n\n  ");

        for (Node node : nodes) {
            content.append(node.getSign()).append("\t").append(node.getCode()).append("\n  ");
        }

        return content.toString();
    }

    /* Sprawdza jaki podano plik i decyduje co zwrócić */
    public String readFile(String filePath) throws IOException {
        if (!(new File(filePath)).isFile()) {
            throw new FileNotFoundException("Plik nie istnieje.");
        }

        if (filePath.endsWith(".cps")) {
            isCPSFile = true;
            return readFileDictionary(filePath);
        }
        else {
            isCPSFile = false;
            return readFileContent(filePath);
        }
    }

    public boolean isCPSFile() {
        return isCPSFile;
    }

    public int getFileSizeKB(String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        if (!file.isFile()) {
            throw new FileNotFoundException("Plik nie istnieje.");
        }

        return (int) (file.length() / 1024);
    }
}
