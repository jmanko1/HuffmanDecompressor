public class Main {
    public static void main(String[] args) {
        Compress compress = new Compress();
        try {
            compress.compress("src\\files\\simple.txt");
        } catch(Exception e) {
            System.out.println(e);
        }

        Decompress decompressor = new Decompress();
        try {
            decompressor.decompress("src\\files\\simple.cps");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}