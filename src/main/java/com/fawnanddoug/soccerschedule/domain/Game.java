package com.fawnanddoug.soccerschedule.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

public class Game {
	
	private Team home;
	private Team away;
	private String venue;
	private String league;
	private DateTime startTime;
	private String channel;
	
	
	public Game(Team home, Team away, String venue, String league,
			DateTime startTime, String channel) {
		super();
		this.home = home;
		this.away = away;
		this.venue = venue;
		this.league = league;
		this.startTime = startTime;
		this.channel = channel;
	}


	public Team getHome() {
		return home;
	}


	public Team getAway() {
		return away;
	}


	public String getVenue() {
		return venue;
	}


	public String getLeague() {
		return league;
	}


	public DateTime getStartTime() {
		return startTime;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	public String getChannel() {
		return channel;
	}	

}
