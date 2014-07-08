package com.mg.objects;

import java.util.Date;

/**
 * Exhibition object of MG. It has the same properties as an "Event". Distinguishable with "Kind" property
 */
public class Exhibition extends Actuality implements Comparable<Exhibition>{

    // =============================================================================
    // Fields
    // =============================================================================

    private Date dateFrom;
    private Date dateTo;

    // =============================================================================
    // Constructors
    // =============================================================================

    public Exhibition(){
        super();
    }

    // =============================================================================
    // Override methods
    // =============================================================================

    @Override
    public int compareTo(Exhibition another) {
        return this.getDateFrom().compareTo(another.getDateFrom());
    }

    // =============================================================================
    // Getters
    // =============================================================================

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    // =============================================================================
    // Setters
    // =============================================================================

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    // =============================================================================
    // Methods
    // =============================================================================


}
