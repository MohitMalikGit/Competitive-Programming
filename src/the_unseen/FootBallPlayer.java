package the_unseen;

public class FootBallPlayer extends Player{
	private String playerName;
	
	public void playFootBall() {
		System.out.println( playerName + "plays football");
	}

	public String getPlayerName() {
		return playerName;
	}

	public FootBallPlayer(int memberId, String playerName) {
		super(memberId);
		this.playerName = playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		return "FootBallPlayer [playerName=" + playerName + ", getMemberId()=" + getMemberId() +"]";
	}
}
