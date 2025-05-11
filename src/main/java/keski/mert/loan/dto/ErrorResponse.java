package keski.mert.loan.dto;

public record ErrorResponse(
        int code,
        String message
) {
}
