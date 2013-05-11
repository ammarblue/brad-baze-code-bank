package com.software.reuze;
import java.util.TreeSet;

import com.software.reuze.dg_Telegram;



/**
 * This class is responsible for storing Telegrams
 * @author Peter Lager
 *
 */
public class dg_TelegramDispatcher {

	/**
	 * The set of telegrams still to be processed.
	 */
	private static TreeSet<dg_Telegram> telegrams = new TreeSet<dg_Telegram>();

	public static void dispatch(long delay_ms, int sender, int receiver, int msg, Object ... eInfo){
//		BaseEntity bgeReceiver = World.allEntities.get(receiver);
		// Only remember telegrams for entities that have a state machine
//		if(bgeReceiver != null && bgeReceiver.getStateMachine()!= null){
//			Telegram tgram = new Telegram(System.currentTimeMillis() + delay_ms, sender, receiver, msg, eInfo);
//				telegrams.add(tgram);
//		}
	}

	private static void sendTelegram(int receiver, dg_Telegram tgram) {
//		BaseEntity bgeReceiver = World.allEntities.get(receiver);
//		if(bgeReceiver == null){
//			System.out.println("ERROR unable to send telegram");
//			return;
//		}
//		bgeReceiver.getStateMachine().handleMessage(tgram);
	}

	public static void update(){
		dg_Telegram tgram = null;
		while(!telegrams.isEmpty() && telegrams.first().dispatchAt <= System.currentTimeMillis()){
			tgram = telegrams.pollFirst();
			sendTelegram(tgram.receiver, tgram);
		}
	}

}
