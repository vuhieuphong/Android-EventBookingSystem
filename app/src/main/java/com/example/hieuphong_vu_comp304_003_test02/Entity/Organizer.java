package com.example.hieuphong_vu_comp304_003_test02.Entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(indices = {@Index(value = {"orgName"},unique = true)})
public class Organizer {
    @PrimaryKey(autoGenerate = true)
    private int orgId;
    private String orgName;
    private String orgLocation;

    public Organizer(){}

    @Ignore
    public Organizer(String orgName, String orgLocation) {
        this.orgName = orgName;
        this.orgLocation = orgLocation;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgLocation() {
        return orgLocation;
    }

    public void setOrgLocation(String orgLocation) {
        this.orgLocation = orgLocation;
    }

    public static List<Organizer> prepopulateOrganizers(){
        Organizer org1=new Organizer("Cencol Events","Centennial College");
        Organizer org2=new Organizer("Seneca Events","Seneca College");
        Organizer org3=new Organizer("GB Events","George Brown College");
        Organizer org4=new Organizer("Humber Events","Humber College");

        return Arrays.asList(org1,org2,org3,org4);
    }
}


