package data;

import bean.user.*;

import data.usuarios;

import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

public class Data {
	private static final Data data = new Data();

	private static EntityManagerFactory factory;
	private static EntityManager db;

	private Data() {
		factory = Persistence.createEntityManagerFactory("sis_amd");
		db = factory.createEntityManager();
	}

	public static Data getInstance() {
		return data;
	}

	private static void close() {
		db.close();
		factory.close();
	}

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				close();
			}
		});
	}

	public List<usuarios> allUsuarios() {
		String jpql = "select x from usuarios x where x.id_usuario != 1";
		TypedQuery<usuarios> query = db.createQuery(jpql, usuarios.class);

		return query.getResultList();
	}

	public usuarios getUsuario(int id_usuario) {
		if (id_usuario == 1)
			return null;

		String jpql = "select x from usuarios x where x.id_usuario = " + id_usuario;
		TypedQuery<usuarios> query = db.createQuery(jpql, usuarios.class);

		List<usuarios> result = query.getResultList();

		if (result.size() == 0)
			return null;
		else
			return result.get(0);
	}

	public boolean deleteUsuario(int id_usuario) {
		usuarios u = getUsuario(id_usuario);

		if (u == null)
			return false;

		db.getTransaction().begin();
		db.remove(u);
		db.getTransaction().commit();

		return true;
	}

	public void inserirUsuario(UserBean bean) {
		usuarios u = new usuarios();

		u.setNome(bean.getNome());
		u.setLogin(bean.getLogin());
		u.setSenha(bean.getSenha());
		u.setAdmin(bean.isAdmin());

		db.getTransaction().begin();
		db.persist(u);
		db.getTransaction().commit();
	}

	public void atualizarUsuario(UserBean bean) {
		usuarios u = getUsuario(bean.getId_usuario());

		db.getTransaction().begin();

		u.setNome(bean.getNome());
		u.setLogin(bean.getLogin());
		u.setSenha(bean.getSenha());
		u.setAdmin(bean.isAdmin());

		db.getTransaction().commit();
	}

	public usuarios logar(String login, String senha) {
		String jpql = "select x from usuarios x where x.login = :l and x.senha = :s";

		TypedQuery<usuarios> query = db.createQuery(jpql, usuarios.class).setParameter("l", login).setParameter("s", senha);
		List<usuarios> resultado = query.getResultList();

		if (resultado.size() == 0)
			return null;
		else
			return resultado.get(0);
	}
}
