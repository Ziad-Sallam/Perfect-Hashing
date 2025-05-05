import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NSolution nSolution = null;
        N2Solution n2Solution = null;
        boolean running = true;

        System.out.println("Choose hashing type:");
        System.out.println("1. N² Solution");
        System.out.println("2. N Solution");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice == 1) {
            System.out.print("Enter expected number of elements: ");
            int size = scanner.nextInt();
            scanner.nextLine();
            n2Solution = new N2Solution(size);
            System.out.println("N² hash table created.");
        } else {
            System.out.print("Enter expected number of elements: ");
            int size = scanner.nextInt();
            scanner.nextLine();
            nSolution = new NSolution(size);
            System.out.println("N hash table created.");
        }

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Insert element");
            System.out.println("2. Insert batch");
            System.out.println("3. Delete element");
            System.out.println("4. Delete batch");
            System.out.println("5. Search for element");
            System.out.println("6. Print hash tables");
            System.out.println("7. Batch insert from file");
            System.out.println("8. Batch delete from file");
            System.out.println("9. Exit");
            System.out.print("Choose an operation: ");

            int action = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (action) {
                case 1:
                    System.out.print("Enter the element to insert: ");
                    String insertItem = scanner.nextLine();
                    System.out.println(insert(insertItem, choice, n2Solution, nSolution));
                    break;

                case 2:
                    System.out.print("Enter elements to insert (space-separated): ");
                    String[] insertItems = scanner.nextLine().split("\\s+");
                    for (String item : insertItems) {
                        System.out.println(item + " → " + insert(item, choice, n2Solution, nSolution));
                    }
                    break;

                case 3:
                    System.out.print("Enter the element to delete: ");
                    String deleteItem = scanner.nextLine();
                    System.out.println(delete(deleteItem, choice, n2Solution, nSolution));
                    break;

                case 4:
                    System.out.print("Enter elements to delete (space-separated): ");
                    String[] deleteItems = scanner.nextLine().split("\\s+");
                    for (String item : deleteItems) {
                        System.out.println(item + " → " + delete(item, choice, n2Solution, nSolution));
                    }
                    break;

                case 5:
                    System.out.print("Enter the element to search: ");
                    String searchItem = scanner.nextLine();
                    boolean found = (choice == 1) ? n2Solution.search(searchItem) : nSolution.search(searchItem);
                    System.out.println(found ? "Element found." : "Element not found.");
                    break;

                case 6:
                    if (choice == 1)
                        n2Solution.printHashTable();
                    else {
                        nSolution.printFirstLevelTable();
                        nSolution.printSecondLevelTables();
                    }
                    break;

                case 7:
                    System.out.print("Enter path of the file: ");
                    String insertFilePath = scanner.nextLine();
                    batchInsertFromFile(insertFilePath, choice, n2Solution, nSolution);
                    break;

                case 8:
                    System.out.print("Enter path of the file: ");
                    String deleteFilePath = scanner.nextLine();
                    batchDeleteFromFile(deleteFilePath, choice, n2Solution, nSolution);
                    break;

                case 9:
                running = false;
                System.out.println("Exiting program.");
                break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }

    private static String insert(String item, int choice, N2Solution n2, NSolution n) {
        return (choice == 1)
                ? (n2.insert(item) ? "Inserted." : "Already exists.")
                : (n.insert(item) ? "Inserted." : "Already exists.");
    }

    private static String delete(String item, int choice, N2Solution n2, NSolution n) {
        return (choice == 1)
                ? (n2.delete(item) ? "Deleted." : "Not found.")
                : (n.delete(item) ? "Deleted." : "Not found.");
    }

    private static void batchInsertFromFile(String path, int choice, N2Solution n2, NSolution n) {
        int added = 0, existing = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                boolean result = (choice == 1) ? n2.insert(line) : n.insert(line);
                if (result) added++;
                else existing++;
            }
            System.out.println("Batch insert done.");
            System.out.println("Newly added: " + added);
            System.out.println("Already existing: " + existing);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void batchDeleteFromFile(String path, int choice, N2Solution n2, NSolution n) {
        int deleted = 0, notFound = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                boolean result = (choice == 1) ? n2.delete(line) : n.delete(line);
                if (result) deleted++;
                else notFound++;
            }
            System.out.println("Batch delete done.");
            System.out.println("Deleted: " + deleted);
            System.out.println("Not existing: " + notFound);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
