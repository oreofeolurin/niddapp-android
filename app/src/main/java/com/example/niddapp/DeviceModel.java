package com.example.niddapp;

public class DeviceModel {
    private String name;
    private String id;
   // private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   /* public String getIcon() {
        return icon;
    }*/

  /*  public void setIcon(String icon) {
        this.icon = icon;
    }*/

    public DeviceModel() { }

    public DeviceModel(String id, String name) {
        this.id = id;
        this.name = name;
       // this.icon = icon;
    }
}
