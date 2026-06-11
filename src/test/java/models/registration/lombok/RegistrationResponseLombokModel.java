package models.registration.lombok;

import lombok.Data;

import static java.lang.String.format;
@Data
public class RegistrationResponseLombokModel {
    int id;
    String username;
    String firstName;
    String lastName;
    String email;
    String remoteAddr;


}
