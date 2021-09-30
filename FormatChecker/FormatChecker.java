import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * 
 * @author TrevorSmith
 *
 * FormatChecker evaluates input files submitted from the command line and returns VALID if format is correct and INVALID if format is
 * incorrect. Correct format includes the right dimension specifications and proper value types in 2D array.
 */
public class FormatChecker {


	@SuppressWarnings("resource")
	public static void main(String[] args) {

		if(args.length == 0) {
			System.out.println("Usage: $ java FormatChecker file1 [file2 ... fileN]");
		}
		File[] fileArray = new File[args.length];

		for(int i = 0; i < args.length; i++) { //Creates file array from args array
			fileArray[i] = new File(args[i]);
		}
		Scanner fileScan;
		Scanner rowCounter;
		Scanner dimensionScan;
		Scanner lineScanner;

		for(int i = 0;i < fileArray.length; i++) {

			try {
				fileScan = new Scanner(fileArray[i]);
				rowCounter = new Scanner(fileArray[i]);
				String dimensions = fileScan.nextLine();
				dimensionScan = new Scanner(dimensions);
				int row = Integer.parseInt(dimensionScan.next());
				int col = Integer.parseInt(dimensionScan.next());

				if (dimensionScan.hasNext()) { //checks dimension line for extra values
					throw new DimensionMismatchException("DimensionMismatchException: Dimension line should only have 2 integers");
				}
				double[][] grid = new double[row][col]; //creates grid


				for(int r = 0; r < row; r++) { 

					String line = fileScan.nextLine();
					int colCount = 0;
					lineScanner = new Scanner(line);
					while(lineScanner.hasNext()) { //fills each column in row
						
						int c = 0;
						grid[r][c] = Double.parseDouble(lineScanner.next());
						colCount++;
						c++;
					}

					if (colCount > col) { //Checks each row to see if there are too many columns
						throw new DimensionMismatchException("DimensionMismatchException: More columns than specified.");
					}
				}
				int rowCount = 0;
				while(rowCounter.hasNextLine()) {
					rowCounter.nextLine();
					rowCount++;
				}
				if(rowCount > row+1) { //checks number of rows
					throw new DimensionMismatchException("DimensionMismatchException: More rows than specified.");
				}
				System.out.println(fileArray[i] + "\nVALID\n");
			} catch (FileNotFoundException e) {
				System.out.println(fileArray[i]);
				System.out.println(e.toString() + "\nINVALID\n");
			} catch (InputMismatchException e) {
				System.out.println(fileArray[i]);
				System.out.println(e.toString() + "\nINVALID\n");
			} catch (NumberFormatException e) {
				System.out.println(fileArray[i]);
				System.out.println(e.toString() + "\nINVALID\n");
			} catch (DimensionMismatchException e) {
				System.out.println(fileArray[i]);
				System.out.println(e.getMessage() + "\nINVALID\n");
			}
		}
	}
}
