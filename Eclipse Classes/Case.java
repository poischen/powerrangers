package powerRangers;

import java.util.List;

public class Case {

	int caseId;
	boolean isConfirmed;
	String information;
	String pictureURL; // url of the picture
	int skala; // 0,1,2
	double areaSize;
	double location;
	double reward;
	List<Task> relatedTasks;
	
	
	public Case(int caseId){
		this.caseId = caseId;
	}
	
	/*
	 * assign a task to a particular user
	 */
	
	public void assignTask(User user){
		
	}
	
	/*
	 * compute the reward that the ranger should get
	 */
	
	public double computeReward (double areaSize, int skala){
		return 1;
	}
	
	
	/*
	 * compute the number of tasks for a particular case
	 */
	public int computeNumberOfTasks (double areaSize, int skala){
		return 1;
	}
	
}
