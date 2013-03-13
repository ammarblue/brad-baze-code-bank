
public class GAME {
	GUI gui;
	GAME(GUI in){
		gui=in;
	}
	public int Comp(){
		gui.A2.STATE=(int)(Math.random()*3);
		gui.A2.repaint();
		gui.A1.repaint();
		if(gui.A1.STATE==gui.A2.STATE){
			return 0;
		}else if(gui.A1.STATE==0&&gui.A2.STATE==2||gui.A1.STATE==1&&gui.A2.STATE==0||gui.A1.STATE==2&&gui.A2.STATE==1){
			return 1;
		}else{
			return 2;
		}
	}
}
