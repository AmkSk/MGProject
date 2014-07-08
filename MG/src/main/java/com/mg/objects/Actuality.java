package com.mg.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Actuality extends MGEntry{

    // =============================================================================
    // Fields
    // =============================================================================

    private Date insertionDate;
    private Date removeDate;
    private List<String> buildingIdList = new ArrayList<String>();
    private String fbLink = null;
    private String twLink = null;
    private String ytLink = null;

    // =============================================================================
    // Constructors
    // =============================================================================

    public Actuality(){}

    // =============================================================================
    // Getters
    // =============================================================================

    public Date getInsertionDate() {
        return insertionDate;
    }

    public Date getRemoveDate() {
        return removeDate;
    }

    public String getFbLink() {
        return fbLink;
    }

    public String getTwLink() {
        return twLink;
    }

    public String getYtLink() {
        return ytLink;
    }

    // =============================================================================
    // Setters
    // =============================================================================


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public void setRemoveDate(Date removeDate) {
        this.removeDate = removeDate;
    }

    public void setTwLink(String twLink) {
        this.twLink = twLink;
    }

    public void setYtLink(String ytLink) {
        this.ytLink = ytLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    // =============================================================================
    // Methods
    // =============================================================================

    public void addBuildingId(String buildingId){
        this.buildingIdList.add(buildingId);
    }

    public String getBuildingId(int index){
        return buildingIdList.get(index);
    }

    public void removeBuildingId(String buildingId){
        this.buildingIdList.remove(buildingId);
    }

    public List<String> getBuildingIdList() {
        return buildingIdList;
    }
}
