package models.registration;

public record SuccessfulRegistrationResponseModel(Integer id, String username, String firstName, String lastName,
                                                  String email, String remoteAddr) {

}
