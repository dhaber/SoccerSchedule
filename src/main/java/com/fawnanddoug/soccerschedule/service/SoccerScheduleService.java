package com.fawnanddoug.soccerschedule.service;

import java.util.List;

import com.fawnanddoug.soccerschedule.domain.Game;

public interface SoccerScheduleService {

	public List<Game> getSchedule(int weeks);
	public List<Game> filterByTeams(List<Game> games, List<String> teams);
}
