package com.mg.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * The base object of MG
 */
public class MGEntry {

    // =============================================================================
    // Fields
    // =============================================================================
    private String kind = null;
    private String name = null;
    private String engName = null;
    private String address = null;
    private String info = null;
    private String engInfo = null;
    private List<Image> imageList = new ArrayList<Image>();
    // =============================================================================
    // Constructors
    // =============================================================================

    public MGEntry(){
    }

    // =============================================================================
    // Getters
    // =============================================================================

    public String getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public String getEngName() {
        return engName;
    }

    public String getAddress() {
        return address;
    }

    public String getInfo() {
        return info;
    }

    public String getEngInfo() {
        return engInfo;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    // =============================================================================
    // Setters
    // =============================================================================

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setEngInfo(String engInfo) {
        this.engInfo = engInfo;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    // =============================================================================
    // Methods
    // =============================================================================

    public void addImage(Image image){
        this.imageList.add(image);
    }

    public Image getImage(int index){
        return imageList.get(index);
    }

    public void removeImage(Image image){
        this.imageList.remove(image);
    }

    public int getImageCount(){
        return imageList.size();
    }
}
