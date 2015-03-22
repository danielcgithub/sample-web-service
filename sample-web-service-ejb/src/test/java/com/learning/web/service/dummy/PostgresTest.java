package com.learning.web.service.dummy;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Transactional(TransactionMode.ROLLBACK)
@RunWith(Arquillian.class)
public class PostgresTest {

	@PersistenceContext(name = "test")
	private EntityManager entityManager;

	DummyUser dummyUser;

	@Before
	public void setUp() {
		dummyUser = new DummyUser();
		dummyUser.setUsername("daniel");
		entityManager.persist(dummyUser);
	}

	@Deployment
	public static WebArchive createDeployment() {
		File[] libraries = Maven.resolver().loadPomFromFile("pom.xml").resolve("postgresql:postgresql")
				.withTransitivity().asFile();

		return ShrinkWrap.create(WebArchive.class, "test.war").addAsWebInfResource("web.xml").addClass(DummyUser.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml").addAsLibraries(libraries)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Test
	public void testGetUser() {
		DummyUser testUser = entityManager.find(DummyUser.class, dummyUser.getId());
		assertEquals("daniel", testUser.getUsername());
	}

}
