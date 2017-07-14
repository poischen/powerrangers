package msp.powerrangers.logic;


public class Confirmer extends RoleForwarding {

    long fixedReward = 5;
    String caseId;

    public Confirmer(User user, String caseId) {
        this.caseId = caseId;
        this.user = user;
    }

    public long getFixedReward(){
        return fixedReward;
    }


}