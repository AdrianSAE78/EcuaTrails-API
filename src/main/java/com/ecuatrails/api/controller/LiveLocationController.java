package com.ecuatrails.api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ecuatrails.api.dto.LocationUpdate;
import com.ecuatrails.api.service.UserService;

@Controller
public class LiveLocationController {
	private final SimpMessagingTemplate ws;
	private final UserService userService;

	public LiveLocationController(SimpMessagingTemplate ws, UserService us) {
		this.ws = ws;
		this.userService = us;
	}

	@MessageMapping("/location.update")
	public void onUpdate(LocationUpdate update, java.security.Principal principal) {
		var me = userService.findByUsername(principal.getName());
		
		boolean allowed =  true;
		if (!allowed) {
            throw new IllegalArgumentException("No autorizado");
        }
		ws.convertAndSend("/topic/route." + update.routeId(), update);
	}
}
