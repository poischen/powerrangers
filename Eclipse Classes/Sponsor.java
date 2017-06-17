package powerRangers;

public class Sponsor extends User {

	double amount;
	
	
	public Sponsor(String userName, int userId, String email, double amount) {
		super(userName, userId, email);
		this.amount = amount;

	}
	
	public void donate (double amount){
		
	}

}
