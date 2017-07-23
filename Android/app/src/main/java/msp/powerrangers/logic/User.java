package msp.powerrangers.logic;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private int balance;
    private double donatedValue;
    private List<String> reportedCasesIdList;
    private List<String> confirmedCasesIdList;
    private List<String> completedTasksIdList;
    private List<String> openTasksIdList;
    private List<Role> roleList;

    public User(){}

    public User(String dbId, String userId, String userName, String email){

        this.dbId = dbId;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.balance = 0;
        this.donatedValue = 0.0;

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

    public double getDonatedValue() {
        return donatedValue;
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

    public void setNumberCompletedTasks(){
        completedTasksIdList.add("viki");
    }

    public void addCaseIDToReportedCases(String caseID){
        reportedCasesIdList.add(caseID);
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

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setDonatedValue(double donatedValue) {
        this.donatedValue = donatedValue;
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



}