import java.util.*;

/**
 * Write a description of class BlockchainRunner here.
 *
 * @author (Kristen)
 * @version (2018-11-19)
 */
public class BlockchainRunner
{   
    public static void main(String[] args) {
        System.out.println("Welcome to Blockchain\n_____________________\n");
        panel();
        System.out.println("Bye!");
    }

    /* 
     * Takes in integer that corresponds to a certain action 
     * Runs until user exits the program
     */
    public static void panel() {
        boolean valid = false;
        int input = 0;
        Scanner sc = new Scanner(System.in);
        String one = "- Type 1 to add a block\n";
        String two = "- Type 2 to view the current chain\n";
        String three = "- Type 3 to view a certain block\n";
        String four = "- Type 4 to exit the program\n";
        String total = one + two + three + four;

        Blockchain.fileStatus();
        while (!valid) {
            System.out.println(total);
            try {
                input = sc.nextInt();
                switch (input) {
                    case 1:
                        if(!Blockchain.addBlock()) {
                            valid = true;
                        }
                        break;
                    case 2:
                        Blockchain.printChain();
                        break;
                    case 3:
                        Blockchain.viewBlockInfo();
                        break;
                    case 4: 
                        valid = true;
                        break;
                    default:
                        System.out.print("Please enter a number between 1 and 4.\n\n");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Your input must be a number, it " + 
                "cannot contain characters or symbols");
                panel();
                break;
            }
        }
    }
}
