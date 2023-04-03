import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PLResolution {
    


    public static void main(String args[]) throws FileNotFoundException {
        ArrayList<String> clauses = new ArrayList<String>();

        //input test cases
        File test1 = new File("TestCases/restest1.txt");
        File test2 = new File("TestCases/restest2.txt");
        File test3 = new File("TestCases/restest3.txt");


        //output text files
        File output1 = new File("TestCases/restest1output.txt");
        File output2 = new File("TestCases/restest2output.txt");
        File output3 = new File("TestCases/restest3output.txt");


        //read in from file
        
        Scanner scan = new Scanner(test1);


        while (scan.hasNextLine()) {
            clauses.add(scan.nextLine());
        }
        
        scan.close();


        System.out.println(clauses.toString());

        //get each clause from the file and store them into a string array
        

        PLRes(clauses);



    }


    public static void PLRes (ArrayList<String> c) {


    }







    
}
