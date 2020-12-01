package com.ssp.platform.response;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ValidatorResponse {
	@NonNull private boolean success;
	private String field;
	@NonNull private String message;
}
