package compressionManager;

public class Node {
    private final int sign;
    private final String code;
    private Node left;
    private Node right;

    public Node(int sign, String code) {
        this.sign = sign;
        this.code = code;
        this.left = null;
        this.right = null;
    }

    public int getSign() {
        return sign;
    }

    public String getCode() {
        return code;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
