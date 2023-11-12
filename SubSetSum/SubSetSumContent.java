package SubSetSum;

import Board.Content;

public class SubSetSumContent implements Content{
    private int value;
    private boolean hasValue;
    private boolean isViewed;
    private boolean isTarget;

    public SubSetSumContent(){
        this.hasValue = false;
        this.isViewed = false;
        this.isTarget = false;
    }

    public SubSetSumContent(int value){
        this.value = value;
        this.hasValue = true;
    }

    public void setTarget(){
        this.isTarget = true;
    }

    public void unsetTarget(){
        this.isTarget = false;
    }

    public boolean isTarget(){
        return this.isTarget;
    }

    public void setViewed(){
        this.isViewed = true;
    }

    public void unsetViewed(){
        this.isViewed = false;
    }

    public boolean isViewed(){
        return this.isViewed;
    }

    public void setValue(int value){
        this.value = value;
        this.hasValue = true;
    }

    public int getValue(){
        return this.value;
    }

    public boolean hasValue(){
        return this.hasValue;
    }
}
