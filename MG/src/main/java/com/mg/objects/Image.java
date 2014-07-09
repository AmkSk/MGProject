package com.mg.objects;

public class Image {
    // =============================================================================
    // Fields
    // =============================================================================

    private String id = null;
    private String description = null;
    private String engDescription = null;

    // =============================================================================
    // Constructors
    // =============================================================================

    public Image(){}

    public Image(String id, String description, String engDescription){
        this.id = id;
        this.description = description;
        this.engDescription = engDescription;
    }

    public Image(String id){
        this.id = id;
    }

    // =============================================================================
    // Getters
    // =============================================================================

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getEngDescription() {
        return engDescription;
    }


    // =============================================================================
    // Setters
    // =============================================================================


    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEngDescription(String engDescription) {
        this.engDescription = engDescription;
    }
}
