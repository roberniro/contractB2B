package civilCapstone.contractB2B.user.controller;

import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.global.service.Validator;
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
// 회원가입, 로그인, 회원정보 조회, 회원정보 수정 요청 컨트롤러
public class UserController {
    @Autowired
    private UserJoinService userJoinService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserModifyService userModifyService;
    @Autowired
    private Validator validator;

    @PostMapping
    public ResponseEntity<?> joinUser(@Valid @RequestBody UserDto.UserJoinRequestDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            ResponseDto responseDto = ResponseDto.builder().error(validator.validateHandling(errors)).build();
            return ResponseEntity.badRequest().body(responseDto);
        }
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
