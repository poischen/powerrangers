import java.util.List;

public abstract class RoleForwarding extends Role{

	protected User user;
	String userName;
	int userId;
	
	
	@Override
	public int getId(){
		return user.getId();
	}

	@Override
	public void setId(int newid){
		user.setId(newid);
	}

	@Override
	public String getName(){
		return user.getName();
	}

	@Override
	public  void setName(String newName){
		user.setName(newName);
	}
	
	
	@Override
	public List<Role> getRoles(){
		return user.getRoles();
	}

	@Override
	public void setRole(Role specificRole){
		user.setRole(specificRole);
	}

	@Override
	public  boolean hasRole(Role specificRole){
		return user.hasRole(specificRole);
	}

}