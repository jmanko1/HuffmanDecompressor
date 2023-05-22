package compressionManager;

import java.io.*;
import java.util.*;

/* Klasa odpowiadająca za dekompresje plików .cps
* W jej zakresie leży czytanie słownika, budowa drzewa Huffmana oraz zasadnicza dekompresja. */
public class Decompressor {
    private FileInputStream inputStream;
    private int data;
    private int extraBits;
    private int dictLength;
    private int codeLength;

    /* Funkcja czyta plik .cps i zwraca listę obiektów Node tworzących słownik */
    public List<Node> readDictionary(String filePath) throws IOException {
        if (!(new File(filePath).isFile())) {
            throw new FileNotFoundException("Plik nie istnieje.");
        }

        if(!filePath.endsWith(".cps"))
            throw new InvalidFileExtensionException("Dekompresor nie obsługuje pliku o tym rozszerzeniu.");

        inputStream = new FileInputStream(filePath);
        extraBits = inputStream.read(); // Czytanie pierwszego bajtu pliku
        dictLength = inputStream.read(); // Czytanie drugiego bajtu pliku
        List<Node> nodeList = new ArrayList<>();

        /* Pętla czytająca liście drzewa Huffmana */
        for(int i = 0; i < dictLength; i++) {
            int sign = inputStream.read(); // Czytanie znaku
            codeLength = inputStream.read(); // Czytanie długości kodu znaku
            String code = "";

            /* Czytanie bajtów zależnie od długości kodu znaku.
            * Jeśli długość kodu ma więcej niż 8 bitów to należy wczytać 2 bajty itd. */
            for(int j = 0; j < (int) Math.ceil(codeLength / 8.0); j++) {
                data = inputStream.read();
                code += intToBinaryString(data);
            }

            /* Usuwanie dopełniających zer z lewej strony  */
            code = code.substring((int) ((Math.ceil(codeLength / 8.0) * 8) - codeLength), (int) (Math.ceil(codeLength / 8.0) * 8));

            nodeList.add(new Node(sign, code));
        }
        inputStream.close();

        return nodeList;
    }

    protected String intToBinaryString(int a) {
        // Zamiana liczby na system dwójkowy
        String binaryString = Integer.toBinaryString(a & 0xFF);

        // uzupełnienie zerami z przodu, aby uzyskać 8 cyfr
        while (binaryString.length() < 8) {
            binaryString = "0" + binaryString;
        }

        return binaryString;
    }

    /* Funkcja tworzy strukturę drzewa Huffmana na podstawie słownika pliku
    * Zwracany jest obiekt Node, który jest korzeniem drzewa */
    private Node makeBinaryTree(String filePath) throws IOException {
        List<Node> nodes = readDictionary(filePath);

        /* root jest węzłem przechowującym korzeń drzewa, pointer to węzeł pomocniczy,
        * za jego pomocą 'chodzimy' po drzewie i wykonujemy w nim operacje. */
        Node root = new Node(0, null);
        Node pointer = root;

        /* Pętla iterująca po węzłach ze słownika */
        for(Node leaf : nodes) {
            // Pętla iterująca po kolejnych cyfrach kodu
            for(int i = 0; i < leaf.getCode().length(); i++) {
                /* Sprawdzamy kolejne cyfry kodu, zależnie od nich tworzymy prawe lub lewe dziecko.
                 * Jeśli aktualna cyfra jest ostatnią cyfrą kodu - tworzymy liść i wracamy do korzenia
                 * jeśli nie - tworzymy pusty węzeł i przenosimy na niego pointer */
                if(leaf.getCode().charAt(i) == '0') {
                    if(i + 1 == leaf.getCode().length()) {
                        pointer.setLeft(new Node(leaf.getSign(), null));
                        pointer = root;
                        break;
                    }

                    if(pointer.getLeft() == null) {
                        pointer.setLeft(new Node(0, null));
                    }

                    pointer = pointer.getLeft();
                }
                // Działanie analogiczne jak wyżej, tylko tworzymy prawe dziecko
                else if(leaf.getCode().charAt(i) == '1') {
                    if(i + 1 == leaf.getCode().length()) {
                        pointer.setRight(new Node(leaf.getSign(), null));
                        pointer = root;
                        break;
                    }

                    if(pointer.getRight() == null) {
                        pointer.setRight(new Node(0, null));
                    }

                    pointer = pointer.getRight();
                }
            }
        }

        return root;
    }

    /* Funkcja odpowiadająca za dekompresję pliku .cps */
    public void decompress(String inputFilePath) throws IOException {
        /* Wykorzystujemy funkcję tworzącą drzewo Huffmana i pobieramy z niej korzeń. */
        Node root = makeBinaryTree(inputFilePath);
        Node pointer = root;

        String outFilePath;
        if (inputFilePath.endsWith(".cps")) {
            outFilePath = inputFilePath.replace(".cps", ".txt");
        }
        else {
            throw new InvalidFileExtensionException("Nieobsługiwany format pliku.");
        }

        File outFile = new File(outFilePath); // Plik wyjściowy
        FileOutputStream outputStream = new FileOutputStream(outFile);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream); // Strumień pliku wyjściowego

        // Czytamy ponownie skompresowany plik
        inputStream = new FileInputStream(inputFilePath);
        String bin = "";

        // Ignorujemy część słownikową pliku
        inputStream.read(); // Liczba bitów uzupełniających
        inputStream.read(); // Długość słownika
        for(int i = 0; i < dictLength; i++) {
            inputStream.read(); // Znak
            inputStream.read(); // Długość znaku
            for (int j = 0; j < (int) Math.ceil(codeLength / 8.0); j++) {
                inputStream.read(); // Kod znaku
            }
        }

        /* Pętla czyta kolejne bajty pliku, zamienia je na kod binarny,
        * wyczytuje kody i wysyła odpowiadające znaki do strumienia wyjściowego. */
        while((data = inputStream.read()) != -1) {
            bin = intToBinaryString(data);

            // Jeśli to ostatni bajt, musimy zignorować uzupełnione zera.
            if(inputStream.available() == 0) {
                bin = bin.substring(0, bin.length() - extraBits);
            }

            // Chodzimy po drzewie zależnie od wczytanych bitów pliku
            for (int i = 0; i < bin.length(); i++) {
                if (bin.charAt(i) == '0') {
                    pointer = pointer.getLeft();
                }
                else if (bin.charAt(i) == '1') {
                    pointer = pointer.getRight();
                }

                /* Sytuacja może mieć miejsce ponieważ czasami kompresor błędnie tworzy słownik */
                if (pointer == null) {
                    throw new BrokenFileException("Skompresowany plik jest uszkodzony");
                }

                if (pointer.getLeft() == null && pointer.getRight() == null) {
                    // Zapisywanie znaku do pliku
                    dataOutputStream.write(pointer.getSign());
                    pointer = root;
                }
            }
        }

        dataOutputStream.close();
        outputStream.close();

        System.out.println("Poprawnie zdekompresowano");
    }
}
