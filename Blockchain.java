import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Exception.*;
import java.text.SimpleDateFormat;

/**
 * Write a description of class Blockchain here.
 *
 * @author (Kristen)
 * @version (2018-11-19)
 */
public class Blockchain
{
    private static ArrayList<BlickBlock> chain = new ArrayList<BlickBlock>();
    private static Scanner sc = new Scanner(System.in);
    private static String fileName = "Blockchain";

    // variables to handle file
    private static File file;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private static FileWriter fw = null;
    private static FileReader fr = null;

    /* --- FILE HANDLING METHODS---*/

    /*
     * Method checks to see if file with a given name exists
     * 
     * If it doesn't, the program creates a file with that name.
     * 
     * If it does, the program takes all of the information in the file,
     * converts it to blocks, and adds the blocks to the chain
     */
    public static void fileStatus() {
        file = new File(fileName +".txt");
        try {
            boolean isFile = (file).createNewFile();
            file.setReadOnly();
            if (isFile) {
                System.out.println("File created\n");
            } else {
                getDataFromFile();
                System.out.println("Data retrieved from file\n");
            }
        } catch (Exception e) {
            System.out.println("Error with file creation?");
        }
    }

    /* 
     * This method reads data from the CSV file and splits it into 
     * pieces that can be stored in blocks
     * 
     * Blocks created from the data on the file are then added to chain
     */
    public static void getDataFromFile() {
        try {
            br = new BufferedReader(new FileReader(file));
            String str = br.readLine();
            while (str != null) {
                String[] data = str.split("\\s*,\\s*");
                try {
                    BlickBlock b = new BlickBlock(data);
                    chain.add(b);
                    str = br.readLine();
                } catch (Exception e) {
                    System.out.println("The file was illegally edited, so the chain will be deleted");
                    file.delete();
                    chain = new ArrayList<BlickBlock>();
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file\n");
        }
    }

    /**
     * @param b: This is the block that's information will be added
     * to the CSV file
     * 
     * This method puts the text file in editing mode and adds the CSV
     * formatted information of a given block b to the file
     * 
     * Once the information is added, the file is set to view only mode
     */
    public static void putInfoOnFile(BlickBlock b) {
        try {
            file.setWritable(true);
            bw = new BufferedWriter(new FileWriter(file,true));
            bw.write(b.toTextFile() + "\n");
            file.setReadOnly();
            bw.close();
            System.out.println("Block successfully added to file\n");
        } catch (IOException e) {
            System.out.print("Error adding block to file.\n\n");
            e.printStackTrace();
        }
    }

    /* --- BLOCK HANDLING METHODS---*/

    /* 
     * Prints out the properly formatted information of a block in 
     * the chain to the console
     */ 
    public static void viewBlockInfo() {
        boolean done = false;
        int index = 0;

        // loops until a valid index is entered
        while (!done) {
            switch (chain.size()) {
                case 0:
                    System.out.println("Sorry, there are no blocks in this chain.\n");
                    done = true;
                    break;
                case 1: 
                    System.out.print(chain.get(index).printBlock() + "\n\n");
                    done = true;
                    break;
                default:
                    System.out.println("Please enter an index between 0 and " + (chain.size() - 1));
                try {
                    index = sc.nextInt();
                    if(index >= 0 && index < chain.size()) {
                        System.out.print(chain.get(index).printBlock() + "\n\n");
                        done = true;
                    } else {
                        System.out.println("That's not a valid index");
                    }
                } catch (Exception e) {
                    System.out.println("You must enter a number\n");
                    break;
                }
            }
        }
    }

    /*
     * If there are no blocks in the chain, method takes in information
     * to store, creates the genesis block, and writes the block's 
     * information to a CSV file
     * 
     * unaltered() is called to make sure all previous blocks were 
     * correct before current block is created
     * 
     * If the chain is all verified, user can submit the block and 
     * to the chain and have it written to the CSV
     */
    public static boolean addBlock() {
        BlickBlock b = null;
        String prevHash, timeStamp, data, hash;

        if (unaltered()) {
            if (chain.size() == 0) {
                System.out.println("Submit the data you want in the genesis block.");
                prevHash = "0";
            } else {
                System.out.println("Submit the data you want in this block.");
                prevHash = chain.get(chain.size() - 1).getHash();
            }
            timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            data = sc.nextLine();
            hash = HashBrown.mine(chain.size() + prevHash + timeStamp + data);
            b = new BlickBlock(chain.size(), prevHash, hash, timeStamp, data);
            chain.add(b);
            putInfoOnFile(b);
            return true;
        } else {
            System.out.println("Hm, something's wrong with the chain, " +
                "we're gonna have to get rid of it\n");
            file.delete();
            chain = new ArrayList<BlickBlock>();
            return false;
        }
    }

    /* --- CHAIN HANDLING METHODS---*/

    /**
     * @returns false if hash of a block doesn't match the prevHash 
     * value stored in the subsequent block, or if it doesn't match 
     * the remined hash of the block's information 
     * 
     * @returns true otherwise
     */
    public static boolean unaltered() {
        BlickBlock prev, curr = null;
        String rehashed = null;

        if (chain.size() > 1) {
            for (int i = 1; i < chain.size(); i++) {
                prev = chain.get(i-1);
                curr = chain.get(i);
                rehashed = HashBrown.mine(prev.getNum() + prev.getPrev() + prev.getTime() + prev.getData());
                if (!prev.getHash().equals(rehashed) || !prev.getHash().equals(curr.getPrev())) {
                    System.out.println("Chain could not be verified");
                    return false;
                } 
            }
        }
        return true;
    }

    /*
     * Prints the length of the chain and the information about each 
     * block in the chain
     */
    public static void printChain() {
        if (chain.size() != 0) {
            System.out.println("\nLength of chain: " + chain.size());
            for (BlickBlock b: chain) {
                System.out.println(b.printBlock());
            }
        } else {
            System.out.println("I'm sorry, there are currently no blocks in this chain.\n");
        }
    }
}
