package civilCapstone.contractB2B.user.service;

import org.springframework.http.ResponseEntity;

public interface UserInfoService {
    public ResponseEntity<?> getResponseEntity(String username);
}
