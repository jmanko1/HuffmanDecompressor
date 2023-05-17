package compressionManager;

import exeRunner.ExeRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class Compressor {
    private ExeRunner exeRunner;
    public Compressor() {
        exeRunner = new ExeRunner("src\\compressor.exe");
    }

    public void compress(String filePath) throws IOException, InterruptedException {
        if (!(new File(filePath).isFile())) {
            throw new FileNotFoundException("Plik nie istnieje.");
        }

        if(!filePath.endsWith(".txt"))
            throw new InvalidFileExtensionException("Nieobsługiwany typ pliku przez kompresor.");

        exeRunner.run("-i " + filePath);

        switch (exeRunner.getExitCode()) {
            case 0 -> System.out.println("Poprawnie skompresowano");
            case 3 -> throw new FileNotFoundException("Plik nie istnieje");
            case 4 -> throw new AccessDeniedException("Brak uprawnień do pliku");
        }
    }
}
