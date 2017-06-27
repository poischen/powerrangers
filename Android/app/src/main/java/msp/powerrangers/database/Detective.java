package msp.powerrangers.database;

public class Detective {

    private String caseId;
    private double rewardPerCase;
    private String userId;


    public Detective(String userId, String caseId, double rewardPerCase){
        this.userId = userId;
        this.caseId = caseId;
        this.rewardPerCase = rewardPerCase;

    }

}
