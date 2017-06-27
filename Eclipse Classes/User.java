import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// in pattern RoleCore

public class User extends Role {
	
		 
	private String userName;
	private int userId;
	private String email;
	private double balance;
	private List<Role> roleList;
	
	
	public User(String userName, int userId){
		this.userName = userName;
		this.userId = userId;
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
		
	
	@Override
	public int getId() {
		return userId;
	}

	@Override
	public void setId(int newid) {
		userId = newid;
		
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public void setName(String newName) {
		userName = newName;
		
	}

	@Override
	public List<Role> getRoles() {
		if(roleList == null){
			System.out.println("Rolelist is Null");
		}
		
		return roleList;
	}

	@Override
	public void setRole(Role specificRole) {

		if (roleList == null){
			roleList = new ArrayList<Role>();
		}
		
		roleList.add(specificRole);
		
	}

	@Override
	public boolean hasRole(Role specificRole) {
		return roleList.contains(specificRole);
		
	}

	
	public static void main(String[] args){
		
		User u = new User("Julia", 12);
		u.login();
				
		Detective d = new Detective(u, 12, 2);
		u.setRole(d);
		System.out.println(d.getName() + " " + d.getId());
		System.out.println(u.getRoles().toString());
		
		
		Ranger r1 = new Ranger(u, 12);
		u.setRole(r1);
		System.out.println(r1.getName() + " " + r1.getId());
		System.out.println(u.getRoles().toString());
		
		
		Ranger r2 = new Ranger(u, 13);
		u.setRole(r2);
		System.out.println(r2.getName() + " " + r2.getId());
		System.out.println(u.getRoles().toString());
		
		
		
	}
	
}
