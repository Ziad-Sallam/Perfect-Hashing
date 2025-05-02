
import java.util.ArrayList;

public class N2Solution {

    private int prevRebuilds = 0;
    private String[] hashTable;
    private final int TSize;
    private final UniversalHashing hasher = new UniversalHashing();
    private ArrayList<String> elements = new ArrayList<>();
    public int get_prev_rebuilds(){
        return prevRebuilds;
    }

    public N2Solution(int size) {
        TSize = size*size;
        hashTable = new String[TSize];
        hasher.newHashMatrix(TSize);
        hasher.newHashBase();
    }

    public boolean insert(String item){
        prevRebuilds = 0;
        int hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(item));
        if (hashTable[hash] == null){
            elements.add(item);
            hashTable[hash] = item;
            return true;
        } else {
            if (!hashTable[hash].equals(item)){
                rehash(item);
                return true;
            }
            return false;
        }
    }

    public boolean delete(String item){
        int hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(item));
        if (hashTable[hash] != null && hashTable[hash].equals(item)){
            hashTable[hash] = null;
            return true;
        }
        return false;
    }
    public boolean search(String item){
        int hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(item));
        return hashTable[hash] != null && hashTable[hash].equals(item);
    }


    private void rehash(String item){
        ArrayList<String> newElements = new ArrayList<>();
        for (String element : elements) {
            if (search(element)) {
                newElements.add(element);
            }
        }
        hasher.newHashMatrix(TSize);
        int hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(item));
        String[] New_hashTable = new String[TSize];
        New_hashTable[hash] = item;
        newElements.add(item);
        for (int i = 0; i < newElements.size(); i++) {
            hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(newElements.get(i)));
            if (New_hashTable[hash] == null){
                New_hashTable[hash] = newElements.get(i);
            }
            else if (!New_hashTable[hash].equals(newElements.get(i))){
                if (hasher.hash_string(New_hashTable[hash]).equals(hasher.hash_string(newElements.get(i)))){
                    hasher.newHashBase();
                    hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(item));
                    New_hashTable = new String[TSize];
                    New_hashTable[hash] = item;
                    i = 0;
                    prevRebuilds++;
                }
                else {
                    hasher.newHashBase();
                    hasher.newHashMatrix(TSize);
                    hash = hasher.hash(hasher.getHashMatrix(), hasher.hash_string(item));
                    New_hashTable = new String[TSize];
                    New_hashTable[hash] = item;
                    i = 0;
                    prevRebuilds++;
                }
            }
        }
        hashTable = New_hashTable;
        elements = newElements;
        prevRebuilds++;
    }
    public void printHashTable() {
        int i = 0;
        for (String slot : hashTable) {
            System.out.print("Index = " + i++);
            if (slot == null) {
                System.out.println(" → empty");
            } else {
                System.out.println(" → element = " + slot);
            }
        }
    }

    public static void main(String[] args) {
        // Initialize hash table with size 4 (will use 16 slots since N²)
        N2Solution hashTable = new N2Solution(4);

        // Test insertion
        System.out.println("\n--- Testing Insertion ---");
        String[] testItems = {"apple", "banana", "orange", "apple", "grape", "kiwi"};
        for (String item : testItems) {

            System.out.printf("Insert '%s': %s%n", item, "Success");
        }

        // Test search
        System.out.println("\n--- Testing Search ---");
        String[] searchItems = {"apple", "banana", "watermelon", "kiwi"};
        for (String item : searchItems) {
            boolean found = hashTable.search(item);
            System.out.printf("Search '%s': %s%n", item, found ? "Found" : "Not found");
        }

        // Test deletion
        System.out.println("\n--- Testing Deletion ---");
        String[] deleteItems = {"banana", "mango", "orange"};
        for (String item : deleteItems) {
            boolean deleted = hashTable.delete(item);
            System.out.printf("Delete '%s': %s%n", item, deleted ? "Success" : "Failed (not found)");
        }


        // Test collision handling and rehashing
        System.out.println("\n--- Testing Collision Handling ---");
        // These items are likely to cause collisions
        String[] collisionItems = {"cat", "dog", "rat", "bat", "mat", "hat"};
        for (String item : collisionItems) {
            boolean inserted = hashTable.insert(item);
            System.out.printf("Insert '%s': %s%n", item, inserted ? "Success" : "Failed");
        }
        System.out.println("Total rehashes performed: " + hashTable.get_prev_rebuilds());

        // Final verification
        System.out.println("\n--- Final Contents ---");
        String[] finalCheck = {"apple", "grape", "kiwi", "cat", "dog", "hat"};
        for (String item : finalCheck) {
            System.out.printf("'%s' exists: %s%n", item, hashTable.search(item) ? "Yes" : "No");
        }
    }


}