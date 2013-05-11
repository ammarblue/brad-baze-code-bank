package reuze.pending;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;

import com.software.reuze.a_Problem;
import com.software.reuze.aa_AgentSearch;
import com.software.reuze.aa_SearchScheduler;
import com.software.reuze.aa_TreeSearch;
import com.software.reuze.aa_TreeSearchSimulatedAnnealing;
import com.software.reuze.aa_a_Environment;
import com.software.reuze.aa_i_Action;
import com.software.reuze.aa_i_ActionsFunction;
import com.software.reuze.aa_i_Agent;
import com.software.reuze.aa_i_Environment;
import com.software.reuze.aa_i_EnvironmentState;
import com.software.reuze.aa_i_Percept;
import com.software.reuze.dag_GraphSearch;
import com.software.reuze.das_SearchProblemBreadthFirst;
import com.software.reuze.das_SearchProblemDepthFirst;
import com.software.reuze.das_SearchProblemDepthLimited;
import com.software.reuze.das_SearchProblemHillClimbing;
import com.software.reuze.das_SearchProblemIterativeDeepening;
import com.software.reuze.das_TreeAstar;
import com.software.reuze.das_i_SearchProblem;
import com.software.reuze.dg_NQueensBoard;
import com.software.reuze.dg_NQueensGoalTest;
import com.software.reuze.ga_XYLocation;

/*import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.environment.nqueens.QueenAction;*/

/**
 * Graphical n-queens game application. It demonstrates the performance of
 * different search algorithms. An incremental problem formulation is supported
 * as well as a complete-state formulation. Additionally, the user can make
 * experiences with manual search.
 * 
 * @author Ruediger Lunde
 */
public class demoNQueens extends SimpleAgentApp {

	/** List of supported search algorithm names. */
	protected static List<String> SEARCH_NAMES = new ArrayList<String>();
	/** List of supported search algorithms. */
	protected static List<das_i_SearchProblem> SEARCH_ALGOS = new ArrayList<das_i_SearchProblem>();

	/** Adds a new item to the list of supported search algorithms. */
	public static void addSearchAlgorithm(String name, das_i_SearchProblem algo) {
		SEARCH_NAMES.add(name);
		SEARCH_ALGOS.add(algo);
	}

	static {
		addSearchAlgorithm("Depth First Search (Graph Search)",
				new das_SearchProblemDepthFirst(new dag_GraphSearch()));
		addSearchAlgorithm("Breadth First Search (Tree Search)",
				new das_SearchProblemBreadthFirst(new aa_TreeSearch()));
		addSearchAlgorithm("Breadth First Search (Graph Search)",
				new das_SearchProblemBreadthFirst(new dag_GraphSearch()));
		addSearchAlgorithm("Depth Limited Search (8)",
				new das_SearchProblemDepthLimited(8));
		addSearchAlgorithm("Iterative Deepening Search",
				new das_SearchProblemIterativeDeepening());
		addSearchAlgorithm("A* search (attacking pair heuristic)",
				new das_TreeAstar(new dag_GraphSearch(),
						new AttackingPairsHeuristic()));
		addSearchAlgorithm("Hill Climbing Search", new das_SearchProblemHillClimbing(
				new AttackingPairsHeuristic()));
		addSearchAlgorithm("Simulated Annealing Search",
				new aa_TreeSearchSimulatedAnnealing(new AttackingPairsHeuristic(),
						new aa_SearchScheduler(20, 0.045, 1000)));
	}

	/** Returns a <code>NQueensView</code> instance. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new NQueensView();
	}

	/** Returns a <code>NQueensFrame</code> instance. */
	@Override
	public AgentAppFrame createFrame() {
		return new NQueensFrame();
	}

