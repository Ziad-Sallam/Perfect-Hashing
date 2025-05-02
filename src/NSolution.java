import java.util.ArrayList;

public class NSolution {
    private int inputSize;
    private int prevRebuilds = 0;
    private final double loadFactor = 0.7;
    int numberOfElements = 0;
    private final int u = 63 ;
    private final UniversalHashing hasher = new UniversalHashing();
    private int[][] universalHashingMatrix;
    private ArrayList<String>[] firstlevel;
    private N2Solution[] secondlevel;
    private ArrayList<String> elements = new ArrayList<>(); // To keep track of elements
    public NSolution(){
        this.inputSize =64;
        sethash();
    }
    public NSolution(int inputSize){
        this.inputSize = inputSize;
        sethash();
    }
    public  void sethash(){
        firstlevel = new ArrayList[inputSize*8];
        secondlevel = new N2Solution[inputSize *8];
        universalHashingMatrix = new int[(int) Math.floor(Math.log10(inputSize*8)/Math.log10(2))][63];
        randomizeMatrix();
    }
    private void randomizeMatrix() {
        int row = universalHashingMatrix.length;
        int col = universalHashingMatrix[0].length;
        universalHashingMatrix = UniversalHashing.getRandomMatrix(row, col);
    }
    private int firstlevelHash(String item){
        long key = UniversalHashing.convertStringToLong(item);
        int[] binary = UniversalHashing.decimalToBinary(key, u);
        int[] result = UniversalHashing.matrixmultiplication(universalHashingMatrix, binary);
        return  UniversalHashing.binaryToDecimal(result);
    }
    private void secondlevelHash(int index,String item,int size){
        N2Solution n2Solution = new N2Solution(size);
        if (firstlevel[index] == null){
            return;
        }
        if(firstlevel[index].contains(item)){
            return;
        }
        firstlevel[index].add(item);
        for(String entry: firstlevel[index]){
            n2Solution.insert(entry);
        }
        secondlevel[index] = n2Solution;
    }
    
    public boolean insert(String item){
        int index = firstlevelHash(item);
        if (firstlevel[index] == null || firstlevel[index].isEmpty()){
            firstlevel[index] = new ArrayList<>();
            firstlevel[index].add(item);
            numberOfElements++;
            elements.add(item);
            return true;
        }
        int size = firstlevel[index].size();
        if(elements.contains(item)){
            return false;
        }
        elements.add(item);
        numberOfElements++;
        secondlevelHash(index, item, size);  
        return true;
    }
    public boolean delete(String item){
        int index = firstlevelHash(item);
        if (firstlevel[index] == null || !firstlevel[index].contains(item)){
            return false;
        }
        if(secondlevel[index]!=null) {
            firstlevel[index].remove(item);
            if(secondlevel[index].delete(item)){
                numberOfElements--;
                elements.remove(item);
                return true;
        }
        return false;
    }
    firstlevel[index].remove(item);
    numberOfElements--;
    elements.remove(item);
    return true;
}
    public boolean search(String item){
        int index = firstlevelHash(item);
        if (firstlevel[index] == null || !firstlevel[index].contains(item)){
            return false;
        }
        if(secondlevel[index]!=null) {
            return secondlevel[index].search(item);
        }
        return true;
    }
      // Print the first-level table
      public void printFirstLevelTable() {
        System.out.println("\nFirst Level Table:");
        for (int i = 0; i < firstlevel.length; i++) {
            System.out.print("Index = " + i);
            if (firstlevel[i] != null) {
                if (!firstlevel[i].isEmpty())
                    System.out.print("  → elements = ");
                int s = 0;
                for (String key : firstlevel[i]) {
                    s++;
                    System.out.print(key + " ");
                    if (s != firstlevel[i].size())
                        System.out.print(", ");
                }
            }
            System.out.println();
        }
    }
    public void printSecondLevelTables() {
        int count = 0;
        for (int i = 0; i < secondlevel.length; i++) {
            if (secondlevel[i] != null) {
                if (count == 0) {
                    System.out.println("\nSecond Level Tables:" );
                    count++;
                }
                System.out.println("Slot " + i + ":");
                secondlevel[i].printHashTable();
            }
        }
    }
    public static void main(String[] args) {
            NSolution nSolution = new NSolution(8); 
        
            // Insert elements
            nSolution.insert("apple");
            nSolution.insert("banana");
            nSolution.insert("cherry");
            nSolution.insert("date");
            nSolution.insert("elderberry");
            nSolution.insert("fig");
            nSolution.insert("grape");
            nSolution.insert("apple"); // duplicate
            nSolution.insert("banana"); // duplicate
            nSolution.insert("honeydew");
        
            // Print hash tables
            nSolution.printFirstLevelTable();
            nSolution.printSecondLevelTables();
        
            // ✅ Test Search
            System.out.println("\n--- Search Tests ---");
            System.out.println("Searching for 'banana': " + nSolution.search("banana")); // true
            System.out.println("Searching for 'kiwi': " + nSolution.search("kiwi"));     // false
        
            // ✅ Test Delete
            System.out.println("\n--- Delete Tests ---");
            System.out.println("Deleting 'banana': " + nSolution.delete("banana")); // true
            System.out.println("Deleting 'kiwi': " + nSolution.delete("kiwi"));     // false
        
            // Verify deletion
            System.out.println("Searching for 'banana' after deletion: " + nSolution.search("banana")); // false
        
            // Print updated hash tables
            nSolution.printFirstLevelTable();
            nSolution.printSecondLevelTables();
        }
        


}