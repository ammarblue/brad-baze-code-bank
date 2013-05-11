/**
 * @author Petr (http://www.sallyx.org/)
 * 
 * Desc:   A class defining a goldminer. The miner has a FSM defined
 *         by a Lua script.
 */
package reuze.pending;

import com.software.reuze.l_ParseM;

public class demoScriptM extends Entity {
	private static ScriptedStateMachine<demoScriptM> m_pStateMachine;
    private static final String script=
    	"s=a[1].CurrentState(); f=a[0].gi('itired')>2; n=a[0].gs('sname');"+
    	"ifb caseb (s=='State_GoHome'): "+
    	   "ifc casec (a[2]=='Execute'): println('[M]: '+n+' here at the shack. yes siree!') c=a[0].si('igold',0) "+
    	     "ifd cased (f): c=a[1].changeState('State_Sleep') cased (1): c=a[1].changeState('State_GoToMine'); endd; "+
    	   "casec (a[2]=='Exit'): "+
    	   "casec (a[2]=='Enter'): println('[M]: '+n+' is walkin home in the hot n thusty heat of the desert.')"+
    	   "endc;"+
    	"caseb (s=='State_GoToMine'): "+
    	   "ifc casec (a[2]=='Execute'): c=a[0].ai('itired',1); c=a[0].ai('igold',2); g=a[0].gi('igold');"+
  	         "println('[M]: '+n+' has got '+g+' nuggets.')"+
  	         "ifd cased (g>4): c=a[1].changeState('State_GoHome'); println('[M]: '+n+' decides to go home with pockets full of nuggets.') endd; "+
  	       "casec (a[2]=='Enter'): println('[M]: '+n+' is at the mine.')"+
  	       "casec (a[2]=='Exit'): println('[M]: '+n+' exits the mine.')"+
  	       "endc;"+
  	    "caseb (s=='State_Sleep'): "+
  	       "ifc casec (a[2]=='Execute'): "+
  	         "ifd cased (f): c=a[0].ai('itired',-1) println('[M]: '+n+' ZZZZZ...') cased (1): c=a[1].changeState('State_GoToMine'); endd; "+
           "casec (a[2]=='Enter'): println('[M]: '+n+' is dozin off.')"+
           "casec (a[2]=='Exit'): println('[M]: '+n+' wakes up mighty refreshed and heads to work!')"+
           "endc;"+
    	" endb;";
	public demoScriptM(String name) {
		super(name);
		m_pStateMachine = new ScriptedStateMachine<demoScriptM>(this, script);
	}

	//this must be implemented
	@Override
	public void Update(float time) {
		m_pStateMachine.Update();
	}

	public static void main(String args[]) {
		//create a miner
		demoScriptM bob = new demoScriptM("bob");
		bob.var("itired");
		bob.var("igold");
		//make sure Bob's CurrentState object is set to a valid state.
		m_pStateMachine.SetCurrentState("State_GoHome");
		//run him through a few update cycles
		for (int i = 0; i < 16; ++i) {
			bob.Update(0);
		}
	}
}