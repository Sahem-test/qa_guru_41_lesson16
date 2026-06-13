package models.logout;

import java.util.List;

public record WithoutRefreshTokenLogoutResponseModel(List<String> refresh) {
}
