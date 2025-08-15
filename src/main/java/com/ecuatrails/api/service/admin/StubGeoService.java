package com.ecuatrails.api.service.admin;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.ecuatrails.api.service.AdminInterestPointService;

@Component
@Primary
public class StubGeoService implements AdminInterestPointService.GeoService {
	@Override
	public Float[] geocode(String address, String city) {
		if (address == null || city == null)
			return null;
		// Stub determinista: NO usa red. Úsalo mientras conectas un cliente real.
		// Ej: hash simple → semilla → coordenadas dentro de Ecuador.
		int h = Math.abs((address + "|" + city).hashCode());
		float lat = -4.0f + (h % 6000) / 1000f; // [-4 .. +2]
		float lng = -81.0f + ((h / 1000) % 6000) / 100f; // [-81 .. -21]
		return new Float[] { lat, lng };
	}
}
