package civilCapstone.contractB2B.user.service;

import civilCapstone.contractB2B.contractor.entity.Contractor;
import civilCapstone.contractB2B.contractor.repository.ContractorRepository;
import civilCapstone.contractB2B.global.entity.Address;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.global.service.Validator;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserJoinServiceImpl implements UserJoinService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractorRepository contractorRepository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<?> getResponseEntity(UserDto.UserJoinRequestDto userDto) {
        User user = null;
        if (userDto.getRole() == Role.CITIZEN) {
            user = User.builder()
                    .username(userDto.getUsername())
                    .password(encoder.encode(userDto.getPassword()))
                    .name(userDto.getName())
                    .role(userDto.getRole())
                    .build(); // UserDto -> User
        } else {
            user = User.builder()
                    .username(userDto.getUsername())
                    .password(encoder.encode(userDto.getPassword()))
                    .name(userDto.getName())
                    .nip(userDto.getNip())
                    .contact(userDto.getContact())
                    .role(userDto.getRole())
                    .build(); // UserDto -> User
            Address address = Address.builder()
                    .city(userDto.getCity())
                    .district(userDto.getDistrict())
                    .addressDetail(userDto.getAddressDetail())
                    .build(); // UserDto -> Address
            user.setAddress(address); // User에 Address 추가
        }
        User registerdUser; // 회원가입 처리 후 결과를 받음
        // 회원가입 처리
        try {
            registerdUser = join(user);
        } catch (IllegalArgumentException e) {
            // 회원가입 실패 시, 예외 메시지를 받아서 ResponseDto에 담아 반환
            ResponseDto responseErrorDto = getResponseErrorDto(e);
            return ResponseEntity.badRequest().body(responseErrorDto);
        }
        if(registerdUser.getRole() == Role.CONTRACTOR) {
            Contractor contractor = Contractor.builder()
                    .contractor(registerdUser)
                    .build();
            contractorRepository.save(contractor);
        }
        // 회원가입 성공 시, 회원 정보를 받아서 ResponseDto에 담아 반환
        UserDto responseUserDto = getResponseUserDto(registerdUser);
        return ResponseEntity.ok().body(responseUserDto);
    }

    private ResponseDto getResponseErrorDto(IllegalArgumentException e) {
        Map<String, String> joinResult = new HashMap<>();
        joinResult.put("valid_join", e.getMessage());
        ResponseDto responseDto = ResponseDto.builder().error(joinResult).build();
        return responseDto;
    }

    private UserDto getResponseUserDto(User user) {
        if (user.getRole() == Role.CITIZEN) {
            UserDto responseUserDto = UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .companyName(user.getName())
                    .role(user.getRole())
                    .build();
            return responseUserDto;
        }
        UserDto responseUserDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
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

    // 회원가입 처리
    public User join(final User user) {
        // 유효성 검사
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("유저 정보가 없습니다.");
        }
        final String username = user.getUsername();
        // 중복 검사
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        return userRepository.save(user);
    }
}
