/**
 * @author Petr (http://www.sallyx.org/)
 * Desc:   A simple scripted state machine class. Inherit from this class and 
 *         create some states in Lua to give your agents FSM functionality
 */
package reuze.pending;

import com.software.reuze.l_ParseM;

public class ScriptedStateMachine<entity_type extends Entity> {

    /**
     * pointer to the agent that owns this instance
     */
    public entity_type m_pOwner;
    public String m_CurrentState="";
    public l_ParseM.ExecutionContext e;
    public String script;
    private l_ParseM.Str ex=new l_ParseM.Str("Execute");
    private l_ParseM.Str ext=new l_ParseM.Str("Exit");
    private l_ParseM.Str en=new l_ParseM.Str("Enter");
	private String m_NewState;
    
    public ScriptedStateMachine(entity_type owner, String script) {
        m_pOwner = owner;
        this.e=new l_ParseM.ExecutionContext(owner, this, ex);
        this.script=script;
    }

    /**
     * use these methods to initialize the FSM
     */
    public void SetCurrentState(String s) {
        m_CurrentState = s;
    }

    /**
     * call this to update the FSM
     */
    public void Update() {
    	m_NewState=null;
    	e.setArg(2, ex);
        e.execute(script);
        System.out.print(e.output());
        if (m_NewState!=null) {
        	e.setArg(2, ext);
            e.execute(script);
            System.out.print(e.output());
            m_CurrentState=m_NewState;
            e.setArg(2, en);
            e.execute(script);
            System.out.print(e.output());
        }
    }

    //setup a new state
    public void changeState(String new_state) {
	//change state to the new state
	m_NewState = new_state;
  }

  /**
   * retrieve the current state
   */
  public String CurrentState() {
        return m_CurrentState;
  }
  @Override
  public String toString() {
	  return m_pOwner+":"+m_CurrentState;
  }
}