package travel.thirdparty.touroperator.service.adapter.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "reservationResponse")
public class ReservationResponse {

    @JacksonXmlProperty(isAttribute = true)
    private String status;

    private String referenceNo;

    private RoomReferenceDetails roomReferenceDetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public RoomReferenceDetails getRoomReferenceDetails() {
        return roomReferenceDetails;
    }

    public void setRoomReferenceDetails(RoomReferenceDetails roomReferenceDetails) {
        this.roomReferenceDetails = roomReferenceDetails;
    }
}

class RoomReferenceDetails {
    @JacksonXmlProperty(isAttribute = true)
    private int roomNo;

    private String roomReferenceNo;

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomReferenceNo() {
        return roomReferenceNo;
    }

    public void setRoomReferenceNo(String roomReferenceNo) {
        this.roomReferenceNo = roomReferenceNo;
    }
}
