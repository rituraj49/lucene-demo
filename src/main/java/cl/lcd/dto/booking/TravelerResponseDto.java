package cl.lcd.dto.booking;

import lombok.Data;

import java.util.List;

@Data
public class TravelerResponseDto {
    private String id;
    private String dateOfBirth;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String email;
    private List<Phone> phones;
    private List<IdentityDocumentResponse> documents;

    @Data
    public static class Phone {
        private DeviceType deviceType;
        private String countryCallingCode;
        private String number;
    }

    @Data
    public static class IdentityDocumentResponse {
        private String number;
        private String expiryDate;
        private String issuanceCountry;
        private String nationality;
        private DocumentType documentType;
        private boolean holder;
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

