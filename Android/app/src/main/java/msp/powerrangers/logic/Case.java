package msp.powerrangers.logic;

import java.util.List;

public class Case {

    private String dbId;
    private String caseId;
    private String name;
    private String city;
    private String country;
    private int scale;
    private int areaX;
    private int areaY;
    //private String pictureURL;
    private String comment;
    private boolean isConfirmed;
    List<Task> relatedTasks;

    public Case(String dbId, String caseId, String name, String city, String country,
                int scale, int areaX, int areaY, //String pictureURL,
                String comment){
        this.dbId = dbId;
        this.name = name;
        this.caseId = caseId;
        this.city = city;
        this.country = country;
        this.scale = scale;
        this.areaX = areaX;
        this.areaY = areaY;
        //this.pictureURL = pictureURL;
        this.comment = comment;
        this.isConfirmed = false;
    }


    // getter
    public String getId() {
        return caseId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getScale() {
        return scale;
    }

    public int getAreaX() {
        return areaX;
    }

    public int getAreaY() {
        return areaY;
    }

    public String getComment() {
        return comment;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public List<Task> getRelatedTasks() {
        return relatedTasks;
    }
    /*
    public String getPictureURL() {
        return pictureURL;
    }*/

    // setter
    public void setId(String caseId) {
        this.caseId = caseId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setAreaX(int areaX) {
        this.areaX = areaX;
    }

    public void setAreaY(int areaY) {
        this.areaY = areaY;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public void setRelatedTasks(List<Task> relatedTasks) {
        this.relatedTasks = relatedTasks;
    }

    /*
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
    */


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