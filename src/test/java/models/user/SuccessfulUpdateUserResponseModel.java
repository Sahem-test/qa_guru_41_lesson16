package models.user;

public record SuccessfulUpdateUserResponseModel(int id, String username, String firstName,
                                                String lastName, String email, String remoteAddr ) {
}
