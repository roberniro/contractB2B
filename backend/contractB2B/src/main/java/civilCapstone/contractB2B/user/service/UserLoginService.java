package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.user.model.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserLoginService {
    public ResponseEntity<?> getResponseEntity(UserDto userDto);

}
