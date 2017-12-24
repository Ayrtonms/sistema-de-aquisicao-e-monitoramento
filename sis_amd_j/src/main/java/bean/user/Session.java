package bean.user;

import data.Data;
import data.usuarios;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class Session {
	private Data data = Data.getInstance();

	private String login;

	private String senha;

	private boolean logado = false;

	private boolean admin = false;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void logar() {
		FacesContext context = FacesContext.getCurrentInstance();

		boolean ok = true;

		if (login == null) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login inválido!", ""));
		}

		if (senha == null) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha inválida!", ""));
		}

		if (ok) {
			usuarios u = data.logar(login, senha);

			if (u == null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acesso negado!", ""));
			} else {
				logado = true;
				admin = u.isAdmin();

				try {
					context.getExternalContext().redirect("index.jsf");
				} catch (Exception ex) {

				}
			}
		}
	}

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();

		setLogin("");
		setSenha("");
		logado = false;
		admin = false;

		try {
			context.getExternalContext().redirect("/sis_amd_j/login.jsf");
		} catch (Exception ex) {

		}
	}

	public void checkLogado() {
		if (!logado) {
			FacesContext context = FacesContext.getCurrentInstance();

			try {
				context.getExternalContext().redirect("/sis_amd_j/login.jsf");
			} catch (Exception ex) {

			}
		}
	}

	public void checkAdmin() {
		if (!admin) {
			FacesContext context = FacesContext.getCurrentInstance();

			try {
				context.getExternalContext().redirect("/sis_amd_j/403.jsf");
			} catch (Exception ex) {

			}
		}
	}
}
