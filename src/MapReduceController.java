import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MapReduceController {
    private final ExecutorService executor;
    private final List<Map<String, List<String>>> intermediateResults;

    public MapReduceController(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.intermediateResults = new ArrayList<>();
    }

    // Função Map que lê um arquivo e conta as palavras
    public Callable<Map<String, List<String>>> mapTask(String fileName) {
        return () -> {
            Map<String, List<String>> wordCounts = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        wordCounts.computeIfAbsent(word, k -> new ArrayList<>()).add("1");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return wordCounts;
        };
    }

    // Coleta resultados de todas as threads após o Map
    public Map<String, List<String>> collectResults(List<Future<Map<String, List<String>>>> futures) throws Exception {
        Map<String, List<String>> aggregatedResults = new HashMap<>();
        for (Future<Map<String, List<String>>> future : futures) {
            Map<String, List<String>> result = future.get();
            for (Map.Entry<String, List<String>> entry : result.entrySet()) {
                aggregatedResults.merge(entry.getKey(), entry.getValue(), (v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                });
            }
        }
        return aggregatedResults;
    }

    // Função Reduce que soma as ocorrências de cada palavra
    public void reduce(String word, List<String> occurrences) {
        System.out.println(word + ": " + occurrences.size());
    }

    // Executa o MapReduce
    public void execute(String[] files) throws Exception {
        List<Future<Map<String, List<String>>>> futures = new ArrayList<>();
        for (String file : files) {
            futures.add(executor.submit(mapTask(file)));
        }

        Map<String, List<String>> finalResults = collectResults(futures);
        executor.shutdown();

        // Processa os resultados na função Reduce
        for (Map.Entry<String, List<String>> entry : finalResults.entrySet()) {
            reduce(entry.getKey(), entry.getValue());
        }
    }
}
