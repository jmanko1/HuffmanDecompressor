package exeRunner;

import java.io.IOException;

/* Klasa odpowiada za uruchamianie plików wykonywalnych,
* Podczas tworzenia obiektu, określamy plik, który ma być wykonywany. */
public class ExeRunner {
    private final String path;
    private Process process;
    private int exitCode;

    public ExeRunner(String path) {
        this.path = path;
    }

    /* Funkcja uruchamia plik wykonywalny i czeka na zakończenie jego działania
    * by zapisać jego exit code. */
    public void run(String command) throws IOException, InterruptedException {
        process = Runtime.getRuntime().exec(path + " " + command);
        exitCode = process.waitFor();
    }

    public int getExitCode() {
        return exitCode;
    }
}
