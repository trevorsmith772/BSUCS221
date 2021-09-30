import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 * 
 * @author TrevorSmith
 *
 * This class is the GUI component of the CircuitTracer application. CircuitTracerPanel outputs
 * fastes solutions for the input boards and allows user to load in a new board from the application.
 */
@SuppressWarnings("serial")
public class CircuitTracerPanel extends JPanel{

	private JFrame frame = CircuitTracer.frame;
	private ArrayList<TraceState> bestPaths;
	private JPanel boardPanel;
	private JList<String> solutionList;
	private JTextField loadField;

	/**
	 * Constructor for the CircuitTracerPanel class. Creates main panel with all necessary components
	 * @param board
	 * @param bestPaths
	 */
	public CircuitTracerPanel(CircuitBoard board, ArrayList<TraceState> bestPaths) {
		this.bestPaths = bestPaths;
		this.setLayout(new BorderLayout());

		JMenuBar menuBar = createMenu();
		boardPanel = createBoardPanel(board, 0);

		solutionList = createSolutionList(bestPaths);
		solutionList.addListSelectionListener(new SolutionListListener());
		solutionList.setBackground(null);

		JPanel listPanel = createListPanel();

		this.add(menuBar, BorderLayout.NORTH);
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(listPanel, BorderLayout.EAST);
		
	}

	/**
	 * Creates the list panel that contains the list of solutions, the text field, and button 
	 * to load a new board
	 * @return
	 */
	private JPanel createListPanel() {
		JPanel listPanel = new JPanel();
		listPanel.setBorder(BorderFactory.createTitledBorder("Solutions"));
		listPanel.setLayout(new BorderLayout());
		listPanel.add(solutionList, BorderLayout.CENTER);
		
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
		JButton loadButton = new JButton("Load Board");
		loadField = new JTextField(loadButton.getWidth());
		loadButton.addActionListener(new LoadListener());
		loadPanel.add(loadField);
		loadPanel.add(loadButton);
		
		listPanel.add(loadPanel, BorderLayout.SOUTH);
		return listPanel;
	}

	/**
	 * Creates the list of solutions that will be displayed on the right side of the GUI
	 * @param bestPaths
	 * @return
	 */
	private JList<String> createSolutionList(ArrayList<TraceState> bestPaths) {

		String[] solutions = new String[bestPaths.size()];

		for(int i = 0; i < bestPaths.size(); i++) {
			solutions[i] = "Solution " + (i+1) + " ";
		}

		JList<String> list = new JList<String>(solutions);

		return list;
	}

	/**
	 * Creates the menu bar for CircuitTracerPanel
	 * @return
	 */
	private JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenu help = new JMenu("Help");

		JMenuItem quit = new JMenuItem("Quit");
		JMenuItem about = new JMenuItem("About");

		quit.addActionListener(new QuitListener());
		about.addActionListener(new AboutListener());

		file.add(quit);
		help.add(about);

		menuBar.add(file);
		menuBar.add(help);

		return menuBar;
	}

	/**
	 * Creates the board panel which contains the board of the selected solution
	 * @param board
	 * @param index
	 * @return
	 */
	private JPanel createBoardPanel(CircuitBoard board, int index) {
		if(!bestPaths.isEmpty()) {
			board = bestPaths.get(index).getBoard();
		}
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(board.numRows(), board.numCols()));
		for(int r = 0;r < board.numRows() ;r++) {
			for(int c = 0;c < board.numCols();c++) {
				JLabel boardVal = new JLabel(Character.toString(board.charAt(r, c)));
				boardVal.setFont(new Font("sanserif", Font.PLAIN, 30));

				if(board.charAt(r, c) == 'T') {
					boardVal.setForeground(Color.GREEN);
				}
				else if(board.charAt(r, c) == '1' || board.charAt(r, c) == '2') {
					boardVal.setForeground(Color.BLUE);
				}
				else if(board.charAt(r,c) == 'X') {
					boardVal.setForeground(Color.RED);
				}
				else {
					boardVal.setForeground(Color.BLACK);
				}
				boardVal.setHorizontalAlignment(JLabel.CENTER);
				boardVal.setBorder(BorderFactory.createRaisedBevelBorder());
				boardPanel.add(boardVal);
			}
		}
		return boardPanel;
	}

	/**
	 * 
	 * @author TrevorSmith
	 * SolutionListListener implements the ListSelectionListener and is the listener class for 
	 * the solution list displayed on the right side of the application. This class updates the board
	 * panel when a new solution is clicked on.
	 */
	private class SolutionListListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {

			int index = solutionList.getSelectedIndex();
			remove(boardPanel);
			boardPanel = createBoardPanel(bestPaths.get(index).getBoard(), index);
			add(boardPanel);
			revalidate();
		}
	}

	/**
	 * 
	 * @author TrevorSmith
	 * QuitListener is the ActionListener for the quit button in the menu bar.
	 * QuitListener quits the application whenever the quit button is clicked.
	 */
	private class QuitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.exit(1);
		}

	}

	/**
	 * 
	 * @author TrevorSmith
	 * AboutListener is the ActionListener for the about button in the menu bar.
	 * AboutListener displays a message showing the program authors whenever the
	 * about button is clicked.
	 */
	private class AboutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(new JFrame(), "Circuit Tracer written by:\n"
					+ "Mason Vail (masonvail@boisestate.edu)\n"
					+ "Trevor Smith (trevorsmith772@u.boisestate.edu)");
		}
	}
	
	/**
	 * 
	 * @author TrevorSmith
	 * LoadListener is the ActionListener for the 'Load Board' button in the list panel.
	 * LoadListener essentially closes the original Circuit Tracer Solutions frame, then reruns 
	 * CircuitTracer with the original arg[0] that was used along with -g and the filename from the 
	 * loadField as the other arguments.
	 */
	private class LoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String[] newArgs = {CircuitTracer.getArgs(0),"-g", loadField.getText()};
			frame.setVisible(false);
			CircuitTracer newTracer = new CircuitTracer(newArgs);
		}
	}
}
