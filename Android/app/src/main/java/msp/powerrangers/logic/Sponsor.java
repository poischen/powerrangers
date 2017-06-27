package msp.powerrangers.logic;

public class Sponsor extends RoleForwarding {
    double amount;

    public Sponsor(User user, double amount) {
        this.amount = amount;
        this.user = user;
    }

    public void donate (double amount){
    }


}