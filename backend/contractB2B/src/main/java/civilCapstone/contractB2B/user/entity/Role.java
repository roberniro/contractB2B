package civilCapstone.contractB2B.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    client,
    contractor,
    admin;
}
