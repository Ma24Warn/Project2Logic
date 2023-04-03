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
        String finalString; //string to hold final CNF string with all steps applied to it
        int index = 0;
        StringBuilder PLString = new StringBuilder(s);
        StringBuilder substr;

        //create FileWriter object
        FileWriter writer = new FileWriter(f);


        //STEP ONE
        //Remove > (implies =>) and = (biconditional <=>)
        //(A > B) becomes (~A | B)
        //(A = B) becomes ((~A | B) & (~B | A)) 
        //POSSIBLE REGEX FOR THIS PART??????????????????????????????????

        //BICONDITIONAL AND IMPLIES (FOR SINGLE CHARS ON BOTH SIDES ONLY)
        //biconditional (single chars)
        while (index < PLString.length()-1) {
            
            //if biconditional is seen
            if (PLString.charAt(index) == '=') {
                substr = new StringBuilder();
                char prev = PLString.charAt(index-1);
                char next = PLString.charAt(index+1);

                substr.append("(");

                //check if prev has a not
                if(index >= 2 && PLString.charAt(index-2) == '~') {
                    substr.append("~" + prev);
                }
                else {
                    substr.append(prev);
                }

                substr.append(">");

                //check if next has a not
                if (next == '~') {
                    substr.append("~" + PLString.charAt(index+2));
                }
                else {
                    substr.append(next);
                }

                substr.append(")&(");


                //check if next has a not
                if (next == '~') {
                    substr.append("~" + PLString.charAt(index+2));
                }
                else {
                    substr.append(next);
                }

                substr.append(">");

                //check if prev has a not
                if(index >= 2 && PLString.charAt(index-2) == '~') {
                    substr.append("~" + prev);
                }
                else {
                    substr.append(prev);
                }

                substr.append(")");

                PLString.replace(index-1, index+2, substr.toString());

            }
            
            index++;  

            //System.out.println(PLString.toString());
        }

        index = 0; //reset index back to 0 to be reused

        //implies (single chars)
        while (index < PLString.length()-1) {
            
            //if implications is seen
            if (PLString.charAt(index) == '>') {
                substr = new StringBuilder();
                char prev = PLString.charAt(index-1);
                char next = PLString.charAt(index+1);

                substr.append("~");

                //check if prev has a not
                if(index >= 2 && PLString.charAt(index-2) == '~') {
                    substr.append("~" + prev);
                }
                else {
                    substr.append(prev);
                }

                substr.append("|");

                //check if next has a not
                if (next == '~') {
                    substr.append("~" + PLString.charAt(index+2));
                }
                else {
                    substr.append(next);
                }
                
                PLString.replace(index-1, index+2, substr.toString());

            }

            
            index++;  

            //System.out.println(PLString.toString());
        }

        


        


        index = 0;

        //STEP TWO
        //move NOTs inwards. YOULL KNOW YOURE DONE when you go through the sentence and there are no
        //NOTs in front of any parenthesis. Thats all you have to check for. If you see a NOT in front
        //of parens, convert that substring into its converted form.
        //I THINK JUST DISTRIBUTE NOTS AND CANCEL OUT DOUBLE NOTS

        //FOR SINGLE DIGITS WITHIN ONLY?????
        while (index < PLString.length()-1) {
            
            //if a not is seen
            if (PLString.charAt(index) == '~') {
                //if the not is followed by an open parenthesis
                if (PLString.charAt(index+1) == '(') {

                    //get a substring of everything in the parents

                    //https://www.javatpoint.com/StringBuilder-class


                    //flip inner symbol to and or or
                    

                }
                
            }

            
            index++;  

            //System.out.println(PLString.toString());
        }



        //STEP THREE
        //apply distributive and associative properties
        //(~A | (B & ~C)) becomes ((~A | B) & (~A | ~C))
        //maybe just if you see a letter or a NOT then a letter, look at next logic symbol, then
        //if the next thing is a letter, save that letter (or not letter) instead, either way continue
        //until an open paren is seen?????





        //STEP FOUR
        //Any clause containing both A and ~A just drops out. Not sure what this means.








        //NEEDS TO BE EACH CHAR IN THE CLAUSES (NOTS INCLUDED) SEPARATED BY COMMAS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        char curChar;
        //go through final string and delete parens and replace all | with ,
        for (int z = 0; z < PLString.toString().length(); z++) {
            curChar = PLString.charAt(z);

            //get rid of parenthesis
            if (curChar == '(' || curChar == ')') {
                PLString.delete(z, z+1);
            }
            
            //turn | into ,
            else if (curChar == '|') {
                PLString.replace(z, z+1, ",");
            }
        }



        String[] clauses = PLString.toString().split("&");

        for (String cl : clauses) {
            writer.write(cl);
            writer.write("\n");
        }

        writer.close();
        
    }






}
