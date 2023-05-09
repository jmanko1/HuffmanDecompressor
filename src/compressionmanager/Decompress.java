package compressionmanager;

import java.io.*;
import java.util.*;

public class Decompress {
    private FileInputStream inputStream;
    private int data;
    private int extraBits;
    private int dictLength;
    private int sign;
    private int codeLength;
    private String code;
    private List<Node> nodeList;
    private String bin;

    public Decompress() {

    }

    public List<Node> readDictionary(String filePath) throws InvalidFileExtensionException, IOException {
        if(!filePath.endsWith(".cps"))
            throw new InvalidFileExtensionException("To nie jest plik skompresowany lub nie jest on obsługiwany");

        inputStream = new FileInputStream(filePath);
        extraBits = inputStream.read();
        dictLength = inputStream.read();
        nodeList = new ArrayList<>();

        for(int i = 0; i < dictLength; i++) {
            sign = inputStream.read();
            codeLength = inputStream.read();
            code = "";
            for(int j = 0; j < (int) Math.ceil(codeLength / 8.0); j++) {
                data = inputStream.read();
                code += intToBinaryString(data);
            }

            code = code.substring((int) ((Math.ceil(codeLength / 8.0) * 8) - codeLength), (int) (Math.ceil(codeLength / 8.0) * 8));
            nodeList.add(new Node(sign, code));
        }
        inputStream.close();

        return nodeList;
    }

    protected String intToBinaryString(int a) {
        String binaryString = Integer.toBinaryString(a & 0xFF);
        // uzupełnienie zerami z przodu, aby uzyskać 8 cyfr
        while (binaryString.length() < 8) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    private Node makeBinaryTree(String filePath) throws InvalidFileExtensionException, IOException {
        List<Node> nodes = readDictionary(filePath);

        Node root = new Node(0, null);
        Node pointer = root;

        for(Node leaf : nodes) {
            for(int i = 0; i < leaf.getCode().length(); i++) {
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

    public void decompress(String filePath) throws InvalidFileExtensionException, IOException {
        Node root = makeBinaryTree(filePath);
        Node pointer = root;

        File file = new File("src\\files\\simple_decoded.txt");
        FileOutputStream fos = new FileOutputStream(file);
        DataOutputStream dos = new DataOutputStream(fos);

        inputStream = new FileInputStream(filePath);
        bin = "";

        inputStream.read();
        inputStream.read();
        for(int i = 0; i < dictLength; i++) {
            inputStream.read();
            inputStream.read();
            for (int j = 0; j < (int) Math.ceil(codeLength / 8.0); j++) {
                inputStream.read();
            }
        }

        while((data = inputStream.read()) != -1) {
            bin = intToBinaryString(data);

            if(inputStream.available() == 0) {
                bin = bin.substring(0, bin.length() - extraBits);
            }

            for(int i = 0; i < bin.length(); i++) {
                if(bin.charAt(i) == '0') {
                    pointer = pointer.getLeft();
                }

                if(bin.charAt(i) == '1') {
                    pointer = pointer.getRight();
                }

                if(pointer.getLeft() == null && pointer.getRight() == null) {
                    dos.write(pointer.getSign());
                    pointer = root;
                }
            }
        }

        dos.close();
        fos.close();
    }
}
