
import java.math.BigInteger;
import java.security.MessageDigest;


public class UniversalHashing
{
    private int b = 0 ;
    private int[][] hashMatrix;
    private int tableSize;
    private int hashBase = 31;

    public int[][] getHashMatrix() {return this.hashMatrix;}

    public int getHashBase() {return this.hashBase;}

    private boolean isPrime(int n)
    {
        for(int i = 1 ; i < n ; i++)
        {
            if(n % i == 0 && i != 1) return false;
        }
        return true;
    }

    public void newHashBase() {
        while(true)
        {
            int test = (int) (Math.random() * 256  + 1);
            if(isPrime(test))
            {
                this.hashBase = test;
                break;
            }
        }
    }

    public void newHashMatrix(int tableSize)
    {

        this.tableSize = tableSize;
        this. b = (int) Math.max(1, Math.ceil(Math.log(tableSize) / Math.log(2)));
        int u = 64;
        int[][] hashMatrix = new int[b][u];

        for(int i = 0 ; i < b ; i ++)
        {
            for(int j = 0; j < u; j++)
            {
                hashMatrix[i][j] = (int) (Math.random() * 2);
            }
        }
        this.hashMatrix = hashMatrix;
    }
    public static int[] decimalToBinary(long num,int size) {
        int[] binary = new int[64];
        if (num<0) {
            binary[0] = 1;
            num*=-1;
        }
        int i=size-1;
        while (num != 0) {
            binary[i--] = (int) (num % 2);
            num /= 2;
        }
        return binary;
    }
    public static int binaryToDecimal(int[] binary) {
        int decimal = 0;
        for (int i = 0; i < binary.length; i++) {
            decimal += binary[i] * Math.pow(2, binary.length - 1 - i);
        }
        return decimal;
    }
    public static long convertStringToLong(String input){
        try {
            MessageDigest digest =MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            BigInteger bigInt = new BigInteger(1, hash);
            return bigInt.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static int[] matrixmultiplication(int[][] matrix, int[] vector) {
        int[] result = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int temp = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                temp += matrix[i][j] * vector[j];
            }
            result[i] = temp % 2;
        }
        return result;
    }
    public static int[][] getRandomMatrix(int row, int col){
        int[][] matrix = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matrix[i][j] = (int) (Math.random() * 2);
            }
        }
        return matrix;
    }
    public String hash_string(String str)
    {
        long code = 1;
        for (int i=0; i < str.length(); i++)
        {
            code = code * this.hashBase + str.charAt(i);
        }
        return Long.toBinaryString(code);
    }

    public int hash(int[][] hashMatrix, String binaryString)
    {
        int[] hx = new int[this.b];

        for(int i = 0 ; i < binaryString.length() ; i ++)
        {
            if(binaryString.charAt(i) == '1')
            {
                for(int j = 0 ; j < this.b ; j++)
                {
                    hx[j] = (hx[j] + hashMatrix[j][i]) % 2;
                }
            }
        }

        StringBuilder digest = new StringBuilder();
        for(int i = 0 ; i < this.b ; i ++) digest.append(hx[i]);

        return Integer.parseInt(digest.toString(), 2) % this.tableSize;
    }

    public static void main(String[] args) {
        UniversalHashing uh = new UniversalHashing();
        int tableSize = 8; // Example table size
        uh.newHashMatrix(tableSize);
        uh.newHashBase();

        System.out.println("Testing UniversalHashing:");
        System.out.println("Table size: " + tableSize);
        System.out.println("Hash base (prime): " + uh.getHashBase());
        System.out.println("Hash matrix dimensions: " + uh.getHashMatrix().length + " rows x " + uh.getHashMatrix()[0].length + " columns");

        // Display a portion of the hash matrix
        int[][] matrix = uh.getHashMatrix();
        System.out.println("First row of hash matrix (first 10 elements):");
        for (int j = 0; j < 10; j++) {
            System.out.print(matrix[0][j] + " ");
        }
        System.out.println("\n...");

        // Test various strings
        String[] testStrings = {"hello", "world", "", "test", "universal", "hashing", "repeat"};
        for (String s : testStrings) {
            String binary = uh.hash_string(s);
            int index = uh.hash(matrix, binary);
            System.out.printf("String: %-10s -> Binary: %-20s -> Index: %d%n", "\"" + s + "\"", binary, index);
        }
        // Test consistency for the same string
        String repeatStr = "repeatTest";
        String binary1 = uh.hash_string(repeatStr);
        int index1 = uh.hash(matrix, binary1);
        String binary2 = uh.hash_string(repeatStr);
        int index2 = uh.hash(matrix, binary2);
        System.out.println("Consistency check for '" + repeatStr + "': " + (index1 == index2 ? "Passed" : "Failed"));
    }
}

