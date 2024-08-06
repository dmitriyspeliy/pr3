package com.effectivemobile.practice3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"local","default"})
// need connect to db
class Practice3ApplicationTests {

	@Test
	void contextLoads() {
	}

}
