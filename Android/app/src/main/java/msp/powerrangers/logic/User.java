package msp.powerrangers.logic;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
// in pattern RoleCore
public class User extends Role implements Serializable  {

    private String dbId;
    private String userId;
    private List<String> caseIdList;
    private List<String> taskIdList;
    private String userName;
    private String email;
    private String userPic = "";
    private double balance;
    private List<Role> roleList;

    public User(){

    }

    public User(String dbId, String userId, String userName, String email){
        this.dbId = dbId;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        roleList = new ArrayList<Role>();
        caseIdList = new ArrayList<String>();
        taskIdList = new ArrayList<String>();
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

    public void addCaseIDtoList(String caseID){
        caseIdList.add(caseID);
        Object [] totest;
        totest = caseIdList.toArray();
        Log.i("CASE ID ADDED TO LIST" , Arrays.toString(totest));
    }

    public void addTaskIDtoList(String taskID){
        taskIdList.add(taskID);

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