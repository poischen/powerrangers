package msp.powerrangers.logic;

public class Detective extends RoleForwarding{
    int caseId;
    double rewardPerCase;

    public Detective(User user, int caseId, double rewardPerCase){
        this.user = user;
        this.caseId = caseId;
        this.rewardPerCase = rewardPerCase;

    }

    public void reportCase(){
        System.out.println("Detective Task completed");
    }

}