package com.ssp.platform.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class PurchasesPageRequest {

	private Integer requestPage = 0;

	private Integer numberOfElements = 10;

	public PurchasesPageRequest(Integer requestPage, Integer numberOfElements)
	{
		this.requestPage = requestPage;
		this.numberOfElements = numberOfElements;
	}
}
