package com.software.reuze;


public abstract class dg_a_GameState {

	//this will execute when the state is entered
	public abstract void enter(dg_a_EntityBase owner);

	//this is the state's normal update function
	public abstract void execute(dg_a_EntityBase owner, double deltaTime, dg_World world);

	//this will execute when the state is exited. (My word, isn't
	//life full of surprises... ;o))
	public abstract void exit(dg_a_EntityBase owner);

	//this executes if the agent receives a message from the message dispatcher
	public abstract boolean onMessage(dg_a_EntityBase owner, dg_Telegram tgram);

}
