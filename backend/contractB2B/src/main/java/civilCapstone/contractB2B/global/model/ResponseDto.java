package civilCapstone.contractB2B.global.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto<T> {
    private Map<String, String> error;
    private List<T> data;
}
