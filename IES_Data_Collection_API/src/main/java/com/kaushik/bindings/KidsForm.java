package com.kaushik.bindings;

import java.util.List;

import lombok.Data;

@Data
public class KidsForm {

	private List<KidForm> kids;
	
	private Long caseNo;
}
