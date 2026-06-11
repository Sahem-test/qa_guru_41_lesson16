package models.registration.records;

import static java.lang.String.format;

public record RegistrationResponseRecordsModel(Integer id,String username,String firstName,String lastName,
                                               String email,String remoteAddr) {

}
