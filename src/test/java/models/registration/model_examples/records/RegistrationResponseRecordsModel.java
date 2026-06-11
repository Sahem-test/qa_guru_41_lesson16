package models.registration.model_examples.records;

import static java.lang.String.format;

public record RegistrationResponseRecordsModel(Integer id,String username,String firstName,String lastName,
                                               String email,String remoteAddr) {

}
