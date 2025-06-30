package cl.lcd.dto.booking;

import lombok.Data;

import java.util.List;

@Data
public class TravelerRequestDto {
    private String id;
    private String dateOfBirth;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String email;
    private List<Phone> phones;
    private List<IdentityDocument> documents;

    @Data
    public static class Phone {
        private DeviceType deviceType;
        private String countryCallingCode;
        private String number;
    }

    @Data
    public static class IdentityDocument {
        private String number;
        private String issuanceDate;
        private String expiryDate;
        private String issuanceCountry;
        private String issuanceLocation;
        private String nationality;
        private String birthPlace;
        private DocumentType documentType;
        private String validityCountry; // two-letter ISO code of country
        private String birthCountry; // two-letter ISO code of country
        private boolean holder; // whether the document is held by the traveler
    }

    public enum Gender {
        MALE,
        FEMALE,
        UNSPECIFIED,
        UNDISCLOSED
    }

    public enum DeviceType {
        MOBILE,
        LANDLINE,
        FAX,
    }

    public enum DocumentType {
        VISA,
        PASSPORT,
        IDENTITY_CARD,
        KNOWN_TRAVELER,
        REDRESS
    }
}

