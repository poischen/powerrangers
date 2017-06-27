package msp.powerrangers.logic;


public class Confirmer extends RoleForwarding {

    double fixedReward;
    int caseId;

    public Confirmer(User user, double fixedReward, int caseId) {
        this.fixedReward = fixedReward;
        this.caseId = caseId;
        this.user = user;
    }
}