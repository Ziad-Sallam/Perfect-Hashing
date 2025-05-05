import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.TestWatcher;

import java.io.*;
import java.util.*;

import org.junit.runners.MethodSorters;


import org.junit.runner.Description;
;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerfectHashingTest {

    static NSolution nSolution;
    static N2Solution n2Solution;
    static final StringBuilder resultLog = new StringBuilder();
    static long startTime;

    @BeforeClass
    public static void setupClass() {
        nSolution = new NSolution(64);
        n2Solution = new N2Solution(64);
    }

    @AfterClass
    public static void writeResultsToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("test_results.txt"));
        writer.write(resultLog.toString());
        writer.close();
    }

    // Rule to monitor each test and record time/result
    @Rule
    public TestWatcher watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected void succeeded(Description description) {
            long duration = System.currentTimeMillis() - startTime;
            resultLog
                     .append(description.getMethodName())
                     .append(" PASSED in ")
                     .append(duration)
                     .append(" ms\n");
        }

        @Override
        protected void failed(Throwable e, Description description) {
            long duration = System.currentTimeMillis() - startTime;
            resultLog
                     .append(description.getMethodName())
                     .append(" FAILED in ")
                     .append(duration)
                     .append(" ms - Reason: ")
                     .append(e.getMessage())
                     .append("\n");
        }
    };

    @Test
    public void test01_insertNewElement_N() {
        boolean inserted = nSolution.insert("apple");
        assertTrue(inserted);
    }

    @Test
    public void test02_insertDuplicateElement_N() {
        boolean inserted = nSolution.insert("apple");
        assertFalse(inserted);
    }

    @Test
    public void test03_searchExistingElement_N() {
        boolean found = nSolution.search("apple");
        assertTrue(found);
    }

    @Test
    public void test04_searchNonExistingElement_N() {
        boolean found = nSolution.search("banana");
        assertFalse(found);
    }

    @Test
    public void test05_deleteExistingElement_N() {
        boolean deleted = nSolution.delete("apple");
        assertTrue(deleted);
    }

    @Test
    public void test06_deleteAlreadyDeletedElement_N() {
        boolean deleted = nSolution.delete("apple");
        assertFalse(deleted);
    }

    @Test
    public void test07_batchInsertFromFile_N() throws IOException {
        List<String> words = Arrays.asList("cat", "dog", "fish", "dog", "bird");
        FileWriter writer = new FileWriter("batch_insert_test.txt");
        for (String word : words) {
            writer.write(word + "\n");
        }
        writer.close();

        int[] result = BatchProcessor.batchInsertFromFile(nSolution, "batch_insert_test.txt");
        assertEquals(4, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void test08_batchDeleteFromFile_N() throws IOException {
        List<String> toDelete = Arrays.asList("dog", "lion", "fish");
        FileWriter writer = new FileWriter("batch_delete_test.txt");
        for (String word : toDelete) {
            writer.write(word + "\n");
        }
        writer.close();

        int[] result = BatchProcessor.batchDeleteFromFile(nSolution, "batch_delete_test.txt");
        assertEquals(2, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void test09_insertEmptyString_N() {
        boolean inserted = nSolution.insert("");
        assertTrue(inserted);
    }

    @Test
    public void test10_searchEmptyString_N() {
        boolean found = nSolution.search("");
        assertTrue(found);
    }

    @Test
    public void test11_deleteEmptyString_N() {
        boolean deleted = nSolution.delete("");
        assertTrue(deleted);
    }

    @Test
    public void test12_insertSymbols_N() {
        boolean inserted = nSolution.insert("@!#%$");
        assertTrue(inserted);
    }

    @Test
    public void test13_searchSymbols_N() {
        boolean found = nSolution.search("@!#%$");
        assertTrue(found);
    }

    @Test
    public void test14_deleteSymbols_N() {
        boolean deleted = nSolution.delete("@!#%$");
        assertTrue(deleted);
    }

    @Test
    public void test15_largeBatchInsert_N() throws IOException {
        FileWriter writer = new FileWriter("large_batch.txt");
        for (int i = 0; i < 100; i++) {
            writer.write("item" + i + "\n");
        }
        writer.write("item1\n");  // duplicate
        writer.close();

        int[] result = BatchProcessor.batchInsertFromFile(nSolution, "large_batch.txt");
        assertEquals(100, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void test16_insertNewElement_N2() {
        boolean inserted = n2Solution.insert("apple");
        assertTrue(inserted);
    }

    @Test
    public void test17_insertDuplicateElement_N2() {
        boolean inserted = n2Solution.insert("apple");
        assertFalse(inserted);
    }

    @Test
    public void test18_searchExistingElement_N2() {
        boolean found = n2Solution.search("apple");
        assertTrue(found);
    }

    @Test
    public void test19_searchNonExistingElement_N2() {
        boolean found = n2Solution.search("banana");
        assertFalse(found);
    }

    @Test
    public void test20_deleteExistingElement_N2() {
        boolean deleted = n2Solution.delete("apple");
        assertTrue(deleted);
    }

    @Test
    public void test21_deleteAlreadyDeletedElement_N2() {
        boolean deleted = n2Solution.delete("apple");
        assertFalse(deleted);
    }
    @Test
    public void test22_batchInsertFromFile_N2() throws IOException {
        List<String> words = Arrays.asList("cat", "dog", "fish", "dog", "bird");
        FileWriter writer = new FileWriter("batch_insert_test_n2.txt");
        for (String word : words) {
            writer.write(word + "\n");
        }
        writer.close();

        int[] result = BatchProcessor.batchInsertFromFile(n2Solution, "batch_insert_test_n2.txt");
        assertEquals(4, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void test23_batchDeleteFromFile_N2() throws IOException {
        List<String> toDelete = Arrays.asList("dog", "lion", "fish");
        FileWriter writer = new FileWriter("batch_delete_test_n2.txt");
        for (String word : toDelete) {
            writer.write(word + "\n");
        }
        writer.close();

        int[] result = BatchProcessor.batchDeleteFromFile(n2Solution, "batch_delete_test_n2.txt");
        assertEquals(2, result[0]);
        assertEquals(1, result[1]);
    }

    @Test
    public void test24_insertEmptyString_N2() {
        boolean inserted = n2Solution.insert("");
        assertTrue(inserted);
    }

    @Test
    public void test25_searchEmptyString_N2() {
        boolean found = n2Solution.search("");
        assertTrue(found);
    }

    @Test
    public void test26_deleteEmptyString_N2() {
        boolean deleted = n2Solution.delete("");
        assertTrue(deleted);
    }

    @Test
    public void test27_insertSymbols_N2() {
        boolean inserted = n2Solution.insert("@!#%$");
        assertTrue(inserted);
    }

    @Test
    public void test28_searchSymbols_N2() {
        boolean found = n2Solution.search("@!#%$");
        assertTrue(found);
    }

    @Test
    public void test29_deleteSymbols_N2() {
        boolean deleted = n2Solution.delete("@!#%$");
        assertTrue(deleted);
    }

    @Test
    public void test30_largeBatchInsert_N2() throws IOException {
        FileWriter writer = new FileWriter("large_batch_n2.txt");
        for (int i = 0; i < 100; i++) {
            writer.write("item" + i + "\n");
        }
        writer.write("item1\n");  // duplicate
        writer.close();

        int[] result = BatchProcessor.batchInsertFromFile(n2Solution, "large_batch_n2.txt");
        assertEquals(100, result[0]);
        assertEquals(1, result[1]);
    }
}
