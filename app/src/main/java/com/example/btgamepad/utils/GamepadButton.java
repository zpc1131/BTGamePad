package com.example.btgamepad.utils;

public class GamepadButton {

   public GamepadButton(String name) {
        this.Name = name;
        this.Status=false;
    }
    /**
     * 按钮名称
     */
    public String Name;
    /**
     *按钮状态
     */
    public Boolean Status;


    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean value){
        this.Status = value;
    }
}
