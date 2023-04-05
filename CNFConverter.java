import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

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
        Scanner scan = new Scanner(test5);
        String plSentence = scan.nextLine();
        scan.close();

        //send Propositional Logic sentence to CNF converter method
        cnfConvert(plSentence, output5);

        
    }


    public static void cnfConvert(String s, File f) throws IOException {
        String finalString; //string to hold final CNF string with all steps applied to it
        int index = 0;
        StringBuilder PLString = new StringBuilder(s);
        StringBuilder substr;

        //create FileWriter object
        FileWriter writer = new FileWriter(f);

        System.out.println("ORIGINAL " + PLString.toString());

        //STEP ONE
        //Remove > (implies) and = (biconditional)
        int openParen = 0, closedParen = 0, end = 0, begin = 0;
        String p1 = "", p2 = "";

        //biconditional DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (index < PLString.length()-1) {
            

            //if a biconditional is seen
            if (PLString.charAt(index) == '=') {
                substr = new StringBuilder();

                //if next symbol is a letter
                if (Character.isLetter(PLString.charAt(index+1))) {
                    p1 = String.valueOf(PLString.charAt(index+1));
                    end = index+2;
                }

                //else if next symbol is an open paren, get a substring of everything within it
                else if (PLString.charAt(index+1) == '(') {
                    closedParen = forwardParen(PLString.toString(), index+1);
                    end = closedParen+1;

                    //get substring
                    p1 = PLString.substring(index+1, closedParen+1);

                }


                //if prev symbol is a letter
                if (Character.isLetter(PLString.charAt(index-1))) {
                    p2 = String.valueOf(PLString.charAt(index-1));
                    begin = index-1;
                }

                //else if prev symbol is a closed paren, get a substring of everything within it
                else if (PLString.charAt(index-1) == ')') {
                    openParen = backwardParen(PLString.toString(), index-1);
                    begin = openParen;

                    //get substring
                    p2 = PLString.substring(openParen, index);

                }

                substr.append("(" + p2 + ">" + p1 + ")&(" + p1 + ">" + p2 + ")");


                PLString.replace(begin, end, substr.toString());


            }

            index++;

        }


        System.out.println("STEP1 P1 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused


        //implies DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (index < PLString.length()-1) {
            
            //if implications is seen
            if (PLString.charAt(index) == '>') {
                substr = new StringBuilder();

                //just insert a | in place of the >
                PLString.replace(index, index+1, "|");


                //if the previous thing is a single letter, put a ~ before it
                if (Character.isLetter(PLString.charAt(index-1))) {
                    PLString.insert(index-2, "~");
                }

                //if the previous thing is a closed paren, call the reverseParen method and insert a ~ at the
                //returned index
                else if (PLString.charAt(index-1) == ')') {
                    int pIndex = backwardParen(PLString.toString(), index-1);
                    PLString.insert(pIndex, "~");
                }


            }
            
            index++;  

        }

        System.out.println("STEP1 P2 " + PLString.toString());

        index = 0;









        //STEP TWO
        //move NOTs inwards.
        int nIndex = 0;
        Stack<Character> stack;

        //DeMorgan's Law DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (index < PLString.length()-1) {
            
            //if a not is seen
            if (PLString.charAt(index) == '~') {

                //if the not is followed by an open parenthesis
                if (PLString.charAt(index+1) == '(') {
                    nIndex = index+1;
                    stack = new Stack<Character>();
                    substr = new StringBuilder();


                    //insert a negative before the first element that was distributed to
                    PLString.insert(index+2, "~");


                    //until we have reached the closing paren of the first open paren we see
                    //this is very similar to my forwardParen and backwardParen, except I am actually
                    //modifying the PLString here
                    while (true) {

                        //if you see an open paren, push it to the stack
                        if (PLString.charAt(nIndex) == '(') {
                            stack.push('(');
                        }

                        //else if you see a closed paren, pop from the stack
                        else if (PLString.charAt(nIndex) == ')') {
                            stack.pop();
                        }

                        //else if you see a symbol and the stack size is 1, insert a ~ after it
                        //my reasoning for the stack size needing to be 1 is that if it is 1, then we are not
                        //inside another open paren, which shouldn't get a ~. It would mean we are in the outer-
                        //most part of the original set of parenthesis, if that makes sense.
                        else if (PLString.charAt(nIndex) == '&' && stack.size() == 1) {
                            PLString.replace(nIndex, nIndex+1, "|~");

                            nIndex++;
                        }

                        //same as description above, just needed two so I could easily flip the symbol around
                        else if (PLString.charAt(nIndex) == '|' && stack.size() == 1) {
                            PLString.replace(nIndex, nIndex+1, "&~");
                            nIndex++;
                        }

                        //if the stack is empty, exit the loop
                        if (stack.isEmpty()) {
                            break;
                        }
                        
                        nIndex++;
                    }


                    //delete original ~ before the first open paren
                    PLString.delete(index, index+1);

                }

                //if the not is followed by another NOT??? SHOULD I ACTuALLY COMBINE THEM????????????????????????????
                


            }

            index++;  
        }

        System.out.println("STEP2 P1 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused


        //Remove Double Negatives DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (index < PLString.length()-1) {
            
            //if a NOT is seen
            if (PLString.charAt(index) == '~') {
                //if it is followed by another NOT
                if (PLString.charAt(index+1) == '~') {

                    //remove the double negative
                    PLString.delete(index, index+2);

                }
            }
            
            index++;  

        }

        System.out.println("STEP2 P2 " + PLString.toString());

        index = 0; //reset index back to 0 to be reused



        //STEP THREE
        //apply distributive and associative properties
        char outer, inner = ' ';
        String prev = "", next1 = "", next2 = "", sub;
        int ind1 = 0, ind2 = 0, ind3 = 0; //used to hold the indexes after calling the forwardParen/backwardParen methods
        int st = 0, en = 0; //used to get the start and end values for updating PLString with insert()
        boolean more = true, cnfForm = false;
        String regex = "([a-zA-Z]\\|)([a-zA-Z]\\|)*([a-zA-Z])";
        

        /* TESTING REGEX
        if (Pattern.matches(regex, "A|B|C|D|C|D|(C&D)")) {
            System.out.println("YES!!!");
        }
        else {
            System.out.println("NO!!!");
        }
        */

