package msp.powerrangers.model;

public class User {

    private String userName;
    private String userId;
    private String dbId;
    private String email;
    private double balance;
    //private Role role;

    public User(String userName, String dbId, String userId, String email){
        this.userName = userName;
        this.userId = userId;
        this.dbId = dbId;
        this.email = email;
    }

    public  String getId(){
        return userId;
    }

    public  void setId(String newid){
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


    /*public static void confirmTask(Task t){

    }

    public static List<Case> showAllCases(){
        return null;
    }

    public static List<Case> showAllCasesToConfirm(){
        return null;
    }*/

    public static void receiveReward(double reward){

    }

    public static void voteForTask(){

    }

}