package msp.powerrangers.logic;


public class Confirmer extends RoleForwarding {

    int fixedReward = 2;
    String caseId;

    public Confirmer(User user, String caseId) {
        this.caseId = caseId;
        this.user = user;
    }

    public int getFixedReward(){
        return fixedReward;
    }


}