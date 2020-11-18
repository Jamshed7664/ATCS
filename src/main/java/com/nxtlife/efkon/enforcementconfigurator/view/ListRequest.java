package com.nxtlife.efkon.enforcementconfigurator.view;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class ListRequest<T> {

	public List<T> list;

	@Valid
	@NotEmpty(message = "list can't be empty")
	public List<T> getList() {
		return list;
	}

}
