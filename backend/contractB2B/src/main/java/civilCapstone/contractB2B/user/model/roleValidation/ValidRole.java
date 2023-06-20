package civilCapstone.contractB2B.user.model.roleValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
@Documented
// 유저 권한 유효성 검사위한 커스텀 어노테이션
public @interface ValidRole {
    String message() default "권한을 선택해 주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}