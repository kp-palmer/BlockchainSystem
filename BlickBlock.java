import java.util.Scanner;

/**
 * Write a description of class BlickBlock here.
 *
 * @author (Kristen)
 * @version (2018-11-19)
 */
public class BlickBlock
{
    private int num;
    private String prevHash;
    private String hash;
    private String timestamp;
    private String data;

    
    /* --- Constructors ---*/
    
    // constructor for creating blocks
    public BlickBlock(int num, String prevHash, String hash, String timestamp, String data) {
        this.num = num;
        this.prevHash = prevHash;
        this.hash = hash;
        this.timestamp = timestamp;
        this.data = data;
    }

    // constructor for generating blocks from file
    public BlickBlock(String[] allData) {
        num = Integer.parseInt(allData[0]);
        prevHash = allData[1];
        hash = allData[2];
        timestamp = allData[3];
        data = allData[4];
    }

    /* --- Getters ---*/
    public int getNum() {
        return this.num;
    }

    public String getPrev() {
        return this.prevHash;
    }

    public String getHash() {
        return this.hash;
    }
    
    public String getTime() {
        return this.timestamp;
    }

    public String getData() {
        return this.data;
    }

    // formats block info to be written into text file
    public String toTextFile() {
        return num + "," + prevHash + "," + hash + "," + timestamp + "," + data ;
    }

    // formats block info to be written to the console
    public String printBlock() {
        return "\nBlock " + num + " info:\n" + 
        "- Previous Hash: " + prevHash + "\n" +
        "- Current Hash: " + hash + "\n" +
        "- Timestamp: " + timestamp + "\n" +
        "- Data: " + data + "\n";
    }
}
