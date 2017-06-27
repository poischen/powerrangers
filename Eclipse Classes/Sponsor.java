
public class Sponsor extends RoleForwarding {

	double amount;

	
	public Sponsor(User user, double amount) {
		this.amount = amount;
		this.user = user;

	}
	
	public void donate (double amount){
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return userName;
	}
	
}
