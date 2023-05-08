package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.user.model.UserDto;
import civilCapstone.contractB2B.user.entity.User;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getResponseEntity(String username) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username).get();
            UserDto responseUserDto = getResponseUserDto(user);
            return ResponseEntity.ok().body(responseUserDto);
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    private static UserDto getResponseUserDto(User user) {
        UserDto responseUserDto = UserDto.builder()
                .username(user.getUsername())
                .companyName(user.getCompanyName())
                .nip(user.getNip())
                .address(user.getAddress())
                .contact(user.getContact())
                .role(user.getRole())
                .build();
        return responseUserDto;
    }
}
