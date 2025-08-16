package com.ecuatrails.api.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.ecuatrails.api.service.JwtService;

@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtService jwtService;

	public WsConfig(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry reg) {
		reg.addEndpoint("/ws")
				.setAllowedOriginPatterns("https://tu-frontend.com", "http://localhost:*")
				.addInterceptors(new HandshakeInterceptor() {
					@Override
					public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
							WebSocketHandler wsHandler, Map<String, Object> attributes) {
						if (request instanceof ServletServerHttpRequest servletReq) {
							var httpReq = servletReq.getServletRequest();
							String auth = httpReq.getHeader("Authorization");
							if (auth != null && auth.startsWith("Bearer ")) {
								String username = jwtService.extractUsername(auth.substring(7));
								attributes.put("username", username);
							}
						}
						return true;
					}

					@Override
					public void afterHandshake(ServerHttpRequest req, ServerHttpResponse res, WebSocketHandler h,
							Exception ex) {
					}
				});
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration reg) {
		reg.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> msg, MessageChannel ch) {
				var accessor = StompHeaderAccessor.wrap(msg);
				if (accessor.getUser() == null) {
					var sessionAttrs = accessor.getSessionAttributes();
					if (sessionAttrs != null && sessionAttrs.containsKey("username")) {
						String username = (String) sessionAttrs.get("username");
						accessor.setUser(new Principal() {
							@Override
							public String getName() {
								return username;
							}
						});
					}
				}
				return msg;
			}
		});
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry conf) {
		conf.enableSimpleBroker("/topic");
		conf.setApplicationDestinationPrefixes("/app");
	}
}
