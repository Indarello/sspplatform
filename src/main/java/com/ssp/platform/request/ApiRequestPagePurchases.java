package com.ssp.platform.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class ApiRequestPagePurchases {

	int requestPage = 0;

	int numberOfElements = 30;

	String SortParameter = "createDate";

	String SortType = "ascending";

	Pageable pageable;
}