	/** Returns a <code>NQueensController</code> instance. */
	@Override
	public AgentAppController createController() {
		return new NQueensController();
	}

	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new demoNQueens().startApplication();
	}

	// ///////////////////////////////////////////////////////////////
	// some inner classes

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	protected static class NQueensFrame extends AgentAppFrame {
		private static final long serialVersionUID = 1L;
		public static String ENV_SEL = "EnvSelection";
		public static String PROBLEM_SEL = "ProblemSelection";
		public static String SEARCH_SEL = "SearchSelection";

		public NQueensFrame() {
			setTitle("N-Queens Application");
			setSelectors(new String[] { ENV_SEL, PROBLEM_SEL, SEARCH_SEL },
					new String[] { "Select Environment",
							"Select Problem Formulation", "Select Search" });
			setSelectorItems(ENV_SEL, new String[] { "4 Queens", "8 Queens",
					"16 Queens", "32 Queens" }, 1);
			setSelectorItems(PROBLEM_SEL, new String[] { "Incremental",
					"Complete-State" }, 0);
			setSelectorItems(SEARCH_SEL, (String[]) SEARCH_NAMES
					.toArray(new String[] {}), 0);
			setEnvView(new NQueensView());
			setSize(800, 600);
		}
	}

	/**
	 * Displays the informations provided by a <code>NQueensEnvironment</code>
	 * on a panel.
	 */
	protected static class NQueensView extends AgentAppEnvironmentView
			implements ActionListener {
		private static final long serialVersionUID = 1L;
		protected JButton[] squareButtons;
		protected int currSize = -1;

		protected NQueensView() {
		}

		@Override
		public void setEnvironment(aa_i_Environment env) {
			super.setEnvironment(env);
			showState();
		}

		/** Agent value null indicates a user initiated action. */
		public void agentActed(aa_i_Agent agent, aa_i_Action action,
				aa_i_EnvironmentState resultingState) {
			showState();
			notify((agent == null ? "User: " : "") + action.toString());
		}

		public void agentAdded(aa_i_Agent agent, aa_i_EnvironmentState resultingState) {
			showState();
		}

		/**
		 * Displays the board state by labeling and coloring the square buttons.
		 */
		protected void showState() {
			dg_NQueensBoard board = ((NQueensEnvironment) env).getBoard();
			if (currSize != board.getSize()) {
				currSize = board.getSize();
				removeAll();
				setLayout(new GridLayout(currSize, currSize));
				squareButtons = new JButton[currSize * currSize];
				for (int i = 0; i < currSize * currSize; i++) {
					JButton square = new JButton("");
					square.setMargin(new Insets(0, 0, 0, 0));
					square
							.setBackground((i % currSize) % 2 == (i / currSize) % 2 ? Color.WHITE
									: Color.LIGHT_GRAY);
					square.addActionListener(this);
					squareButtons[i] = square;
					add(square);
				}
			}
			for (int i = 0; i < currSize * currSize; i++)
				squareButtons[i].setText("");
			Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN, Math.min(
					getWidth(), getHeight())
					* 3 / 4 / currSize);
			for (ga_XYLocation loc : board.getQueenPositions()) {
				JButton square = squareButtons[loc.getXCoOrdinate()
						+ loc.getYCoOrdinate() * currSize];
				square.setForeground(board.isSquareUnderAttack(loc) ? Color.RED
						: Color.BLACK);
				square.setFont(f);
				square.setText("Q");
			}
			validate();
		}

		/**
		 * When the user presses square buttons the board state is modified
		 * accordingly.
		 */
		public void actionPerformed(ActionEvent ae) {
			for (int i = 0; i < currSize * currSize; i++) {
				if (ae.getSource() == squareButtons[i]) {
					NQueensController contr = (NQueensController) getController();
					ga_XYLocation loc = new ga_XYLocation(i % currSize, i / currSize);
					contr.modifySquare(loc);
				}
			}
		}
	}

	/**
	 * Defines how to react on standard simulation button events.
	 */
	protected static class NQueensController extends AgentAppController {

		protected NQueensEnvironment env = null;
		protected aa_AgentSearch agent = null;
		protected boolean boardDirty;

		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}

		/**
		 * Creates an n-queens environment and clears the current search agent.
		 */
		@Override
		public void prepare(String changedSelector) {
			AgentAppFrame.SelectionState selState = frame.getSelection();
			dg_NQueensBoard board = null;
			switch (selState.getValue(NQueensFrame.ENV_SEL)) {
			case 0: // 4 x 4 board
				board = new dg_NQueensBoard(4);
				break;
			case 1: // 8 x 8 board
				board = new dg_NQueensBoard(8);
				break;
			case 2: // 8 x 8 board
				board = new dg_NQueensBoard(16);
				break;
			case 3: // 32 x 32 board
				board = new dg_NQueensBoard(32);
				break;
			}
			env = new NQueensEnvironment(board);
			if (selState.getValue(NQueensFrame.PROBLEM_SEL) == 1)
				for (int i = 0; i < board.getSize(); i++)
					board.addQueenAt(new ga_XYLocation(i, 0));
			boardDirty = false;
			agent = null;
			frame.getEnvView().setEnvironment(env);
		}

		/**
		 * Creates a new search agent and adds it to the current environment if
		 * necessary.
		 */
		protected void addAgent() throws Exception {
			if (agent != null && agent.isDone()) {
				env.removeAgent(agent);
				agent = null;
			}
			if (agent == null) {
				int pSel = frame.getSelection().getValue(
						NQueensFrame.PROBLEM_SEL);
				int sSel = frame.getSelection().getValue(
						NQueensFrame.SEARCH_SEL);
				aa_i_ActionsFunction af;
				if (pSel == 0)
					af = dg_NQueensFunctionFactory.getIActionsFunction();
				else
					af = dg_NQueensFunctionFactory.getCActionsFunction();
				a_Problem problem = new a_Problem(env.getBoard(), af,
						dg_NQueensFunctionFactory.getResultFunction(),
						new dg_NQueensGoalTest());
				das_i_SearchProblem search = SEARCH_ALGOS.get(sSel);
				agent = new aa_AgentSearch(problem, search);
				env.addAgent(agent);
			}
		}

		/** Checks whether simulation can be started. */
		@Override
		public boolean isPrepared() {
			int problemSel = frame.getSelection().getValue(
					NQueensFrame.PROBLEM_SEL);
			return problemSel == 1
					|| (agent == null || !agent.isDone())
					&& (!boardDirty || env.getBoard()
							.getNumberOfQueensOnBoard() == 0);
		}

		/** Starts simulation. */
		@Override
		public void run(MessageLogger logger) {
			logger.log("<simulation-log>");
			try {
				addAgent();
				while (!agent.isDone() && !frame.simulationPaused()) {
					Thread.sleep(200);
					env.step();
				}
			} catch (InterruptedException e) {
				// nothing to do...
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
			logger.log(getStatistics());
			logger.log("</simulation-log>\n");
		}

		/** Executes one simulation step. */
		@Override
		public void step(MessageLogger logger) {
			try {
				addAgent();
				env.step();
			} catch (Exception e) {
				e.printStackTrace(); // probably search has failed...
			}
		}

		/** Updates the status of the frame after simulation has finished. */
		public void update(SimulationThread simulationThread) {
			if (simulationThread.isCanceled()) {
				frame.setStatus("Task canceled.");
			} else if (frame.simulationPaused()) {
				frame.setStatus("Task paused.");
			} else {
				frame.setStatus("Task completed.");
			}
		}

		/** Provides a text with statistical information about the last run. */
		private String getStatistics() {
			StringBuffer result = new StringBuffer();
			Properties properties = agent.getInstrumentation();
			Iterator<Object> keys = properties.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String property = properties.getProperty(key);
				result.append("\n" + key + " : " + property);
			}
			return result.toString();
		}

		public void modifySquare(ga_XYLocation loc) {
			boardDirty = true;
			String atype;
			if (env.getBoard().queenExistsAt(loc))
				atype = QueenAction.REMOVE_QUEEN;
			else
				atype = QueenAction.PLACE_QUEEN;
			env.executeAction(null, new QueenAction(atype, loc));
			agent = null;
			frame.updateEnabledState();
		}
	}

	/** Simple environment maintaining just the current board state. */
	public static class NQueensEnvironment extends aa_a_Environment {
		dg_NQueensBoard board;

		public NQueensEnvironment(dg_NQueensBoard board) {
			this.board = board;
		}

		public dg_NQueensBoard getBoard() {
			return board;
		}

		/**
		 * Executes the provided action and returns null.
		 */
		@Override
		public aa_i_EnvironmentState executeAction(aa_i_Agent agent, aa_i_Action action) {
			if (action instanceof QueenAction) {
				QueenAction act = (QueenAction) action;
				ga_XYLocation loc = new ga_XYLocation(act.getX(), act.getY());
				if (act.getName() == QueenAction.PLACE_QUEEN)
					board.addQueenAt(loc);
				else if (act.getName() == QueenAction.REMOVE_QUEEN)
					board.removeQueenFrom(loc);
				else if (act.getName() == QueenAction.MOVE_QUEEN)
					board.moveQueenTo(loc);
				if (agent == null)
					updateEnvironmentViewsAgentActed(agent, action, null);
			}
			return null;
		}

		/** Returns null. */
		@Override
		public aa_i_EnvironmentState getCurrentState() {
			return null;
		}

		/** Returns null. */
		@Override
		public aa_i_Percept getPerceptSeenBy(aa_i_Agent anAgent) {
			return null;
		}
	}
}
