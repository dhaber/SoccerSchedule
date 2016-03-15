package com.fawnanddoug.soccerschedule.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fawnanddoug.soccerschedule.domain.Game;
import com.fawnanddoug.soccerschedule.service.PrinterService;

@Service
public class PipeDelimitedPrinterService implements PrinterService {

	private static Logger logger = LoggerFactory.getLogger(PipeDelimitedPrinterService.class);
	
	@Override
	public void print(List<Game> games) {
		for (Game game : games) {
			String channel = game.getChannel();
			channel = StringUtils.hasText(channel) ? channel : " ";
			logger.warn(game.getStartTime().toString("yyyy MMM-dd E hh:mm a") + "|" + game.getLeague() + "|" + game.getAway().getName() + "|"  + game.getHome().getName() + "|" + channel);			
		}
	}

}
