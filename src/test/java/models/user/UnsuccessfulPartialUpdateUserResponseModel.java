package models.user;


import java.util.List;

public record UnsuccessfulPartialUpdateUserResponseModel(List<String> username, List<String> firstName,
                                                         List<String>lastName,List<String>email,List<String> remoteAddr) {
}
