package civilCapstone.contractB2B.citizen.controller;

import civilCapstone.contractB2B.citizen.service.CitizenService;
import civilCapstone.contractB2B.global.model.ResponseDto;
import civilCapstone.contractB2B.global.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("citizen")
// 시민 기능 컨트롤러
public class CitizenController {
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private Validator validator;

    // 시민 하청업체 조회 요청 처리
    @GetMapping("/contractor")
    public ResponseEntity<String> getContractor(@AuthenticationPrincipal String username) {
        return citizenService.getContractor(username);
    }

    // 시민 공사 조회 요청 처리
    @GetMapping("/construction")
    public ResponseEntity<String> getConstruction(@AuthenticationPrincipal String username) {
        return citizenService.getConstruction(username);
    }

}
