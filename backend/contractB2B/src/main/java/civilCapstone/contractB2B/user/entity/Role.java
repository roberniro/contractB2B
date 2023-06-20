package civilCapstone.contractB2B.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
// 유저 권한을 enum으로 정의
public enum Role {
    CLIENT,
    CONTRACTOR,
    CITIZEN;
}
