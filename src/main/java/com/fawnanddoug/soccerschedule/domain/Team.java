package com.fawnanddoug.soccerschedule.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.map.ObjectMapper;

public class Team {
	
	private String name;
	private String record;
	private String logoUrl;

	public Team(String name, String record, String logoUrl) {
		super();
		this.name = name;
		this.record = record;
		this.logoUrl = logoUrl;
	}

	public String getName() {
		return name;
	}

	public String getRecord() {
		return record;
	}

	public String getLogoUrl() {
		return logoUrl;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}	

}
