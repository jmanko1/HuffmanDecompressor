import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Decompress {
    private FileInputStream inputStream;
    private int data;
    private int extraBits;
    private int dictLength;
    private int sign;
    private int codeLength;
    private String code;

    public Decompress() {
        code = "";
    }

    public void decompress(String filePath) throws InvalidFileExtensionException, IOException {
        if(!filePath.endsWith(".cps"))
            throw new InvalidFileExtensionException("To nie jest plik skompresowany lub nie jest on obsługiwany");

        inputStream = new FileInputStream(filePath);

        extraBits = inputStream.read();
        dictLength = inputStream.read();

        for(int i = 0; i < dictLength; i++) {
            sign = inputStream.read();
            codeLength = inputStream.read();

            for(int j = 0; j < Math.ceil(codeLength / 8); j++) {
                data = inputStream.read();
                code += intToBinaryString(data);
            }

            code = code.substring()
        }
    }

    protected String intToBinaryString(int a) {
        String binaryString = Integer.toBinaryString(a & 0xFF);
        // uzupełnienie zerami z przodu, aby uzyskać 8 cyfr
        while (binaryString.length() < 8) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }
}
