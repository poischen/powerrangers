package msp.powerrangers.logic;

public class Ranger extends RoleForwarding{
    int taskId;

    public Ranger(User user, int taskId){
        this.user = user;
        this.taskId = taskId;

    }


    public void completeTask(){
        System.out.println("Ranger Task completed");
    }
}