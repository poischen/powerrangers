package msp.powerrangers.logic;

public class Detective extends RoleForwarding{
    String caseId;
    double rewardPerCase = 2;

    public Detective(User user, String caseId){
        this.user = user;
        this.caseId = caseId;
    }

    public void reportCase(){
        System.out.println("Detective Task completed");
    }

}