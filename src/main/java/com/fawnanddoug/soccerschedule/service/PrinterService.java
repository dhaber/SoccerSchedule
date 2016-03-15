package com.fawnanddoug.soccerschedule.service;

import java.util.List;

import com.fawnanddoug.soccerschedule.domain.Game;

public interface PrinterService {
	
	public void print(List<Game> games);

}
