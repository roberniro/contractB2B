package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.user.model.UserDto;
import civilCapstone.contractB2B.user.repository.UserRepository;
import civilCapstone.contractB2B.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class UserModifyServiceImpl implements UserModifyService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<?> getResponseEntity(UserDto.UserModifyRequestDto userDto, String username) {
        long id;
        try { id = userRepository.findByUsername(username).get().getId(); } catch (NoSuchElementException e) {
            ResponseDto responseErrorDto = getResponseErrorDto(e);
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        User user = User.builder()
                .id(id)
                .username(username)
                .password(encoder.encode(userDto.getPassword()))
                .name(userDto.getCompanyName())
                .nip(userDto.getNip())
                .contact(userDto.getContact())
                .role(userDto.getRole())
                .build();
        Address address = Address.builder()
                .city(userDto.getCity())
                .district(userDto.getDistrict())
                .addressDetail(userDto.getAddressDetail())
                .build();
        user.setAddress(address);
        User modifiedUser;
        try {
            modifiedUser = modify(user);
        } catch (IllegalArgumentException e) {
            ResponseDto responseErrorDto = getResponseErrorDto(e);
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        final String token = tokenProvider.create(user);
        UserDto responseUserDto = getResponseUserDto(modifiedUser, token);
        return ResponseEntity.ok().body(responseUserDto);
    }

    private ResponseDto getResponseErrorDto(Exception e) {
        Map<String, String> modifyResult = new HashMap<>();
        modifyResult.put("valid_modify", e.getMessage());
        ResponseDto responseDto = ResponseDto.builder().error(modifyResult).build();
        return responseDto;
    }

    private UserDto getResponseUserDto(User registerdUser, String token) {
        UserDto responseDto = new UserDto().builder()
                .token(token)
                .id(registerdUser.getId())
                .username(registerdUser.getUsername())
                .companyName(registerdUser.getName())
                .nip(registerdUser.getNip())
                .city(registerdUser.getAddress().getCity())
                .district(registerdUser.getAddress().getDistrict())
                .addressDetail(registerdUser.getAddress().getAddressDetail())
                .contact(registerdUser.getContact())
                .role(registerdUser.getRole())
                .build();
        return responseDto;
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    @Override
    public Map<String, String> validateHandling(Errors errors) {
        // 유효성 검사, 중복 검사에 실패한 필드 목록을 받음
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public User modify(final User user) {
        // 유효성 검사
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("유저 정보가 없습니다.");
        }
        return userRepository.save(user);
    }
}
