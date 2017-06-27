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

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public int getSkala() {
		return skala;
	}

	public void setSkala(int skala) {
		this.skala = skala;
	}

	public double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(double areaSize) {
		this.areaSize = areaSize;
	}

	public double getLocation() {
		return location;
	}

	public void setLocation(double location) {
		this.location = location;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public List<Task> getRelatedTasks() {
		return relatedTasks;
	}

	public void setRelatedTasks(List<Task> relatedTasks) {
		this.relatedTasks = relatedTasks;
	}
	
	
	
}
