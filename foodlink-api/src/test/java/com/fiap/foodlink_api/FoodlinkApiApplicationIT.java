package com.fiap.foodlink_api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Integracao da aplicacao com H2")
class FoodlinkApiApplicationIT {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	@DisplayName("Deve subir contexto aplicando migrations e seed no H2")
	void deveSubirContextoAplicandoMigrationsESeedNoH2() {
		Integer userTypes = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tipos_usuario", Integer.class);
		Integer restaurants = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM restaurantes", Integer.class);
		Integer menuItems = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM itens_cardapio", Integer.class);
		Integer seededRestaurant = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM restaurantes WHERE id = '88888888-8888-8888-8888-888888888888'",
				Integer.class
		);

		assertTrue(userTypes >= 2);
		assertTrue(restaurants >= 4);
		assertTrue(menuItems >= 12);
		assertTrue(seededRestaurant != null && seededRestaurant == 1);
	}
}
