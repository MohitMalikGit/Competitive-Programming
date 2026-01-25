package the_unseen;

public class Player {
	private int memberId;
	public void play() {
		System.out.println("player is playing");
	}
	public Player(int memberId) {
		super();
		this.memberId = memberId;
	}
	public int getMemberId() {
		return memberId;
	}
	@Override
	public String toString() {
		return "Player [memberId=" + memberId + ", getMemberId()=" + getMemberId() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	public Player() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
}
