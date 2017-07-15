package msp.powerrangers.logic;

public class Detective extends RoleForwarding{
    String caseId;
    int rewardPerCase = 5;

    public Detective(User user, String caseId){
        this.user = user;
        this.caseId = caseId;
    }

    public void reportCase(){
        System.out.println("Detective Task completed");
    }

    public int getRewardPerCase(){
        return rewardPerCase;
    }
}