
import java.awt.Dimension;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail
 * @author TrevorSmith
 */
public class CircuitTracer {

	private static String[] savedArgs;
	static JFrame frame;

	/**
	 * Get method to access the orginal args array with the desired index as a parameter
	 * @param index
	 * @return
	 */
	public static String getArgs(int index) {

		return savedArgs[index];
	}

	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		savedArgs = args;
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			new CircuitTracer(args); //create this with args
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		System.out.println("Invalid or missing/extra arguments.\n\n"

						 + "Usage: java CircuitTracer -s/-q -c/-g <filename> \n"
						 + "First argument: -s (stack storage configuration) or -q (queue storage configuration)\n"
						 + "Second argument: -c (console output) or g (GUI output)\n"
						 + "Third argument: <filename> (full name of input file)\n");
	}

	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 */
	public CircuitTracer(String[] args) {

		if(!args[0].equals("-s") && !args[0].equals("-q")) {	//if first argument is invalid
			printUsage();
			System.exit(1);;
		}
		if(!args[1].equals("-c") && !args[1].equals("-g")){		//if second argument is invalid
			printUsage();
			System.exit(1);;
		}

		Storage<TraceState> stateStore;
		ArrayList<TraceState> bestPaths = new ArrayList<TraceState>();
		CircuitBoard board;

		if(args[0].equals("-s")) {	//if using stack storage
			stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
		}
		else {	//if using queue storage
			stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
		}

		try {
			board = new CircuitBoard(args[2]);
		}
		catch(FileNotFoundException e) {
			System.out.println("The file '" + args[2] + "' was not found.");
			printUsage();
			return;
		}
		//Runs the search for best paths
		Point start = board.getStartingPoint();

		if(board.isOpen(start.x, start.y-1)) {	//adding traces to open spaces adjacent to start
			TraceState newTrace = new TraceState(board, start.x, start.y-1);
			stateStore.store(newTrace);
		}
		if(board.isOpen(start.x, start.y+1)) {
			TraceState newTrace = new TraceState(board, start.x, start.y+1);
			stateStore.store(newTrace);
		}
		if(board.isOpen(start.x-1, start.y)) {
			TraceState newTrace = new TraceState(board, start.x-1, start.y);
			stateStore.store(newTrace);
		}
		if(board.isOpen(start.x+1, start.y)) {
			TraceState newTrace = new TraceState(board, start.x+1, start.y);
			stateStore.store(newTrace);
		}

		while(!stateStore.isEmpty()) {
			TraceState currentState = stateStore.retrieve();
			if(currentState.isComplete()) {
				if(bestPaths.isEmpty()) {
					bestPaths.add(currentState);
				}
				else if(currentState.pathLength() == bestPaths.get(0).pathLength()) {
					bestPaths.add(currentState);
				}
				else if(currentState.pathLength() < bestPaths.get(0).pathLength()) {
					bestPaths.clear();
					bestPaths.add(currentState);
				}
			}
			else {	//adds traces at open spaces adjacent to current state's location
				if(currentState.isOpen(currentState.getRow(), currentState.getCol()-1))
				{
					TraceState newTrace = new TraceState(currentState, currentState.getRow(), currentState.getCol()-1);
					stateStore.store(newTrace);
				}
				if(currentState.isOpen(currentState.getRow(), currentState.getCol()+1))
				{
					TraceState newTrace = new TraceState(currentState, currentState.getRow(), currentState.getCol()+1);
					stateStore.store(newTrace);
				}
				if(currentState.isOpen(currentState.getRow()-1, currentState.getCol()))
				{
					TraceState newTrace = new TraceState(currentState, currentState.getRow()-1, currentState.getCol());
					stateStore.store(newTrace);
				}
				if(currentState.isOpen(currentState.getRow()+1, currentState.getCol()))
				{
					TraceState newTrace = new TraceState(currentState, currentState.getRow()+1, currentState.getCol());
					stateStore.store(newTrace);
				}
			}
		}
		//Outputs results to console
		if(!bestPaths.isEmpty()) {
			if(args[1].equals("-c")){
				for(TraceState path: bestPaths)
				{
					System.out.println(path.toString());
				}
			}
			else {
				frame = new JFrame("Circuit Tracer Solutions");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setPreferredSize(new Dimension(800,500));
				frame.add(new CircuitTracerPanel(board, bestPaths));
				frame.pack();
				frame.setVisible(true);	
			}
		}
		else {
			if(args[1].equals("-c")) {
				//System.out.println("No paths available.");
				for(TraceState path: bestPaths)
				{
					System.out.println(path.toString());
				}
			}
			else {
				frame = new JFrame("Circuit Tracer Solutions");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setPreferredSize(new Dimension(800,500));
				frame.add(new CircuitTracerPanel(board, bestPaths));
				frame.pack();
				frame.setVisible(true);	
				JOptionPane.showMessageDialog(new JFrame(), "No solutions available for this board.");
			}
		}
	}
} // class CircuitTracer