//WHAT ABOUT WHEN ITS IN CNF FORM???????????? MAYBE YOU ONLY DISTRIBUTE WITH 1 COMBINATION??????
//

 
        //need to make sure new possible distributions are accounted for. Once nothing gets modified in the
        //inner while loop, then there must be no new possible distributions, so we can exit this while loop
        while (more == true) {
            more = false; //if nothing is changed, this will stay false and break out of this outermost while loop

            //Distribute
            while (index < PLString.length()-1) {
                
                //if a | or a & is seen
                if ((PLString.charAt(index) == '|' || PLString.charAt(index) == '&')) {
                    
                    //if it is followed by an open paren (should never be out of bounds)
                    if (PLString.charAt(index+1) == '(') {
                        more = true; //something is going to be changed, so we have to check for new possible distributions
                        outer = PLString.charAt(index); //save what the char seen was (| or &)
                        substr = new StringBuilder();


                        //check what is in the thing we are going to distribute through, if there are no &s and
                        //NO PARENS, it must look like (a|b|c). Move index to the end of this string and continue

                        ind3 = forwardParen(PLString.toString(), index+1);
                        sub = PLString.substring(index+1, ind3+1);
                        System.out.println("THIS IS THE SUBSTRING " + sub);

                        for (int c = 1; c < sub.length()-1; c++) {

                            //if this string contains any & symbols or parenthesis
                            if (sub.charAt(c) == '&' || sub.charAt(c) == '(' || sub.charAt(c) == ')') {
                                //if it does, it must not be in (A|B|C) form
                                cnfForm = false;
                                System.out.println("DOES NOT CONFORM: " + sub.charAt(c));
                                break; //only need to find one thing wrong with it

                                //I ADDED THIS ^ AND IT BROKE AGAIN. IF THERE CANT BE THREE THINGS WITHIN ONE SET
                                //OF PARENS, HOW DO THESE CLAUSES BECOME WHAT WE WANT?????????????????????????
                            }

                            //else it must be in the correct form, so we can skip this distribution
                            else {
                                cnfForm = true;
                                more = false;
                                index = sub.length(); //skip this entire substring in PLString
                                System.out.println("DOES CONFORM");
                            }

                        }











                        if (cnfForm == false) {

                            //if the first element in the parens is a letter
                            if (Character.isLetter(PLString.charAt(index+2))) {
                                ind1 = index+2;
                                next1 = String.valueOf(PLString.charAt(index+2));
                                inner = PLString.charAt(index+3);
                            }

                            //else if the first element in the parens is another open paren
                            else if (PLString.charAt(index+2) == '(') {
                                ind1 = forwardParen(PLString.toString(), index+2);
                                next1 = PLString.substring(index+2, ind1);
                                inner = PLString.charAt(ind1+1);
                            }

                            ind2 = ind1;

                            //I placed this if statement here to hopefully save on memory, even if its a little
                            //if outermost symbol is the same as the inner symbol (both should only be & or | at this point)
                            if (outer == inner) {
                                break; //they should be opposite. Do not distribute.
                            }

                            

                            //if the second element in the parens is a letter
                            if (Character.isLetter(PLString.charAt(ind2+2))) {
                                next2 = String.valueOf(PLString.charAt(ind2+2));
                                en = ind2+4;
                            }

                            //else if the second element in the parens starts with an open paren
                            else if (PLString.charAt(ind2+2) == '(') {
                                ind1 = forwardParen(PLString.toString(), ind2+2);
                                next2 = PLString.substring(ind2+2, ind1);
                                en = ind1+2;
                            }


                            //if the previous element is a letter, save it as a string
                            if (Character.isLetter(PLString.charAt(index-1))) {
                                prev = String.valueOf(PLString.charAt(index-1));
                                st = index-1;
                            }

                            //else if the previous element is a close paren, get a substring of what it contains
                            else if (PLString.charAt(index-1) == ')') {
                                ind1 = backwardParen(PLString.toString(), index-1);
                                prev = PLString.substring(ind1, index);
                                st = ind1;
                            }



                            //concatenate all gather strings into substring and update PLString with it
                            substr.append("(" + prev + outer + next1 + ")" + inner + "(" + prev + outer + next2 + ")");

                            System.out.println("SUBS " + substr.toString());
                            System.out.println("PL " + PLString.toString());
                            System.out.println("st " + st);
                            System.out.println("en " + en);

                            PLString.replace(st, en, substr.toString());

                        }
    
                    }
                }
                
                index++;  
                
            }

            index = 0;
            System.out.println("LOOK HERE " + PLString.toString());
        }

        System.out.println("STEP3    " + PLString.toString());

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




    //this method takes in a string and an integer (the index in the string to start at)
    //I will return the index of the closing parenthesis that matches that open parenthesis
    //at the starting index
    public static int forwardParen(String s, int i) {
        int pIndex = 0;
        Stack<Character> stk = new Stack<Character>(); //stack to keep track of parenthesis pairs



        //go through string starting at the sent index,
        for (int x = i; x < s.length(); x++) {

            //if another open paren is seen, push to the stack. (this also adds the initial open paren)
            if (s.charAt(x) == '(') {
                stk.push('(');
            }

            //if a closing paren is seen, pop from the stack
            else if (s.charAt(x) == ')') {
                stk.pop();
            }


            //if the stack is empty, we have found the end parenthesis index
            if (stk.isEmpty()) {
                pIndex = x;
                break;
            }



        }

        return pIndex;
    }



    //this method takes in a string and an integer (the index in the string to start at)
    //I will return the index of the open parenthesis that matches that closing parenthesis
    //at the starting index
    public static int backwardParen(String s, int i) {
        int pIndex = 0;
        Stack<Character> stk = new Stack<Character>(); //stack to keep track of parenthesis pairs



        //go through string starting at the sent index,
        for (int x = i; x >= 0; x--) {

            //if another closing paren is seen, push to the stack. (this also adds the initial closing paren)
            if (s.charAt(x) == ')') {
                stk.push(')');
            }

            //if an open paren is seen, pop from the stack
            else if (s.charAt(x) == '(') {
                stk.pop();
            }


            //if the stack is empty, we have found the final parenthesis index
            if (stk.isEmpty()) {
                pIndex = x;
                break;
            }



        }

        return pIndex;
    }





}
