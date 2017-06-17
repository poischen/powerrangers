package powerRangers;

public class Confirmer extends User {
	
	double fixedReward;
	int caseId;

	public Confirmer(String userName, int userId, String email, double fixedReward, int caseId) {
		super(userName, userId, email);
		this.fixedReward = fixedReward;
		this.caseId = caseId;
		
	}

}
