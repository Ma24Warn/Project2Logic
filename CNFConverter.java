import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CNFConverter {
    

    public static void main(String args[]) throws IOException {
        //input test cases
        File test1 = new File("TestCases/testcase1.txt");
        File test2 = new File("TestCases/testcase1.txt");
        File test3 = new File("TestCases/testcase1.txt");
        File test4 = new File("TestCases/testcase1.txt");
        File test5 = new File("TestCases/testcase1.txt");

        //output text files
        File output1 = new File("TestCases/testcase1output.txt");
        File output2 = new File("TestCases/testcase1output.txt");
        File output3 = new File("TestCases/testcase1output.txt");
        File output4 = new File("TestCases/testcase1output.txt");
        File output5 = new File("TestCases/testcase1output.txt");

        //read in from file
        Scanner scan = new Scanner(test1);
        String plSentence = scan.nextLine();
        scan.close();

        //send Propositional Logic sentence to CNF converter method
        cnfConvert(plSentence, output1);

        
    }


    public static void cnfConvert(String s, File f) throws IOException {
        //create FileWriter object
        FileWriter writer = new FileWriter(f);














        //go through and copy each clause into output file. ---------------------------------------------------------
        while (SOMETHING) {
            //store clause (when you see the next & symbol, get substring of everything seen)
            //MAKE SURE you check that it is an & or the END OF THE SENTENCE (HOW THOUGH?????)
            //DO IT LIKE THAT UBBI DUBBI PS THING YOU DID!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!




            
            //change all | symbols into commas with no whitespace between, store back into a string
            //(There will only be | in each clause)

            




            //output that string into the first (or next) line of the outputfile
            //output result to file
            writer.write(SOMESENTENCE);
            writer.write("\n");

            //REPEAT UNTIL END OF SENTENCE
        }



        writer.close();
        
    }






}
