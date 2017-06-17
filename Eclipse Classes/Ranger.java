package powerRangers;

public class Ranger extends User{
	
	int taskId;

	public Ranger(String userName, int userId, String email, int taskId){
		super(userName, userId, email);
		this.taskId = taskId;
	}
	
	
	public void completeTask(){
		System.out.println("Ranger Task completed");
	}
	
}
