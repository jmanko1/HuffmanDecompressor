public class Main {
    public static void main(String[] args) {
        Compress compress = new Compress();
        try {
            compress.compress("src\\files\\LoremIpsum.txt");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}