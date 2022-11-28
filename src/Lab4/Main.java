/* *****************************************************************************
 * Name: Matthew Green
 * Date: 30Nov2022
 * Purpose:  To run the program.  In Main, I get the input and output file names,
 * then run the tests and print the output via the RunSorts class.
 * See README for details about the overall program.
 **************************************************************************** */


package Lab4;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MGArrayList<Integer> input = new MGArrayList<>();

        // Get the input file from the user
        Scanner inputReader = new Scanner(System.in);
        System.out.println("Please enter the input file name");
        String inputFile = inputReader.nextLine();

        // Get the output file from the user
        System.out.println("Please enter the output file name");
        String outputFile = inputReader.nextLine();

        // Read the numbers from the input file into an MGArrayList
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();
            while (line != null) {
                int num = Integer.parseInt(line);
                input.add(num);
                line = reader.readLine();
            }

            // End the process if the input file is empty
            if (input.isEmpty()) {
                System.out.println("Input file is empty.");
            } else {
                // Call RunSorts with the input file, then call print with the output file
                PrintWriter output = new PrintWriter(outputFile);
                RunSorts runSorts = new RunSorts(input);
                runSorts.print(output);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find file");
        } catch (IOException e) {
            System.out.println("Problem with file");
        } catch (NumberFormatException e) {
            System.out.println("File must contain only numbers");
        }

        inputReader.close();
    }
}









