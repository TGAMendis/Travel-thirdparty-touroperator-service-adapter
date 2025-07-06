package travel.thirdparty.touroperator.service.adapter.dto;

import jakarta.xml.bind.annotation.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OTA_HotelResNotifRQ", namespace = "http://www.opentravel.org/OTA/2003/05")
public class ReservationRequest {

    @XmlAttribute(name = "Version")
    private String version;

    @XmlAttribute(name = "EchoToken")
    private String echoToken;

    @XmlAttribute(name = "TimeStamp")
    private String timeStamp;

    @XmlElement(name = "POS", namespace = "http://www.opentravel.org/OTA/2003/05")
    private POS pos;

    @XmlElementWrapper(name = "HotelReservations", namespace = "http://www.opentravel.org/OTA/2003/05")
    @XmlElement(name = "HotelReservation", namespace = "http://www.opentravel.org/OTA/2003/05")
    private List<HotelReservation> hotelReservations;

    // Getters and Setters
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEchoToken() {
        return echoToken;
    }

    public void setEchoToken(String echoToken) {
        this.echoToken = echoToken;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public POS getPos() {
        return pos;
    }

    public void setPos(POS pos) {
        this.pos = pos;
    }

    public List<HotelReservation> getHotelReservations() {
        return hotelReservations;
    }

    public void setHotelReservations(List<HotelReservation> hotelReservations) {
        this.hotelReservations = hotelReservations;
    }

    // Inner Classes
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class POS {
        @XmlElement(name = "Source", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Source source;

        // Getters and Setters
        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Source {
        @XmlElement(name = "BookingChannel", namespace = "http://www.opentravel.org/OTA/2003/05")
        private BookingChannel bookingChannel;

        // Getters and Setters
        public BookingChannel getBookingChannel() {
            return bookingChannel;
        }

        public void setBookingChannel(BookingChannel bookingChannel) {
            this.bookingChannel = bookingChannel;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BookingChannel {
        @XmlAttribute(name = "Type")
        private String type;

        @XmlElement(name = "CompanyName", namespace = "http://www.opentravel.org/OTA/2003/05")
        private CompanyName companyName;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public CompanyName getCompanyName() {
            return companyName;
        }

        public void setCompanyName(CompanyName companyName) {
            this.companyName = companyName;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CompanyName {
        @XmlAttribute(name = "Code")
        private String code;

        @XmlValue
        private String value;

        // Getters and Setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class HotelReservation {
        @XmlAttribute(name = "ResStatus")
        private String resStatus;

        @XmlAttribute(name = "CreateDateTime")
        private String createDateTime;

        @XmlElement(name = "ResGlobalInfo", namespace = "http://www.opentravel.org/OTA/2003/05")
        private ResGlobalInfo resGlobalInfo;

        @XmlElement(name = "ResGuests", namespace = "http://www.opentravel.org/OTA/2003/05")
        private ResGuests resGuests;

        @XmlElement(name = "RoomStays", namespace = "http://www.opentravel.org/OTA/2003/05")
        private RoomStays roomStays;

        // Getters and Setters
        public String getResStatus() {
            return resStatus;
        }

        public void setResStatus(String resStatus) {
            this.resStatus = resStatus;
        }

        public String getCreateDateTime() {
            return createDateTime;
        }

        public void setCreateDateTime(String createDateTime) {
            this.createDateTime = createDateTime;
        }

        public ResGlobalInfo getResGlobalInfo() {
            return resGlobalInfo;
        }

        public void setResGlobalInfo(ResGlobalInfo resGlobalInfo) {
            this.resGlobalInfo = resGlobalInfo;
        }

        public ResGuests getResGuests() {
            return resGuests;
        }

        public void setResGuests(ResGuests resGuests) {
            this.resGuests = resGuests;
        }

        public RoomStays getRoomStays() {
            return roomStays;
        }

        public void setRoomStays(RoomStays roomStays) {
            this.roomStays = roomStays;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ResGlobalInfo {
        @XmlElement(name = "HotelReservationIDs", namespace = "http://www.opentravel.org/OTA/2003/05")
        private HotelReservationIDs hotelReservationIDs;

        // Getters and Setters
        public HotelReservationIDs getHotelReservationIDs() {
            return hotelReservationIDs;
        }

        public void setHotelReservationIDs(HotelReservationIDs hotelReservationIDs) {
            this.hotelReservationIDs = hotelReservationIDs;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class HotelReservationIDs {
        @XmlElement(name = "HotelReservationID", namespace = "http://www.opentravel.org/OTA/2003/05")
        private HotelReservationID hotelReservationID;

        // Getters and Setters
        public HotelReservationID getHotelReservationID() {
            return hotelReservationID;
        }

        public void setHotelReservationID(HotelReservationID hotelReservationID) {
            this.hotelReservationID = hotelReservationID;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class HotelReservationID {
        @XmlAttribute(name = "ResID_Type")
        private String resIDType;

        @XmlAttribute(name = "ResID_Value")
        private String resIDValue;

        // Getters and Setters
        public String getResIDType() {
            return resIDType;
        }

        public void setResIDType(String resIDType) {
            this.resIDType = resIDType;
        }

        public String getResIDValue() {
            return resIDValue;
        }

        public void setResIDValue(String resIDValue) {
            this.resIDValue = resIDValue;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ResGuests {
        @XmlElement(name = "ResGuest", namespace = "http://www.opentravel.org/OTA/2003/05")
        private ResGuest resGuest;

        // Getters and Setters
        public ResGuest getResGuest() {
            return resGuest;
        }

        public void setResGuest(ResGuest resGuest) {
            this.resGuest = resGuest;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ResGuest {
        @XmlAttribute(name = "ResGuestRPH")
        private String resGuestRPH;

        @XmlElement(name = "Profiles", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Profiles profiles;

        @XmlElement(name = "Comments", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Comments comments;

        // Getters and Setters
        public String getResGuestRPH() {
            return resGuestRPH;
        }

        public void setResGuestRPH(String resGuestRPH) {
            this.resGuestRPH = resGuestRPH;
        }

        public Profiles getProfiles() {
            return profiles;
        }

        public void setProfiles(Profiles profiles) {
            this.profiles = profiles;
        }

        public Comments getComments() {
            return comments;
        }

        public void setComments(Comments comments) {
            this.comments = comments;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Profiles {
        @XmlElement(name = "ProfileInfo", namespace = "http://www.opentravel.org/OTA/2003/05")
        private ProfileInfo profileInfo;

        // Getters and Setters
        public ProfileInfo getProfileInfo() {
            return profileInfo;
        }

        public void setProfileInfo(ProfileInfo profileInfo) {
            this.profileInfo = profileInfo;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ProfileInfo {
        @XmlElement(name = "Profile", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Profile profile;

        // Getters and Setters
        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Profile {
        @XmlElement(name = "Customer", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Customer customer;

        // Getters and Setters
        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Customer {
        @XmlElement(name = "PersonName", namespace = "http://www.opentravel.org/OTA/2003/05")
        private PersonName personName;

        @XmlElement(name = "Telephone", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String telephone;

        @XmlElement(name = "Email", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String email;

        @XmlElement(name = "Address", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Address address;

        // Getters and Setters
        public PersonName getPersonName() {
            return personName;
        }

        public void setPersonName(PersonName personName) {
            this.personName = personName;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PersonName {
        @XmlElement(name = "GivenName", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String givenName;

        @XmlElement(name = "Surname", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String surname;

        // Getters and Setters
        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Address {
        @XmlElement(name = "AddressLine", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String addressLine;

        @XmlElement(name = "CityName", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String cityName;

        @XmlElement(name = "StateProv", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String stateProv;

        @XmlElement(name = "CountryName", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String countryName;

        @XmlElement(name = "PostalCode", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String postalCode;

        // Getters and Setters
        public String getAddressLine() {
            return addressLine;
        }

        public void setAddressLine(String addressLine) {
            this.addressLine = addressLine;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getStateProv() {
            return stateProv;
        }

        public void setStateProv(String stateProv) {
            this.stateProv = stateProv;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomStays {
        @XmlElement(name = "RoomStay", namespace = "http://www.opentravel.org/OTA/2003/05")
        private RoomStay roomStay;

        // Getters and Setters
        public RoomStay getRoomStay() {
            return roomStay;
        }

        public void setRoomStay(RoomStay roomStay) {
            this.roomStay = roomStay;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomStay {
        @XmlAttribute(name = "IndexNumber")
        private String indexNumber;

        @XmlAttribute(name = "PromotionCode")
        private String promotionCode;

        @XmlElement(name = "BasicPropertyInfo", namespace = "http://www.opentravel.org/OTA/2003/05")
        private BasicPropertyInfo basicPropertyInfo;

        @XmlElement(name = "TimeSpan", namespace = "http://www.opentravel.org/OTA/2003/05")
        private TimeSpan timeSpan;

        @XmlElement(name = "Guarantee", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Guarantee guarantee;

        @XmlElement(name = "GuestCounts", namespace = "http://www.opentravel.org/OTA/2003/05")
        private GuestCounts guestCounts;

        @XmlElement(name = "Total", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Total total;

        @XmlElement(name = "RoomRates", namespace = "http://www.opentravel.org/OTA/2003/05")
        private RoomRates roomRates;

        @XmlElement(name = "ResGuestRPHs", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String resGuestRPHs;

        // Getters and Setters
        public String getIndexNumber() {
            return indexNumber;
        }

        public void setIndexNumber(String indexNumber) {
            this.indexNumber = indexNumber;
        }

        public String getPromotionCode() {
            return promotionCode;
        }

        public void setPromotionCode(String promotionCode) {
            this.promotionCode = promotionCode;
        }

        public BasicPropertyInfo getBasicPropertyInfo() {
            return basicPropertyInfo;
        }

        public void setBasicPropertyInfo(BasicPropertyInfo basicPropertyInfo) {
            this.basicPropertyInfo = basicPropertyInfo;
        }

        public TimeSpan getTimeSpan() {
            return timeSpan;
        }

        public void setTimeSpan(TimeSpan timeSpan) {
            this.timeSpan = timeSpan;
        }

        public Guarantee getGuarantee() {
            return guarantee;
        }

        public void setGuarantee(Guarantee guarantee) {
            this.guarantee = guarantee;
        }

        public GuestCounts getGuestCounts() {
            return guestCounts;
        }

        public void setGuestCounts(GuestCounts guestCounts) {
            this.guestCounts = guestCounts;
        }

        public Total getTotal() {
            return total;
        }

        public void setTotal(Total total) {
            this.total = total;
        }

        public RoomRates getRoomRates() {
            return roomRates;
        }

        public void setRoomRates(RoomRates roomRates) {
            this.roomRates = roomRates;
        }

        public String getResGuestRPHs() {
            return resGuestRPHs;
        }

        public void setResGuestRPHs(String resGuestRPHs) {
            this.resGuestRPHs = resGuestRPHs;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BasicPropertyInfo {
        @XmlAttribute(name = "HotelCode")
        private String hotelCode;

        // Getters and Setters
        public String getHotelCode() {
            return hotelCode;
        }

        public void setHotelCode(String hotelCode) {
            this.hotelCode = hotelCode;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TimeSpan {
        @XmlAttribute(name = "Start")
        private String start;

        @XmlAttribute(name = "End")
        private String end;

        // Getters and Setters
        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Guarantee {
        @XmlElement(name = "GuaranteesAccepted", namespace = "http://www.opentravel.org/OTA/2003/05")
        private GuaranteesAccepted guaranteesAccepted;

        // Getters and Setters
        public GuaranteesAccepted getGuaranteesAccepted() {
            return guaranteesAccepted;
        }

        public void setGuaranteesAccepted(GuaranteesAccepted guaranteesAccepted) {
            this.guaranteesAccepted = guaranteesAccepted;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GuaranteesAccepted {
        @XmlElement(name = "GuaranteeAccepted", namespace = "http://www.opentravel.org/OTA/2003/05")
        private GuaranteeAccepted guaranteeAccepted;

        // Getters and Setters
        public GuaranteeAccepted getGuaranteeAccepted() {
            return guaranteeAccepted;
        }

        public void setGuaranteeAccepted(GuaranteeAccepted guaranteeAccepted) {
            this.guaranteeAccepted = guaranteeAccepted;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GuaranteeAccepted {
        // Add fields if needed
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GuestCounts {
        @XmlElement(name = "GuestCount", namespace = "http://www.opentravel.org/OTA/2003/05")
        private GuestCount guestCount;

        // Getters and Setters
        public GuestCount getGuestCount() {
            return guestCount;
        }

        public void setGuestCount(GuestCount guestCount) {
            this.guestCount = guestCount;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GuestCount {
        @XmlAttribute(name = "Count")
        private String count;

        @XmlAttribute(name = "AgeQualifyingCode")
        private String ageQualifyingCode;

        // Getters and Setters
        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getAgeQualifyingCode() {
            return ageQualifyingCode;
        }

        public void setAgeQualifyingCode(String ageQualifyingCode) {
            this.ageQualifyingCode = ageQualifyingCode;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Total {
        @XmlAttribute(name = "CurrencyCode")
        private String currencyCode;

        @XmlAttribute(name = "AmountBeforeTax")
        private String amountBeforeTax;

        @XmlAttribute(name = "AmountAfterTax")
        private String amountAfterTax;

        // Getters and Setters
        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAmountBeforeTax() {
            return amountBeforeTax;
        }

        public void setAmountBeforeTax(String amountBeforeTax) {
            this.amountBeforeTax = amountBeforeTax;
        }

        public String getAmountAfterTax() {
            return amountAfterTax;
        }

        public void setAmountAfterTax(String amountAfterTax) {
            this.amountAfterTax = amountAfterTax;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomRates {
        @XmlElement(name = "RoomRate", namespace = "http://www.opentravel.org/OTA/2003/05")
        private RoomRate roomRate;

        // Getters and Setters
        public RoomRate getRoomRate() {
            return roomRate;
        }

        public void setRoomRate(RoomRate roomRate) {
            this.roomRate = roomRate;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RoomRate {
        @XmlAttribute(name = "NumberOfUnits")
        private String numberOfUnits;

        @XmlAttribute(name = "RoomTypeCode")
        private String roomTypeCode;

        @XmlAttribute(name = "RatePlanCode")
        private String ratePlanCode;

        @XmlElement(name = "Rates", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Rates rates;

        // Getters and Setters
        public String getNumberOfUnits() {
            return numberOfUnits;
        }

        public void setNumberOfUnits(String numberOfUnits) {
            this.numberOfUnits = numberOfUnits;
        }

        public String getRoomTypeCode() {
            return roomTypeCode;
        }

        public void setRoomTypeCode(String roomTypeCode) {
            this.roomTypeCode = roomTypeCode;
        }

        public String getRatePlanCode() {
            return ratePlanCode;
        }

        public void setRatePlanCode(String ratePlanCode) {
            this.ratePlanCode = ratePlanCode;
        }

        public Rates getRates() {
            return rates;
        }

        public void setRates(Rates rates) {
            this.rates = rates;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Rates {
        @XmlElement(name = "Rate", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Rate rate;

        // Getters and Setters
        public Rate getRate() {
            return rate;
        }

        public void setRate(Rate rate) {
            this.rate = rate;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Rate {
        @XmlAttribute(name = "RoomPricingType")
        private String roomPricingType;

        @XmlAttribute(name = "EffectiveDate")
        private String effectiveDate;

        @XmlAttribute(name = "ExpireDate")
        private String expireDate;

        @XmlElement(name = "Base", namespace = "http://www.opentravel.org/OTA/2003/05")
        private Base base;

        // Getters and Setters
        public String getRoomPricingType() {
            return roomPricingType;
        }

        public void setRoomPricingType(String roomPricingType) {
            this.roomPricingType = roomPricingType;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(String effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }

        public Base getBase() {
            return base;
        }

        public void setBase(Base base) {
            this.base = base;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Base {
        @XmlAttribute(name = "AmountBeforeTax")
        private String amountBeforeTax;

        // Getters and Setters
        public String getAmountBeforeTax() {
            return amountBeforeTax;
        }

        public void setAmountBeforeTax(String amountBeforeTax) {
            this.amountBeforeTax = amountBeforeTax;
        }
    }

    // Definition of the Comments class
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Comments {
        @XmlElement(name = "Comment", namespace = "http://www.opentravel.org/OTA/2003/05")
        private String comment;

        // Getters and Setters
        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
