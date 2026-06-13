package models.login;

import java.util.List;

public record WithoutRefreshTokenLoginResponseModel(List<String> refresh) {
}
