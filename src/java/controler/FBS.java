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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
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
    private String dateToShow;

    public String getDateToShow() {
        return dateToShow;
    }

    public void setDateToShow(String dateToShow) {
        this.dateToShow = dateToShow;
    }
    
    
    
    private int timeUnit;

    public int getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
    }

    public FBS() {
    }

    private int selectedFacil;

    public int getSelectedFacil() {
        return selectedFacil;
    }

    public void setSelectedFacil(int selectedFacil) {
        timeUnit = em.find(Facility.class, selectedFacil).getBookingDurationUnit();
        this.selectedFacil = selectedFacil;
    }
    private String facility_type;
    private String sub_facility_type;
    List<String> sub_facilities = new ArrayList<>();
    private Booking booking;
    List<KeyValue> facilities;
    private Facility facilityToShow;

    public Facility getFacilityToShow() {
        return facilityToShow;
    }

    public void setFacilityToShow(Facility facilityToShow) {
        this.facilityToShow = facilityToShow;
    }
    
    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if(requestParameterMap.containsKey("dts"))
        {
            dateToShow=requestParameterMap.get("dts");
        }
        else
        {
            dateToShow="today";
        }
        if(requestParameterMap.containsKey("facilityID"))
        {
            String temp=requestParameterMap.get("facilityID");
            facilityToShow=em.find(Facility.class, Integer.valueOf(temp));
        }
        
        try {
            String parameter = requestParameterMap.get("bookingid");
            booking = em.find(Booking.class, Integer.valueOf(parameter));
            booking.getSubBookingList().add(new SubBooking());
            memberVerified = true;

        } catch (Exception e) {
            booking = new Booking();
            List<SubBooking> tempList = new ArrayList<>();
            tempList.add(new SubBooking());
            booking.setSubBookingList(tempList);
            memberVerified = false;
        }

        facility_type = "room";
        sub_facilities = em.createQuery("SELECT DISTINCT(m.roomType) from Room AS m").getResultList();
        sub_facility_type = sub_facilities.get(0);
        facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName,m.facility.bookingDurationUnit) FROM Room AS m WHERE m.roomType=:rt").setParameter("rt", sub_facility_type).getResultList();
        if (facilities != null && !facilities.isEmpty()) {
            timeUnit = facilities.get(0).getTime();
        }
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
            booking = em.merge(booking);
            em.flush();
            for (SubBooking sb : subBooking.subList(0, subBooking.size() - 1)) {
                sb.setBookingId(booking);
                sb.setSubBookingSatuts("active");
                em.merge(sb);

            }
            em.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "/index.xhtml?faces-redirect=true";
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
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName,m.facility.bookingDurationUnit) FROM Room AS m WHERE m.roomType=:rt").setParameter("rt", sub_facility_type).getResultList();

        } else {
            sub_facilities = em.createQuery("SELECT DISTINCT(m.equipmentType) from Equipment AS m").getResultList();
            sub_facility_type = sub_facilities.get(0);
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName,m.facility.bookingDurationUnit) FROM Equipment  AS m WHERE m.equipmentType=:rt").setParameter("rt", sub_facility_type).getResultList();

        }
        if (facilities != null && !facilities.isEmpty()) {
            timeUnit = facilities.get(0).getTime();
        }
    }

    public String getSub_facility_type() {
        return sub_facility_type;
    }

    public void setSub_facility_type(String sub_facility_type) {
        this.sub_facility_type = sub_facility_type;
        if ("Room".equals(facility_type)) {
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName,m.facility.bookingDurationUnit) FROM Room AS m WHERE m.roomType=:rt").setParameter("rt", sub_facility_type).getResultList();
        } else {
            facilities = em.createQuery("SELECT new entity.KeyValue(m.facilityId,m.facility.facilityName,m.facility.bookingDurationUnit) FROM Equipment  AS m WHERE m.equipmentType=:rt").setParameter("rt", sub_facility_type).getResultList();
        }
        if (facilities != null && !facilities.isEmpty()) {
            timeUnit = facilities.get(0).getTime();
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
        } else {
            booking.getSubBookingList().set(booking.getSubBookingList().size() - 1, new SubBooking());
        }
    }

    @Transactional
    public void removeSubBooking(SubBooking sb) {
        booking.getSubBookingList().remove(sb);
        if (sb.getSubBookingId() != null) {
            SubBooking find = em.find(SubBooking.class, sb.getSubBookingId());
            em.remove(find);
        }
    }

    public boolean validateDate() {
        int size = booking.getSubBookingList().size() - 1;
        SubBooking sb = booking.getSubBookingList().get(size);
        if (sb.getEndDate() == null || sb.getStartDate() == null) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("end date and start date shouldn't be null");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage("j_idt6:dp1", msg);
            fc.renderResponse();
            return false;
        }
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
            SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy");
            FacesMessage msg = new FacesMessage("The facility is booked. To find the avaiable dates for this facility please refer to this link <a href='Facility.xhtml?facilityID="+sb.getFacilityId().getFacilityId()+"&dts="+sdf.format(sb.getStartDate())+"'>"+sb.getFacilityId().getFacilityName()+"</a>");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage("j_idt6:dp1", msg);
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
        int size = booking.getSubBookingList().size() - 1;
        double sum = 0;
        for (SubBooking sb : booking.getSubBookingList().subList(0, size)) {
            sum += calculateFee(sb);
        }
        return sum;
    }

    @Transactional
    public void removeBooking(Booking booking) {
        try {
            for (SubBooking sb : booking.getSubBookingList()) {
                SubBooking s = em.find(sb.getClass(), sb.getSubBookingId());
                em.remove(s);
            }
            Booking b = em.find(booking.getClass(), booking.getBookingId());
            em.remove(b);
        } catch (Exception e) {
            System.out.println("controler.FBS.removeBooking()");
        }

    }

    private boolean memberVerified;

    public boolean isMemberVerified() {
        return memberVerified;
    }

    public void setMemberVerified(boolean memberVerified) {
        this.memberVerified = memberVerified;
    }

    public void verifyMember() {
        memberVerified = true;
    }

    public String getFacilityEvents() {
        Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try {
            String parameter = requestParameterMap.get("facilityID");
        List<SubBooking> resultList = em.createQuery("SELECT m From SubBooking as m WHERE m.facilityId.facilityId=:p").setParameter("p", Integer.valueOf(parameter)).getResultList();
        String result = "";
        for (SubBooking sb : resultList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sb.getStartDate());
            String startDate = "new Date(" + cal.get(Calendar.YEAR) + ", " + cal.get(Calendar.MONTH) + "," + cal.get(Calendar.DAY_OF_MONTH) + "," + cal.get(Calendar.HOUR_OF_DAY) + "," + cal.get(Calendar.MINUTE) + ")";
            cal.setTime(sb.getEndDate());
            String endDate = "new Date(" + cal.get(Calendar.YEAR) + ", " + cal.get(Calendar.MONTH) + "," + cal.get(Calendar.DAY_OF_MONTH) + "," + cal.get(Calendar.HOUR_OF_DAY) + "," + cal.get(Calendar.MINUTE) + ")";
            result += "{'id':" + sb.getSubBookingId() + ", 'start': " + startDate + ", 'end': " + endDate + ",'title':'" + sb.getBookingId().getMemberId() + "'},";
        }
        return result.substring(0, result.length() - 1);
        }
        catch(Exception e)
        {
            return "";
        }
 }

}
