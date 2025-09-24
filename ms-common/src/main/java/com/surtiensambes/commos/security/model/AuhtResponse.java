package com.surtiensambes.commos.security.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@JsonPropertyOrder({"username","message","jwt","state"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuhtResponse {
	private String userName;
	private String message;
	private String jwt;
	private Boolean state;

}
