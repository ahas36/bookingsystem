/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahas36
 */
@Entity
@Table(name = "FACILITY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facility.findAll", query = "SELECT f FROM Facility f"),
    @NamedQuery(name = "Facility.findByFacilityId", query = "SELECT f FROM Facility f WHERE f.facilityId = :facilityId"),
    @NamedQuery(name = "Facility.findByFacilityFee", query = "SELECT f FROM Facility f WHERE f.facilityFee = :facilityFee"),
    @NamedQuery(name = "Facility.findByBookingDurationUnit", query = "SELECT f FROM Facility f WHERE f.bookingDurationUnit = :bookingDurationUnit"),
    @NamedQuery(name = "Facility.findByFacilityName", query = "SELECT f FROM Facility f WHERE f.facilityName = :facilityName")})
public class Facility implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FACILITY_ID")
    private Integer facilityId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACILITY_FEE")
    private BigDecimal facilityFee;
    @Column(name = "BOOKING_DURATION_UNIT")
    private Integer bookingDurationUnit;
    @Size(max = 50)
    @Column(name = "FACILITY_NAME")
    private String facilityName;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "facility")
    private Equipment equipment;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facilityId")
    private List<SubBooking> subBookingList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "facility")
    private Room room;

    public Facility() {
    }

    public Facility(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public Facility(Integer facilityId, BigDecimal facilityFee) {
        this.facilityId = facilityId;
        this.facilityFee = facilityFee;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public BigDecimal getFacilityFee() {
        return facilityFee;
    }

    public void setFacilityFee(BigDecimal facilityFee) {
        this.facilityFee = facilityFee;
    }

    public Integer getBookingDurationUnit() {
        return bookingDurationUnit;
    }

    public void setBookingDurationUnit(Integer bookingDurationUnit) {
        this.bookingDurationUnit = bookingDurationUnit;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    @XmlTransient
    public List<SubBooking> getSubBookingList() {
        return subBookingList;
    }

    public void setSubBookingList(List<SubBooking> subBookingList) {
        this.subBookingList = subBookingList;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facilityId != null ? facilityId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facility)) {
            return false;
        }
        Facility other = (Facility) object;
        if ((this.facilityId == null && other.facilityId != null) || (this.facilityId != null && !this.facilityId.equals(other.facilityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Facility[ facilityId=" + facilityId + " ]";
    }
    
}
