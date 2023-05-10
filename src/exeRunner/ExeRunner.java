package exerunner;

import java.io.IOException;

public class ExeRunner {
    private String path;
    private Process process;
    private int exitCode;

    public ExeRunner(String path) {
        this.path = path;
    }

    public void run(String command) throws IOException, InterruptedException {
        process = Runtime.getRuntime().exec(path + " " + command);
        exitCode = process.waitFor();
    }

    public int getExitCode() {
        return exitCode;
    }
}
