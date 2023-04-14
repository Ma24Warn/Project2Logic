import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This program takes in a file containing a Proposititional Logic sentence in Conjunctive Normal Form.
 * It will then perform resolution on the clauses until it runs out of clauses to resolve or it produces
 * and empty clause
 * 
 * @author Matthew Warner
 * @version Spring 2023
 * 
 */

public class PLResolution {
    
    public static void main(String args[]) throws IOException {
        ArrayList<ArrayList<String>> clauses = new ArrayList<ArrayList<String>>();
        ArrayList<String> innerList;
        String line; 
        StringBuilder tempString;
        char[] tempArray;

        //input test cases
        File input1 = new File("TestCases/trivial1_CNF.txt");  
        File input2 = new File("TestCases/trivial2_CNF.txt");  
        File input3 = new File("TestCases/easy1_CNF.txt");    
        File input4 = new File("TestCases/easy2_CNF.txt");     
        File input5 = new File("TestCases/easy3_CNF.txt");    
        File input6 = new File("TestCases/medium1_CNF.txt");   
        File input7 = new File("TestCases/medium2_CNF.txt");   
        File input8 = new File("TestCases/medium3_CNF.txt");   
        File input9 = new File("TestCases/hard1_CNF.txt");    
        File input10 = new File("TestCases/hard2_CNF.txt");  
        

        //output text files
        File output1 = new File("TestCases/trivial1_out.txt");  
        File output2 = new File("TestCases/trivial2_out.txt");  
        File output3 = new File("TestCases/easy1_out.txt");     
        File output4 = new File("TestCases/easy2_out.txt");    
        File output5 = new File("TestCases/easy3_out.txt");    
        File output6 = new File("TestCases/medium1_out.txt");  
        File output7 = new File("TestCases/medium2_out.txt");   
        File output8 = new File("TestCases/medium3_out.txt");  
        File output9 = new File("TestCases/hard1_out.txt");  
        File output10 = new File("TestCases/hard2_out.txt");  

        //create the FileWriter 
        FileWriter writer = new FileWriter("TestCases/OUTPUTTESTER.txt");

        //read in from file
        Scanner scan = new Scanner(input10);

        //get each line of the file and add its individual characters to their respective String ArrayList and
        //add all of those to another ArrayList (This is the best way I could think of that would make editing
        //the clauses easier since Strings are immutable)
        while (scan.hasNextLine()) {
            innerList = new ArrayList<String>();

            line = scan.nextLine();
            tempArray = line.toCharArray(); //turn each line into a char array

            //go through each character in the char array and add the letters or ~letters into an arraylist
            for (int x = 0; x < tempArray.length; x++) {

                //if its a not, get both the not and next letter
                if (tempArray[x] == '~') {
                    tempString = new StringBuilder();
                    tempString.append(tempArray[x]);
                    tempString.append(tempArray[x+1]);
                    innerList.add(tempString.toString());
                }

                //its a normal letter and is the first letter (prevents out of bounds for next else if)
                else if (x == 0 && Character.isLetter(tempArray[x])) {
                    innerList.add(String.valueOf(tempArray[x]));
                }

                else if (Character.isLetter(tempArray[x]) && tempArray[x-1] != '~') {
                    innerList.add(String.valueOf(tempArray[x]));
                }
                
            }

            //add each arraylist clause into another arraylist
            clauses.add(innerList);

        }
        scan.close();

        
        //call the resolution method
        boolean result = PLRes(clauses);

        
        //if it produced an empty clause
        if (result) {
            writer.write("Contradiction");
        }

        //else it ran out of clauses to resolve
        else {
            ArrayList<String> finalClauses = new ArrayList<String>();

            //turn each inner arraylist into a string (removing square brackets and whitespace) so that the clauses can be sorted
            for (ArrayList<String> s : clauses) {
                finalClauses.add(s.toString().replace("[", "").replace("]", "").replace(" ", ""));
                
            }

            //sort the clauses by ASCII order
            Collections.sort(finalClauses, Collections.reverseOrder());

            //write the clauses to the output file (already sorted in ASCII order)
            for (String s : finalClauses) {
                writer.write(s + "\n");
            }
            

        }

        writer.close();

    }


    //this method will go through all of the CNF clauses and perform resolution on them until it
    //a. runs out of pairs of clauses to resolve, where it will then return false
    //b. produces an empty clause, where it will then return true
    public static boolean PLRes (ArrayList<ArrayList<String>> c) {
        ArrayList<String> cur1, cur2, sub1, sub2; //used to hold clauses
        ArrayList<ArrayList<String>> resolvents; //holds the clauses created when two clauses are resolved
        String curElem; //holds the current element we are looking for

        //loop until something is returned
        while (true) {

            resolvents = new ArrayList<ArrayList<String>>();

            //for each inner arraylist (clause)
            for (int x = 0; x < c.size(); x++) {
                cur1 = c.get(x); //get the inner arraylist at x in the outer arraylist

                //for every other inner arraylist (clause) to compare to
                for (int y = 0; y < c.size(); y++) {
                    cur2 = c.get(y);


                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //skip checking within the same arraylist SHOULD I DO THIS??????????????????????????????????
                    if (x == y) {
                        break;
                    } //~b,b AND ALSO A b,~b... why arent they resolving????
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


                    //for each element in the first arraylist (clause)
                    for (int z = 0; z < cur1.size(); z++) {

                        curElem = getOpposite(cur1.get(z)); //get the string we are looking for (opposite of current string)


                        //if the second arraylist (clause) contains the element we are looking for
                        if (cur2.contains(curElem)) {

                            //create a copy of the original clause and remove the current element
                            sub1 = new ArrayList<String>(cur1);
                            sub1.remove(z);
                            
                            //create a copy of the other clause and remove the opposite of the current element
                            sub2 = new ArrayList<String>(cur2);
                            sub2.remove(cur2.indexOf(curElem));
                            

                            //append these copies together
                            sub1.addAll(sub2);


                            //it has produced an empty clause, there is a contradiction
                            if (sub1.isEmpty()) {
                                return true;
                            }


                            //add the newly created clause to the resolvents arraylist
                            resolvents.add(sub1);


                            //break becuase there shouldnt be anymore of the current element we are looking for in this specific clause, move to the next element. Just saves on memory
                            break; 



                        }
                    }

                }


            }

            //if the resolvents from the previous two clauses are not a subset of the list of all clauses
            //add the non duplicates into the list of all clauses
            if (!c.containsAll(resolvents)) {

                //add all of the non duplicates
                for (ArrayList<String> a : resolvents) {
                    if (!c.contains(a)) {
                        c.add(a);
                    }
                }

            }

            //else return false
            else {
                return false;
            }


        }


    }


    //this method takes in a string and returns the opposite of it (x into ~x or ~x into x)
    public static String getOpposite(String s) {
        char c = s.charAt(0);

        //if the sent string is a letter with a NOT
        if (c == '~' && s.length() == 2) {
            return String.valueOf(s.charAt(1)); //return the letter without the NOT
        }

        //if the sent string is a single letter
        else if (Character.isLetter(c) && s.length() == 1) {
            return "~"+s.charAt(0);
        }

        else {
            return "INVALID";
        }


    }
 
}
