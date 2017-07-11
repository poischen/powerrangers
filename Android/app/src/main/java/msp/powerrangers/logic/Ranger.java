package msp.powerrangers.logic;

public class Ranger extends RoleForwarding{
    String taskId;

    public Ranger(User user, String taskId){
        this.user = user;
        this.taskId = taskId;

    }


    public void completeTask(){
        System.out.println("Ranger Task completed");
    }
}