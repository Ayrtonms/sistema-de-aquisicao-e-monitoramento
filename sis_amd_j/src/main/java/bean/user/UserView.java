package bean.user;

import data.Data;
import data.usuarios;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import java.util.List;

@ManagedBean(name = "userView")
@RequestScoped
public class UserView {
	private Data data = Data.getInstance();

	private List<usuarios> usuarios = data.allUsuarios();

	public List<usuarios> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<usuarios> usuarios) {
		this.usuarios = usuarios;
	}

	public void delete(int id_usuario) {
		FacesContext context = FacesContext.getCurrentInstance();

		boolean result = data.deleteUsuario(id_usuario);

		if (result) {
			usuarios = data.allUsuarios();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário deletado com sucesso!", ""));
		} else
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Falha ao deletar o usuário!", ""));
	}
}
