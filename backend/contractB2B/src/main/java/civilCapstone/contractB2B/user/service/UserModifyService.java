package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.user.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.Map;

public interface UserModifyService {
    public ResponseEntity<?> getResponseEntity(UserDto.UserModifyRequestDto userDto, String username);
    public Map<String, String> validateHandling(Errors errors);
}
