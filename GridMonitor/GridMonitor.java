import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class works in conjunction with the GridMonitorTester to determine cells of an input 2D array
 *  to be 'true' or 'false' meaning they will explode if true and not explode if false.
 * 
 * @author TrevorSmith
 *
 */
public class GridMonitor implements GridMonitorInterface{

	private int row,col,r,c;
	private double north, south, east, west;
	private double[][] baseGrid, sumGrid, avgGrid, deltaGrid, baseGridCopy, sumGridCopy, avgGridCopy, deltaGridCopy;
	private boolean[][] dangerGrid, dangerGridCopy;
	private Scanner fileScan;
	private File inputFile;

	public GridMonitor(String filename) throws FileNotFoundException {

		//Reads file
		inputFile = new File(filename);
		fileScan = new Scanner(inputFile);

		//Initializing Base Grid with rows and cols from file
		row = fileScan.nextInt();
		col = fileScan.nextInt();
		baseGrid = new double[row][col];

		//Fills Base Grid with doubles from file
		for(r = 0; r < row; r++) {
			for(c = 0; c < col; c++) {
				baseGrid[r][c] = fileScan.nextDouble();
			}
		}

		//Initializing Surrounding Sum Grid
		sumGrid = new double[row][col];

		//Fills sumGrid with surrounding cells values
		for(r = 0; r < row; r++) {
			for(c = 0; c < col; c++) {
				if(r - 1 <0) {					//If statements determines north, south, east, and west
					north = baseGrid[r][c];		// values based on whether they are a border cell or not
				}
				else {
					north = baseGrid[r-1][c];
				}
				if(r + 1 >=row) {
					south = baseGrid[r][c];
				}
				else {
					south = baseGrid[r+1][c];
				}
				if(c - 1 < 0) {
					west = baseGrid[r][c];
				}
				else {
					west = baseGrid[r][c-1];
				}
				if(c+1 >= col) {
					east = baseGrid[r][c];
				}
				else {
					east = baseGrid[r][c+1];
				}
				sumGrid[r][c] = north + south + east + west; //Adds values of surrounding cells and sets sumGrid cells
			}
		}

		//Initializing Surrounding Average Grid
		avgGrid = new double[row][col];

		//Fills avgGrid with sumGrid values divided by 4
		for(r = 0; r < row; r++) {
			for(c = 0; c < col; c++) {
				avgGrid[r][c] = (sumGrid[r][c])/4;
			}
		}

		//Initializing Delta Grid
		deltaGrid = new double[row][col];

		//Fills deltaGrid with avgGrid values divided by 2
		for(r = 0; r < row; r++) {
			for(c = 0; c < col; c++) {
				deltaGrid[r][c] = Math.abs((avgGrid[r][c])/2);
			}
		}

		//Initializing Danger Grid
		dangerGrid = new boolean[row][col];

		//Fills dangerGrid with boolean values
		for(r = 0; r < row; r++) {
			for(c = 0; c < col; c++) {
				if(baseGrid[r][c] < (avgGrid[r][c] - deltaGrid[r][c]) || baseGrid[r][c] > (avgGrid[r][c] + deltaGrid[r][c])) {
					dangerGrid[r][c] = true;
				}
				else {
					dangerGrid[r][c] = false;
				}
			}
		}
	}

	//Creates String for when toString() is called
	public String toString() {
		String s = "";
		for(int r = 0; r < row; r++){
			for(int c = 0; c < col; c++){
				s += dangerGrid[r][c];
				s += " ";
			}
			s += "\n";
		}
		return s;
	}

	//Creates copy of baseGrid and returns copy
	@Override
	public double[][] getBaseGrid() {
		
		baseGridCopy = new double[row][col];
		for(r = 0;r < row;r++) {
			for(c = 0;c < col;c++) {
				baseGridCopy[r][c] = baseGrid[r][c];
			}
		}
		return baseGridCopy;
	}

	//Creates copy of sumGrid and returns copy
	@Override
	public double[][] getSurroundingSumGrid() {

		sumGridCopy = new double[row][col];
		for(r = 0;r < row;r++) {
			for(c = 0;c < col;c++) {
				sumGridCopy[r][c] = sumGrid[r][c];
			}
		}
		return sumGridCopy;
	}

	//Creates copy of avgGrid and returns copy
	@Override
	public double[][] getSurroundingAvgGrid() {

		avgGridCopy = new double[row][col];
		for(r = 0;r < row;r++) {
			for(c = 0;c < col;c++) {
				avgGridCopy[r][c] = avgGrid[r][c];
			}
		}
		
		return avgGridCopy;
	}

	//Creates copy of deltaGrid and returns copy
	@Override
	public double[][] getDeltaGrid() {

		deltaGridCopy = new double[row][col];
		for(r = 0;r < row;r++) {
			for(c = 0;c < col;c++) {
				deltaGridCopy[r][c] = deltaGrid[r][c];
			}
		}
		return deltaGridCopy;
	}

	//Creates copy of dangerGrid and returns copy
	@Override
	public boolean[][] getDangerGrid() {
		
		dangerGridCopy = new boolean[row][col];
		for(r = 0;r < row;r++) {
			for(c = 0;c < col;c++) {
				dangerGridCopy[r][c] = dangerGrid[r][c];
			}
		}
		return dangerGridCopy;
	}

}