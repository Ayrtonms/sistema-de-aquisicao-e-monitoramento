package bean.temp;

import utils.Utils;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;

import java.util.List;
import java.util.Calendar;

@RequestScoped
@ManagedBean(name = "TMBean")
public class TMensal {
	private int ano;

	private int mes;

	private int qtd;

	private int eixo;

	private int agr;

	private List<String> meses = Utils.getMeses();

	private List<String> eixos = Utils.getEixos();

	private List<String> agrs = Utils.getAgrs();

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public int getEixo() {
		return eixo;
	}

	public void setEixo(int eixo) {
		this.eixo = eixo;
	}

	public int getAgr() {
		return agr;
	}

	public void setAgr(int agr) {
		this.agr = agr;
	}

	public List<String> getMeses() {
		return meses;
	}

	public void setMeses(List<String> meses) {
		this.meses = meses;
	}

	public List<String> getEixos() {
		return eixos;
	}

	public void setEixos(List<String> eixos) {
		this.eixos = eixos;
	}

	public List<String> getAgrs() {
		return agrs;
	}

	public void setAgrs(List<String> agrs) {
		this.agrs = agrs;
	}

	public void trigger() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean ok = true;

		if (getAno() > Calendar.getInstance().get(Calendar.YEAR)) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ANO INVÁLIDO!", ""));
		}

		if (getQtd() <= 0) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "QUANTIDADE INVÁLIDA!", ""));
		}

		if (getQtd() > 100) {
			ok = false;
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "QUANTIDADE ESPECIFICADA MUITO ALTA!", ""));
		}

		if (getAgr() != 0 && getEixo() != 0) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"AO AGRUPAR OS DADOS É NECESSÁRIA A DATA NO EIXO X!", ""));
		}

		if (ok) {
			int mod = (getAgr() == 0) ? 0 : 1;
			String d1 = "" + getAno() + (getMes() < 9 ? "0" : "") + (getMes() + 1) + "01";
			String d2 = "" + getAno() + (getMes() < 9 ? "0" : "") + (getMes() + 1) + "31";

			String url = "grafico.jsf?mod=" + mod + "&agr=" + getAgr() + "&d1=" + d1 + "&d2=" + d2 + "&qtd=" + getQtd()
					+ "&pag=0" + "&eixo=" + getEixo();

			try {
				context.getExternalContext().redirect(url);
			} catch (Exception ex) {

			}
		}
	}

	public void limpar() {
		setAno(0);
		setMes(0);
		setQtd(0);
		setEixo(0);
		setAgr(0);
	}
}
