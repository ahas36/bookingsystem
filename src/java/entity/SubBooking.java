/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahas36
 */
@Entity
@Table(name = "SUB_BOOKING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubBooking.findAll", query = "SELECT s FROM SubBooking s"),
    @NamedQuery(name = "SubBooking.findBySubBookingId", query = "SELECT s FROM SubBooking s WHERE s.subBookingId = :subBookingId"),
    @NamedQuery(name = "SubBooking.findByStartDate", query = "SELECT s FROM SubBooking s WHERE s.startDate = :startDate"),
    @NamedQuery(name = "SubBooking.findByEndDate", query = "SELECT s FROM SubBooking s WHERE s.endDate = :endDate"),
    @NamedQuery(name = "SubBooking.findBySubBookingSatuts", query = "SELECT s FROM SubBooking s WHERE s.subBookingSatuts = :subBookingSatuts")})
public class SubBooking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SUB_BOOKING_ID")
    private Integer subBookingId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SUB_BOOKING_SATUTS")
    private String subBookingSatuts;
    @JoinColumn(name = "BOOKING_ID", referencedColumnName = "BOOKING_ID")
    @ManyToOne(optional = false)
    private Booking bookingId;
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID")
    @ManyToOne(optional = false)
    private Facility facilityId;

    public SubBooking() {
    }

    public SubBooking(Integer subBookingId) {
        this.subBookingId = subBookingId;
    }

    public SubBooking(Integer subBookingId, Date startDate, Date endDate, String subBookingSatuts) {
        this.subBookingId = subBookingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subBookingSatuts = subBookingSatuts;
    }

    public Integer getSubBookingId() {
        return subBookingId;
    }

    public void setSubBookingId(Integer subBookingId) {
        this.subBookingId = subBookingId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSubBookingSatuts() {
        return subBookingSatuts;
    }

    public void setSubBookingSatuts(String subBookingSatuts) {
        this.subBookingSatuts = subBookingSatuts;
    }

    public Booking getBookingId() {
        return bookingId;
    }

    public void setBookingId(Booking bookingId) {
        this.bookingId = bookingId;
    }

    public Facility getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Facility facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subBookingId != null ? subBookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubBooking)) {
            return false;
        }
        SubBooking other = (SubBooking) object;
        if ((this.subBookingId == null && other.subBookingId != null) || (this.subBookingId != null && !this.subBookingId.equals(other.subBookingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SubBooking[ subBookingId=" + subBookingId + " ]";
    }
    
}
