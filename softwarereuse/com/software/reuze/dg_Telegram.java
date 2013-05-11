package com.software.reuze;
public class dg_Telegram implements Comparable<dg_Telegram> {

	private static final long SAME_TIME_INTERVAL = 250;
	
	//the entity that sent this telegram
	public final int sender;

	//the entity that is to receive this telegram
	public final int receiver;

	//the message itself.
	public final int msg;

	//messages can be dispatched immediately or delayed for a specified amount
	//of time. If a delay is necessary this field is stamped with the time 
	//the message should be dispatched.
	public final long dispatchAt;

	//any additional information that may accompany the message
	public Object[] extraInfo;


	protected dg_Telegram(){
		dispatchAt = -1;
		sender = -1;
		receiver = -1;
		msg = -1;
	}


	public dg_Telegram(long dispatchAt, int sender, int receiver, int msg, Object...  info){ 
		this.dispatchAt = dispatchAt;
		this.sender = sender;
		this.receiver = receiver;
		this.msg = msg;
		if(info != null)
			extraInfo = info;
		else
			extraInfo = new Object[0];
	}

	/**
	 * Telegrams are considered the same if they are to be dispatched within 250ms
	 * as defined in SAME_TIME_INTERVAL
	 */
	public int compareTo(dg_Telegram tgram) {
		if(sender == tgram.sender 
				&& receiver == tgram.receiver 
				&& msg == tgram.msg
				&& Math.abs(dispatchAt - tgram.dispatchAt) < SAME_TIME_INTERVAL){
			return 0;
		}
		return (dispatchAt <= tgram.dispatchAt) ? -1 : 1;
	}

}
