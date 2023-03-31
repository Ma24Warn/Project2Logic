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


        //STEP ONE
        //Remove > (implies =>) and = (biconditional <=>)
        //(A > B) becomes (~A | B)
        //(A = B) becomes ((~A | B) & (~B | A)) 
        //POSSIBLE REGEX FOR THIS PART??????????????????????????????????




        //STEP TWO
        //move NOTs inwards. YOULL KNOW YOURE DONE when you go through the sentence and there are no
        //NOTs in front of any parenthesis. Thats all you have to check for. If you see a NOT in front
        //of parens, convert that substring into its converted form.
        //I THINK JUST DISTRIBUTE NOTS AND CANCEL OUT DOUBLE NOTS





        //STEP THREE
        //apply distributive and associative properties
        //(~A | (B & ~C)) becomes ((~A | B) & (~A | ~C))
        //maybe just if you see a letter or a NOT then a letter, look at next logic symbol, then
        //if the next thing is a letter, save that letter (or not letter) instead, either way continue
        //until an open paren is seen?????





        //STEP FOUR
        //Any clause containing both A and ~A just drops out. Not sure what this means.










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
