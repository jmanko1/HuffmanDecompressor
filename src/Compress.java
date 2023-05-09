import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class Compress {
    private ExeRunner exeRunner;
    public Compress() {
        exeRunner = new ExeRunner("src\\kompresor.exe");
    }

    public void compress(String filePath) throws IOException, InterruptedException, InvalidFileExtensionException {
        if(!filePath.endsWith(".txt"))
            throw new InvalidFileExtensionException("Nieobsługiwany typ pliku");
        exeRunner.run("-i " + filePath);
        switch (exeRunner.getExitCode()) {
            case 0:
                System.out.println("Poprawnie skompresowano");
                break;
            case 3:
                throw new FileNotFoundException("Plik nie istnieje");
            case 4:
                throw new AccessDeniedException("Brak uprawnień do pliku");
        }
    }
}
