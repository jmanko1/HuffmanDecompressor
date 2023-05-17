package graphicalUserInterface;

import compressionManager.Decompressor;
import compressionManager.InvalidFileExtensionException;
import compressionManager.Node;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class fileContentReader {
    private Decompressor decompressor;

    public fileContentReader() {
        decompressor = new Decompressor();
    }

    public String readFileContent(String filePath) throws IOException {
        if (filePath.endsWith(".cps")) {
            ArrayList<Node> nodes = (ArrayList<Node>) decompressor.readDictionary(filePath);
            StringBuilder content = new StringBuilder();

            content.append("<sign>\t<code>\n");

            for (Node node : nodes)
            {
                content.append(node.getSign()).append("\t").append(node.getCode()).append("\n");
            }

            return content.toString();
        }
        else {
            Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        }
    }
}
