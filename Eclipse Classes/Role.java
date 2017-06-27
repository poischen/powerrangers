import java.util.List;


public abstract class Role {
	

	public abstract int getId();
	
	public abstract void setId(int newid);
	
	public abstract String getName();
	
	public abstract void setName(String newName);
	
	public abstract List<Role> getRoles();
	
	public abstract void setRole(Role specificRole);
	
	public abstract boolean hasRole(Role specificRole);
		
	

	
}
