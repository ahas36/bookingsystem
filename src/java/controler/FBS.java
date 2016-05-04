/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import entity.Administrator;
import entity.Booking;
import entity.Facility;
import entity.KeyValue;
import entity.SubBooking;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author ahas36
 */
@Named(value = "fBS")
@ViewScoped
public class FBS implements Serializable {

    @PersistenceContext(unitName = "BookingSystemPU")
    private EntityManager em;

    public FBS() {
    }

    private int selectedFacil;

    public int getSelectedFacil() {
        return selectedFacil;
    }

    public void setSelectedFacil(int selectedFacil) {
        this.selectedFacil = selectedFacil;
    }
    private String facility_type;
    private String sub_facility_type;
    List<String> sub_facilities = new ArrayList<>();
    private Booking booking;
    List<KeyValue> facilities;

    @PostConstruct
    public void init() {
        booking = new Booking();
        List<SubBooking> tempList = new ArrayList<>();
        tempList.add(new SubBooking());
        booking.setSubBookingList(tempList);
        facility_type = "room";
        sub_facilities = em.createQuery("SELECT DISTINCT(m.roomType) from Room AS m").getResultList();
        sub_facility_type = sub_facilities.get(0);
        facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName) FROM Room AS m WHERE m.roomType=:rt").setParameter("rt", sub_facility_type).getResultList();
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Transactional
    public String createBooking() {
        try {
            List<SubBooking> subBooking = new ArrayList(booking.getSubBookingList());
            booking.setSubBookingList(null);
            booking.setUserName(em.find(Administrator.class, "admin"));
            em.persist(booking);
            em.flush();
            for (SubBooking sb : subBooking.subList(0, subBooking.size() - 1)) {
                sb.setBookingId(booking);
                sb.setSubBookingSatuts("active");
                em.persist(sb);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "/index.xhtml";
    }

    public String getFacility_type() {
        return facility_type;
    }

    public void setFacility_type(String facility_type) {
        this.facility_type = facility_type;
        if ("Room".equals(facility_type)) {
            sub_facilities = em.createQuery("SELECT DISTINCT(m.roomType) from Room AS m").getResultList();
            sub_facility_type = sub_facilities.get(0);
            sub_facility_type = sub_facilities.get(0);
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName) FROM Room AS m WHERE m.roomType=:rt").setParameter("rt", sub_facility_type).getResultList();

        } else {
            sub_facilities = em.createQuery("SELECT DISTINCT(m.equipmentType) from Equipment AS m").getResultList();
            sub_facility_type = sub_facilities.get(0);
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName) FROM Equipment  AS m WHERE m.equipmentType=:rt").setParameter("rt", sub_facility_type).getResultList();

        }

    }

    public String getSub_facility_type() {
        return sub_facility_type;
    }

    public void setSub_facility_type(String sub_facility_type) {
        this.sub_facility_type = sub_facility_type;
        if ("Room".equals(facility_type)) {
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName) FROM Room AS m WHERE m.roomType=:rt").setParameter("rt", sub_facility_type).getResultList();
        } else {
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName) FROM Equipment  AS m WHERE m.equipmentType=:rt").setParameter("rt", sub_facility_type).getResultList();
        }
    }

    public List<String> getSub_facilities() {
        return sub_facilities;
    }

    public void setSub_facilities(List<String> sub_facilities) {
        this.sub_facilities = sub_facilities;
    }

    public List<KeyValue> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<KeyValue> facilities) {
        this.facilities = facilities;
    }

    public void addSubBooking() {

        Facility find = em.find(Facility.class, selectedFacil);
        booking.getSubBookingList().get(booking.getSubBookingList().size() - 1).setFacilityId(find);
        if (validateDate()) {
            booking.getSubBookingList().add(new SubBooking());
        }
        else
        {
            booking.getSubBookingList().set(booking.getSubBookingList().size() - 1,new SubBooking());
        }
    }

    public void removeSubBooking(SubBooking sb) {
        booking.getSubBookingList().remove(sb);
    }

    public boolean validateDate() {
        int size = booking.getSubBookingList().size() - 1;
        SubBooking sb = booking.getSubBookingList().get(size);
        long diffInMillies = sb.getEndDate().getTime() - sb.getStartDate().getTime();
        long hour = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (hour % sb.getFacilityId().getBookingDurationUnit() != 0) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("The bookin duration should be dividable by " + sb.getFacilityId().getBookingDurationUnit() + " h");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage("err", msg);
            fc.renderResponse();
            return false;
        }
        long singleResult = (long) em.createQuery("SELECT count(sb) FROM SubBooking as sb WHERE sb.facilityId.facilityId=:id and ((sb.endDate BETWEEN :start and :end) or (sb.startDate BETWEEN :start and :end) or (sb.startDate<=:start and sb.endDate>=:end))")
                .setParameter("start", sb.getStartDate())
                .setParameter("end", sb.getEndDate())
                .setParameter("id", sb.getFacilityId().getFacilityId())
                .getSingleResult();
        for (SubBooking subBookin : booking.getSubBookingList().subList(0, size)) {
            if (!subBookin.getFacilityId().equals(sb.getFacilityId())) {
                continue;
            }
            if ((subBookin.getStartDate().getTime() > sb.getStartDate().getTime() && subBookin.getStartDate().getTime() < sb.getEndDate().getTime())
                    || (subBookin.getEndDate().getTime() > sb.getStartDate().getTime() && subBookin.getEndDate().getTime() < sb.getEndDate().getTime())
                    || (subBookin.getStartDate().getTime() <= sb.getStartDate().getTime() && subBookin.getEndDate().getTime() >= sb.getEndDate().getTime())) {
                singleResult++;
                break;
            }
        }
        if (singleResult != 0) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("The facility is booked");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage("err", msg);
            fc.renderResponse();
            return false;
        }
        return true;
    }

    public List<Booking> getAllBookings() {
        return em.createQuery("SELECT m FROM Booking AS m").getResultList();
    }

    public double calculateFee(SubBooking sb) {
        long diffInMillies = sb.getEndDate().getTime() - sb.getStartDate().getTime();
        double hour = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS) / (double) 60;
        return ((double) hour / sb.getFacilityId().getBookingDurationUnit()) * sb.getFacilityId().getFacilityFee().doubleValue();
    }

    public double calculateTotalFee() {
        int size = booking.getSubBookingList().size()-1;
        double sum = 0;
        for (SubBooking sb : booking.getSubBookingList().subList(0, size)) {
            sum+=calculateFee(sb);
        }
        return sum;
    }
}
