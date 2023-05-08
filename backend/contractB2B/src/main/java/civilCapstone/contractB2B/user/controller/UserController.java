package civilCapstone.contractB2B.user.controller;

import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.user.model.UserDto;
import civilCapstone.contractB2B.user.service.UserJoinService;
import civilCapstone.contractB2B.user.service.UserLoginService;
import civilCapstone.contractB2B.user.service.UserModifyService;
import civilCapstone.contractB2B.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserJoinService userJoinService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserModifyService userModifyService;

    @PostMapping
    public ResponseEntity<?> joinUser(@Valid @RequestBody UserDto.UserJoinRequestDto userDto, Errors errors) {
        // 검증 결과 에러가 있으면
        if (errors.hasErrors()) {
            // 에러 메시지를 담은 ResponseDto를 반환
            ResponseDto responseDto = ResponseDto.builder().error(userJoinService.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        // 에러가 없으면
        // 회원가입 서비스를 통해 회원가입을 시도하고, 결과를 반환
        return userJoinService.getResponseEntity(userDto);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        return userLoginService.getResponseEntity(userDto);
    }

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal String username) {
        return userInfoService.getResponseEntity(username);
    }

    @PutMapping
    public ResponseEntity<?> modifyUser(@AuthenticationPrincipal String username, @Valid @RequestBody UserDto.UserModifyRequestDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(userModifyService.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        return userModifyService.getResponseEntity(userDto, username);
    }
}
