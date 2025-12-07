package com.github.oscarmgh.deskflow.exceptions;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

	private int status;
	private String error;
	private String message;
	private String path;
	private OffsetDateTime timestamp;
}
