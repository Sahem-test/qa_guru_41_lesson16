package models.user;

public record SuccessfulUpdateUserBodyModel(String username, String firstName,
                                            String lastName, String email) {
}
