package data;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;

public class CreateDB {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("sis_amd");
		EntityManager db = factory.createEntityManager();

		usuarios u = new usuarios();
		u.setNome("Administrador");
		u.setLogin("admin");
		u.setSenha("123");
		u.setAdmin(true);

		db.getTransaction().begin();
		db.persist(u);
		db.getTransaction().commit();

		db.close();
		factory.close();
	}
}
