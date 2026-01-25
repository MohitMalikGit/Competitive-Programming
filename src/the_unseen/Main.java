package the_unseen;

public class Main {
	public static void main(String[] args) {
		Player p = new FootBallPlayer(1 , "Mohit");
		p.play();	
		FootBallPlayer q = (FootBallPlayer)p;
		q.playFootBall();
	} 
}
