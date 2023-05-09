import java.io.FileInputStream;
import java.io.IOException;
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

//        for(Node node : nodeList) {
//            System.out.println(node.getSign() + ": " + node.getCode());
//        }
        return nodeList;
    }

    public static String intToBinaryString(int a) {
        String binaryString = Integer.toBinaryString(a & 0xFF);
        // uzupełnienie zerami z przodu, aby uzyskać 8 cyfr
        while (binaryString.length() < 8) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    public Node makeBinaryTree(String filePath) throws InvalidFileExtensionException, IOException {
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
        
    }
}
