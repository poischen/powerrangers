package msp.powerrangers.logic;

public class Task {

    int caseId;
    int taskId;
    boolean isConfirmed;
    boolean isAssigned;
    boolean taskCompleted;
    int numberOfConfirmations;

    public int getCaseId() {
        return caseId;
    }
    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public boolean isConfirmed() {
        return isConfirmed;
    }
    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
    public boolean isAssigned() {
        return isAssigned;
    }
    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }
    public boolean isTaskCompleted() {
        return taskCompleted;
    }
    public void setTaskCompleted(boolean taskCompleted) {
        this.taskCompleted = taskCompleted;
    }
    public int getNumberOfConfirmations() {
        return numberOfConfirmations;
    }
    public void setNumberOfConfirmations(int numberOfConfirmations) {
        this.numberOfConfirmations = numberOfConfirmations;
    }

    public Task(int caseId, int taskId){
        this.caseId = caseId;
        this.taskId = taskId;
    }


    public void confirmTask(){

    }


}