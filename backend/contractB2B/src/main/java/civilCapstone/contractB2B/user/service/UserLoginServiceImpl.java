package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.user.entity.Role;
import civilCapstone.contractB2B.user.model.UserDto;
import civilCapstone.contractB2B.user.entity.User;
import civilCapstone.contractB2B.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
// 로그인을 위한 서비스
public class UserLoginServiceImpl implements UserLoginService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    // 비밀번호 암호화위한 encoder
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    // 로그인 처리
    public ResponseEntity<?> getResponseEntity(UserDto userDto) {
        User user = authenticateUsername(userDto.getUsername()); // username으로 사용자 찾기
        if (user == null) { // 유저가 없으면 에러
            String message = "ID가 올바르지 않습니다.";
            ResponseDto responseErrorDto = getResponseErrorDto(message);
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if (!encoder.matches(userDto.getPassword(), user.getPassword())) { // 유저가 있으면 비밀번호가 맞는지 확인, 틀리면 에러
            String message = "비밀번호가 올바르지 않습니다.";
            ResponseDto responseErrorDto = getResponseErrorDto(message);
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        final String token = tokenProvider.create(user); // 토큰 생성
        final UserDto responseUserDto = getResponseUserDto(userDto, user, token); // 유저 정보를 담은 dto 생성
        return ResponseEntity.ok().body(responseUserDto); // dto 반환
    }


    private ResponseDto getResponseErrorDto(String message) { // 에러 메시지를 담은 dto 생성
        Map<String, String> loginResult = new HashMap<>();
        loginResult.put("valid_login", message);
        ResponseDto responseDto = ResponseDto.builder().error(loginResult).build();
        return responseDto;
    }

    private UserDto getResponseUserDto(UserDto userDto, User user, String token) { // 유저 정보를 담은 dto 생성
        if (user.getRole() == Role.CITIZEN) {
            final UserDto responseUserDto = userDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .token(token)
                    .companyName(user.getName())
                    .role(user.getRole())
                    .build();
            return responseUserDto;
        }
        final UserDto responseUserDto = userDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .companyName(user.getName())
                .nip(user.getNip())
                .city(user.getAddress().getCity())
                .district(user.getAddress().getDistrict())
                .addressDetail(user.getAddress().getAddressDetail())
                .contact(user.getContact())
                .role(user.getRole())
                .build();
        return responseUserDto;
    }

    public User authenticateUsername(final String username) { // username으로 유저 찾기
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
