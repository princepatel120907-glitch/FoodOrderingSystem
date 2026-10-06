
package Model;

import java.sql.Timestamp;

public class Delivery {
    private int id;
    private int orderId;
    private String deliveryPartner;
    private String partnerPhone;
    private String status; // ASSIGNED/PICKED_UP/IN_TRANSIT/DELIVERED/FAILED
    private Timestamp assignedTime;
    private Timestamp pickedUpTime;
    private Timestamp deliveredTime;
    private int estimatedTime; // in minutes
    private int actualTime; // in minutes
    private String deliveryAddress;
    private String deliveryNotes;

    public Delivery() {}

    public Delivery(int id, int orderId, String deliveryPartner, String partnerPhone,
                    String status, Timestamp assignedTime, Timestamp pickedUpTime,
                    Timestamp deliveredTime, int estimatedTime, int actualTime,
                    String deliveryAddress, String deliveryNotes) {
        this.id = id;
        this.orderId = orderId;
        this.deliveryPartner = deliveryPartner;
        this.partnerPhone = partnerPhone;
        this.status = status;
        this.assignedTime = assignedTime;
        this.pickedUpTime = pickedUpTime;
        this.deliveredTime = deliveredTime;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.deliveryAddress = deliveryAddress;
        this.deliveryNotes = deliveryNotes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getDeliveryPartner() { return deliveryPartner; }
    public void setDeliveryPartner(String deliveryPartner) { this.deliveryPartner = deliveryPartner; }

    public String getPartnerPhone() { return partnerPhone; }
    public void setPartnerPhone(String partnerPhone) { this.partnerPhone = partnerPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getAssignedTime() { return assignedTime; }
    public void setAssignedTime(Timestamp assignedTime) { this.assignedTime = assignedTime; }

    public Timestamp getPickedUpTime() { return pickedUpTime; }
    public void setPickedUpTime(Timestamp pickedUpTime) { this.pickedUpTime = pickedUpTime; }

    public Timestamp getDeliveredTime() { return deliveredTime; }
    public void setDeliveredTime(Timestamp deliveredTime) { this.deliveredTime = deliveredTime; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }

    public int getActualTime() { return actualTime; }
    public void setActualTime(int actualTime) { this.actualTime = actualTime; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getDeliveryNotes() { return deliveryNotes; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", deliveryPartner='" + deliveryPartner + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}