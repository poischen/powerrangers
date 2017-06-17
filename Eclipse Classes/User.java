package powerRangers;

import java.util.List;

public class User {
	
	private String userName;
	private int userId;
	private String email;
	private double balance;
	private Role role;
	
	public User(String userName, int userId, String email){
		this.userName = userName;
		this.userId = userId;
	}

	public  int getId(){
		return userId;
	}
	
	public  void setId(int newid){
		userId = newid;
	}
	
	public  String getName(){
		return userName;
	}
	
	public  void setName(String newName){
		userName = newName;
	}
	
	public static void login(){
		System.out.println("User Login");
	}
	
	public static void logout(){
		System.out.println("User logout");
	}
	
	
	public static void confirmTask(Task t){
		
	}
	
	public static List<Case> showAllCases(){
		return null;
	}
	
	public static List<Case> showAllCasesToConfirm(){
		return null;
	}
	
	public static void receiveReward(double reward){
		
	}
	
	public static void voteForTask(){
		
	}
	
}
