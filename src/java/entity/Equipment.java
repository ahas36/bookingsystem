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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
@Table(name = "EQUIPMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM Equipment e"),
    @NamedQuery(name = "Equipment.findByFacilityId", query = "SELECT e FROM Equipment e WHERE e.facilityId = :facilityId"),
    @NamedQuery(name = "Equipment.findByEquipmentType", query = "SELECT e FROM Equipment e WHERE e.equipmentType = :equipmentType"),
    @NamedQuery(name = "Equipment.findByEquipmentBoughtDate", query = "SELECT e FROM Equipment e WHERE e.equipmentBoughtDate = :equipmentBoughtDate")})
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACILITY_ID")
    private Integer facilityId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EQUIPMENT_TYPE")
    private String equipmentType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EQUIPMENT_BOUGHT_DATE")
    @Temporal(TemporalType.DATE)
    private Date equipmentBoughtDate;
    @JoinColumn(name = "FACILITY_ID", referencedColumnName = "FACILITY_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Facility facility;

    public Equipment() {
    }

    public Equipment(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public Equipment(Integer facilityId, String equipmentType, Date equipmentBoughtDate) {
        this.facilityId = facilityId;
        this.equipmentType = equipmentType;
        this.equipmentBoughtDate = equipmentBoughtDate;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Date getEquipmentBoughtDate() {
        return equipmentBoughtDate;
    }

    public void setEquipmentBoughtDate(Date equipmentBoughtDate) {
        this.equipmentBoughtDate = equipmentBoughtDate;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
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
        if (!(object instanceof Equipment)) {
            return false;
        }
        Equipment other = (Equipment) object;
        if ((this.facilityId == null && other.facilityId != null) || (this.facilityId != null && !this.facilityId.equals(other.facilityId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Equipment[ facilityId=" + facilityId + " ]";
    }
    
}
