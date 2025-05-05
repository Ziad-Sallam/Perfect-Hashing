import java.io.*;
import java.util.*;

public class BatchProcessor {

    // Overloaded for NSolution
    public static int[] batchInsertFromFile(NSolution solution, String fileName) throws IOException {
        return batchInsertGeneric(solution, fileName);
    }

    // Overloaded for N2Solution
    public static int[] batchInsertFromFile(N2Solution solution, String fileName) throws IOException {
        return batchInsertGeneric(solution, fileName);
    }

    // Overloaded for NSolution
    public static int[] batchDeleteFromFile(NSolution solution, String fileName) throws IOException {
        return batchDeleteGeneric(solution, fileName);
    }

    // Overloaded for N2Solution
    public static int[] batchDeleteFromFile(N2Solution solution, String fileName) throws IOException {
        return batchDeleteGeneric(solution, fileName);
    }

    // Generic logic used internally
    private static int[] batchInsertGeneric(Object solution, String fileName) throws IOException {
        int newCount = 0;
        int existingCount = 0;
        List<String> words = readWordsFromFile(fileName);

        for (String word : words) {
            boolean inserted = false;
            if (solution instanceof NSolution) {
                inserted = ((NSolution) solution).insert(word);
            } else if (solution instanceof N2Solution) {
                inserted = ((N2Solution) solution).insert(word);
            }

            if (inserted) newCount++;
            else existingCount++;
        }

        return new int[]{newCount, existingCount};
    }

    private static int[] batchDeleteGeneric(Object solution, String fileName) throws IOException {
        int deletedCount = 0;
        int notFoundCount = 0;
        List<String> words = readWordsFromFile(fileName);

        for (String word : words) {
            boolean deleted = false;
            if (solution instanceof NSolution) {
                deleted = ((NSolution) solution).delete(word);
            } else if (solution instanceof N2Solution) {
                deleted = ((N2Solution) solution).delete(word);
            }

            if (deleted) deletedCount++;
            else notFoundCount++;
        }

        return new int[]{deletedCount, notFoundCount};
    }

    private static List<String> readWordsFromFile(String fileName) throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            words.add(line.trim());
        }

        reader.close();
        return words;
    }
}
