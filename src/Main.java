public class Main {
    public static void main(String[] args) {
        try {
            // Gera arquivos de teste
            FileGenerator generator = new FileGenerator(3, 100, "abcdefghijklmnopqrstuvwxyz".toCharArray(), 3, 8);
            generator.generateFiles();

            // Executa o MapReduce com 3 threads
            MapReduceController controller = new MapReduceController(3);
            controller.execute(new String[]{"file_0.txt", "file_1.txt", "file_2.txt"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
