package msp.powerrangers.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
// in pattern RoleCore
public class User extends Role implements Serializable  {

    private String dbId;
    private String userId;
    private String userName;
    private String email;
    private String userPic = "";
    private double balance;
    private List<String> reportedCasesIdList;
    private List<String> confirmedCasesIdList;
    private List<String> completedTasksIdList;
    private List<String> openTasksIdList;
    private List<Role> roleList;

    public User(){

    }

    public User(String dbId, String userId, String userName, String email){

        this.dbId = dbId;
        this.userId = userId;
        this.userName = userName;
        this.email = email;

        reportedCasesIdList = new ArrayList<String>();
        reportedCasesIdList.add("DummyID");

        confirmedCasesIdList = new ArrayList<String>();
        confirmedCasesIdList.add("DummyID");

        completedTasksIdList = new ArrayList<String>();
        completedTasksIdList.add("DummyID");

        openTasksIdList = new ArrayList<String>();
        openTasksIdList.add("DummyID");

        roleList = new ArrayList<Role>();
    }


    // getter
    public String getName() {
        return userName;
    }

    public String getId() {
        return userId;
    }

    public String getDbId() {
        return dbId;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public int getNumberReportedCases() {
        return reportedCasesIdList.size()-1;
    }

    public int getNumberConfirmedCases() {
        return confirmedCasesIdList.size()-1;
    }

    public int getNumberCompletedTasks() {
        return completedTasksIdList.size()-1;
    }

    public int getNumberOpenTasks() {
        return openTasksIdList.size()-1;
    }


    // setter
    public void setName(String userName) {
        this.userName = userName;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public List<Role> getRoles() {
        if(roleList == null){
            System.out.println("RoleList is Null");
        }
        return roleList;
    }
    @Override
    public void setRole(Role specificRole) {
        roleList.add(specificRole);

    }
    @Override
    public boolean hasRole(Role specificRole) {
        return roleList.contains(specificRole);

    }


    public  void login(){
        System.out.println("User Login");
    }

    public  void logout(){
        System.out.println("User logout");
    }

    public  void confirmTask(Task t){

    }

    public List<Case> showAllCases(){
        return null;
    }

    public List<Case> showAllCasesToConfirm(){
        return null;
    }

    public void receiveReward(double reward){

    }

    public void voteForTask(){

    }


}