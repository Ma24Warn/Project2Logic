import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class CNFConverter {
    

    public static void main(String args[]) throws IOException {
        //input test cases
        File test1 = new File("TestCases/testcase1.txt");
        File test2 = new File("TestCases/testcase2.txt");
        File test3 = new File("TestCases/testcase3.txt");
        File test4 = new File("TestCases/testcase4.txt");
        File test5 = new File("TestCases/testcase5.txt");

        //output text files
        File output1 = new File("TestCases/testcase1output.txt");
        File output2 = new File("TestCases/testcase2output.txt");
        File output3 = new File("TestCases/testcase3output.txt");
        File output4 = new File("TestCases/testcase4output.txt");
        File output5 = new File("TestCases/testcase5output.txt");

        //read in from file
        Scanner scan = new Scanner(test3);
        String plSentence = scan.nextLine();
        scan.close();

        //send Propositional Logic sentence to CNF converter method
        cnfConvert(plSentence, output3);

        
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

        }

        index = 0; //reset index back to 0 to be reused





        //implies GOOD EXCEPT FOR REVERSE DIRECTION??????????????????????????????????????????????????
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

        }

        


        


        index = 0;

        //STEP TWO
        //move NOTs inwards.

        //YOU ARE NOT MODIFIYING INSIDE PARENS UNLIKE BICONDITIONAL. SO JUST PUT A NOT BEFORE THE OPEN
        //PAREN ON EACH SIDE OF SYMBOL????????????????????????????????????????????????????????????????


        //DeMorgan's Law FOR SINGLE DIGITS WITHIN PARENS
        while (index < PLString.length()-1) {
            
            //if a not is seen
            if (PLString.charAt(index) == '~') {

                //if the not is followed by an open parenthesis
                if (PLString.charAt(index+1) == '(') {
                    substr = new StringBuilder();

                    //get a substring of everything in the parents
                    substr.append("(");
                    
                    substr.append("~" + PLString.charAt(index+2));

                    //flip inner symbol
                    if (PLString.charAt(index+3) == '|') {
                        substr.append("&");
                    }

                    else {
                        substr.append("|");
                    }


                    substr.append("~" + PLString.charAt(index+4));

                    substr.append(")");
                    

                    PLString.replace(index, index+6, substr.toString());

                }
                
            }

            
            index++;  

        }

        index = 0; //reset index back to 0 to be reused


        //REMOVE DOUBLE NEGATIVES (WORKS FOR ALL SCENARIOS)
        while (index < PLString.length()-1) {
            
            //if a NOT is seen
            if (PLString.charAt(index) == '~') {
                //if it is followed by another NOT (should never be out of bounds)
                if (PLString.charAt(index+1) == '~') {

                    //remove the double negative
                    PLString.delete(index, index+2);

                }
            }
            
            index++;  

        }

        index = 0; //reset index back to 0 to be reused



        //STEP THREE
        //apply distributive and associative properties
        char cur;

        //THIS NEEDS TO CHECK THAT THE INNER MOST SYMBOL IS OPPOSITE AND ONLY THAT!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //THIS ALSO DOESNT ACCOUNT FOR NEW DISTRIBUTIONS MADE WHICH NEED TO BE DISTRIBUTED
        //SO (A&B)|(A&C) should be distributed again

        //distributive (With only 3 single characters present)
        while (index < PLString.length()-1) {
            
            //if a | or a & is seen
            if ((PLString.charAt(index) == '|' || PLString.charAt(index) == '&') && Character.isLetter(PLString.charAt(index-1))) {
                //if it is followed by an open paren (should never be out of bounds)
                if (PLString.charAt(index+1) == '(') {
                    cur = PLString.charAt(index); //save what the char seen was (| or &)
                    substr = new StringBuilder();

                    substr.append("(");

                    substr.append(PLString.charAt(index-1));

                    substr.append(cur);

                    substr.append(PLString.charAt(index+2));

                    substr.append(")");

                    //append opposite symbol of cur
                    if (cur == '|') {
                        substr.append("&");
                    }
                    else if (cur == '&') {
                        substr.append("|");
                    }


                    substr.append("(");

                    substr.append(PLString.charAt(index-1));

                    substr.append(cur);

                    substr.append(PLString.charAt(index+4));
                    
                    substr.append(")");



                    PLString.replace(index-1, index+6, substr.toString());
                    System.out.println(PLString.toString());
                }
            }
            
            index++;  

        }

        index = 0; //reset index back to 0 to be reused




        //STEP FOUR
        //Any clause containing both A and ~A just drops out. Not sure what this means.








        //Sort and print out clauses
        char curChar;
        //go through final string and delete parens and replace all | with ,
        for (int z = 0; z < PLString.toString().length(); z++) {
            curChar = PLString.charAt(z);

            //get rid of parenthesis
            if (curChar == '(' || curChar == ')') {
                PLString.delete(z, z+1);
            }
        }


        //String array to get each clause which are separated by the & symbol
        String[] clauses = PLString.toString().split("&");

        for (String str : clauses) {

            //create String ArrayLists to hold the regular letters and letters with a ~
            ArrayList<String> pos = new ArrayList<String>();
            ArrayList<String> neg = new ArrayList<String>();

            for (int x = 0; x < str.length(); x++) {

                //if the character is negative
                if (str.charAt(x) == '~') {
                    neg.add("~" + str.charAt(x+1));
                }

                //if the first character is letter(prevents out of bounds for the next else if)
                else if (x == 0 && Character.isLetter(str.charAt(x))) {
                    pos.add(Character.toString(str.charAt(x)));
                }

                //else if the character is a letter which is not preceded by a NOT
                else if (x != 0 && Character.isLetter(str.charAt(x)) && str.charAt(x-1) != '~') {
                    pos.add(Character.toString(str.charAt(x)));
                }



            }

            //sort the ArrayLists
            Collections.sort(pos);
            Collections.sort(neg);

            //concatenate the ArrayLists, with the NOT letters coming last
            pos.addAll(neg);
      
            //write to output file (replace the square brackets with nothing and remove white space)
            writer.write(pos.toString().replace("[", "").replace("]", "").replace(" ", ""));
            writer.write("\n");
        }



        /* 
        String[] clauses = PLString.toString().split("&");
        String[] clFinal = new String[clauses.length];


        for (int x = 0; x < clauses.length; x++) {
            char cArray[] = clauses[x].toCharArray();
            Arrays.sort(cArray);
            clFinal[x] = cArray.toString();

        }
        

        for (String cl : clFinal) {
            writer.write(cl);
            writer.write("\n");
        }*/

        writer.close();
        
    }






}
