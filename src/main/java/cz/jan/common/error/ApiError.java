package cz.jan.common.error;

import lombok.Builder;

import java.util.List;

@Builder
public record ApiError(
        String message,
        List<String> errors

) {
}
