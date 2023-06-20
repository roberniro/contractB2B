package civilCapstone.contractB2B.client.controller;

import civilCapstone.contractB2B.client.service.ClientConstructionService;
import civilCapstone.contractB2B.construction.model.ConstructionDoneDto;
import civilCapstone.contractB2B.construction.model.ConstructionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("client")
// 원청 공사 관련 기능 컨트롤러
public class ClientConstructionController {
    @Autowired
    private ClientConstructionService clientConstructionService;

    // 공사 조회 요청 처리
    @GetMapping("/construction")
    public ResponseEntity getConstruction(@AuthenticationPrincipal String username) {
        return clientConstructionService.getConstruction(username);
    }

    // 공사 상태 변경 요청 처리
    @PutMapping("/construction/{constructionId}/status")
    public ResponseEntity changeConstructionStatus(@AuthenticationPrincipal String username, @PathVariable  String constructionId, @RequestBody ConstructionDto constructionDto) {
        return clientConstructionService.changeConstructionStatus(username, constructionId, constructionDto);
    }

    // 공사 완료시 평점 등록 요청 처리
    @PostMapping("/construction/{constructionId}/rating")
    public ResponseEntity doneConstruction(@AuthenticationPrincipal String username, @PathVariable String constructionId, @RequestBody ConstructionDoneDto constructionDoneDto) {
        return clientConstructionService.endConstruction(username, constructionId, constructionDoneDto);
    }
}
