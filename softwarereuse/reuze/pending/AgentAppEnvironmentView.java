package reuze.pending;
import javax.swing.JComponent;

import com.software.reuze.aa_i_Environment;
import com.software.reuze.aa_i_EnvironmentView;


/**
 * Base class for all graphical environment view implementations.
 * An environment view visualizes agents in their environment.
 * Typically, 2D graphics will be used for visualization. Environment
 * changes are communicated to the viewer by means of an observer pattern.
 * 
 * @author Ruediger Lunde
 */
public abstract class AgentAppEnvironmentView
extends JComponent implements aa_i_EnvironmentView {
	
	private static final long serialVersionUID = 1L;
	/** The environment providing the data to be visualized. */
	protected aa_i_Environment env;
	/**
	 * If the view provides interactive means to modify the environment,
	 * this controller should be responsible to initiate the changes. 
	 */
	private AgentAppController controller;
	/** Message display is delegated to a separate logger. */
	private MessageLogger logger;
	
	/** Sets the data source for the viewer. */
	public void setEnvironment(aa_i_Environment env) {
		if (this.env != null)
			this.env.removeEnvironmentView(this);
		this.env = env;
		env.addEnvironmentView(this);
		repaint();
	}
	
	/** Is called by the agent application frame. */
	protected void setController(AgentAppController controller) {
		this.controller = controller;
	}
	
	/**
	 * Provides a controller which is responsible for all
	 * environment modifications initiated by user interactions.
	 */
	protected AgentAppController getController() {
		return controller;
	}
	
	/** Selects a logger for message display. */
	public void setMessageLogger(MessageLogger logger) {
		this.logger = logger;
	}
	
	/**
	 * Provides a logger which is responsible for message display.
	 */
	protected MessageLogger getLogger() {
		return logger;
	}
	
	/** Forwards a given message to the selected message logger. */
	public void notify(String msg) {
		if (logger != null)
			logger.log(msg);
	}
}
