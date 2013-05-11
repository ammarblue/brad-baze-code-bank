package com.software.reuze;

import com.software.reuze.dg_a_GameState;

public class dg_StateMachine {

	protected dg_a_EntityBase owner;

	protected dg_a_GameState currentState;
	protected dg_a_GameState previousState;
	protected dg_a_GameState globalState;

	/**
	 * @param owner
	 * @param currentState
	 * @param previousState
	 * @param globalState
	 */
	public dg_StateMachine(dg_a_EntityBase owner, dg_a_GameState currentState,
			dg_a_GameState previousState, dg_a_GameState globalState) {
		this.owner = owner;
		this.currentState = currentState;
		this.previousState = previousState;
		this.globalState = globalState;
	}

	//use these methods to initialize the FSM
	public void setCurrentState(dg_a_GameState s){
		currentState = s;
	}

	public void setGlobalState(dg_a_GameState s){
		globalState = s;
	}

	public void setPreviousState(dg_a_GameState s){
		previousState = s;
	}

	//call this to update the FSM
	public void update(double deltaTime, dg_World world)
	{
		//if a global state exists, call its execute method, else do nothing
		if(globalState != null) globalState.execute(owner, deltaTime, world);

		if(owner.getID() == 2) {
			System.out.println(owner.getName() + "  " + currentState.getClass().getSimpleName());
		}
		//same for the current state
		if (currentState != null) currentState.execute(owner, deltaTime, world);
	}

	public boolean onMessage(dg_Telegram msg)
	{
		//first see if the current state is valid and that it can handle
		//the message
		if (currentState != null && currentState.onMessage(owner, msg))
			return true;

		//if not, and if a global state has been implemented, send 
		//the message to the global state
		if (globalState != null && globalState.onMessage(owner, msg))
			return true;

		return false;
	}

	//change to a new state
	public void changeState(dg_a_GameState pNewState)
	{
		if(pNewState != null){
			//keep a record of the previous state
			previousState = currentState;

			//call the exit method of the existing state
			currentState.exit(owner);

			//change state to the new state
			currentState = pNewState;

			//call the entry method of the new state
			currentState.enter(owner);
		}
	}

	//change state back to the previous state
	public void revertToPreviousState()
	{
		changeState(previousState);
	}

	//returns true if the current state's type is equal to the type of the
	//class passed as a parameter. 
	public boolean isInState(aa_i_State st)
	{
		if (currentState.getClass().getSimpleName().equals(st.getClass().getSimpleName()))
			return true;
		else
			return false;
	}

	public dg_a_GameState currentState(){
		return currentState;
	}

	public dg_a_GameState globalState(){
		return globalState;
	}

	public dg_a_GameState previousState(){
		return previousState;
	}

	//only ever used during debugging to grab the name of the current state
	public String getNameOfCurrentState(){
		return currentState.getClass().getSimpleName();
	}



}
