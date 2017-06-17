package powerRangers;

public class Detective extends User {

	int caseId;
	double rewardPerCase;
	

	public Detective(String userName, int userId, String email, int caseId, double rewardPerCase){
		super(userName, userId, email);
		this.caseId = caseId;
		this.rewardPerCase = rewardPerCase;
	}
	public void reportCase(){
		System.out.println("Detective Task completed");
	}
}
