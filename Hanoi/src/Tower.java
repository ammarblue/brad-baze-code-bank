public class Tower{
    int moves=0;
    int k;
    int P1=1;
    int P2=2;
    int P3=3;
    int p1d=3;
    int p2d=0;
    int p3d=0;
    int h1=0,h2=1,h3=2;
    int t1=0,t2=0,t3=0;
    GUI gui;
    Tower(GUI in){
    	gui=in;
    }
    public void run(int in){
        k=in;
    	hanoi(k,P1,P2,P3);
    }
    private void hanoi(int k,int p1,int p2,int p3){
        if(k==1){
            move(p1,p2);
        }else{
            hanoi(k-1,p1,p3,p2);
            move(p1,p2);
            hanoi(k-1,p3,p2,p1);
        }
    }
    public void move(int p1,int p2){
        moves++;
        if(p1==1&&p2==2){
        	p1d--;
        	p2d++;
        }else if(p1==2&&p2==1){
        	p2d--;
        	p1d++;
        }else if(p1==3&&p2==1){
        	p3d--;
        	p1d++;
        }else if(p1==1&&p2==3){
        	p1d--;
        	p3d++;
        }else if(p1==3&&p2==2){
        	p3d--;
        	p2d++;
        }else if(p1==2&&p2==3){
        	p2d--;
        	p3d++;
        }
        gui.T.repaint();
        System.out.println(moves+" "+p1+" "+p2);
        System.out.println("["+p1d+"]"+"__"+"["+p2d+"]"+"__"+"["+p3d+"]");
    }
}