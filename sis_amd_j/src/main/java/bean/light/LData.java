package bean.light;

import utils.Utils;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;

import java.util.Calendar;
import java.util.List;

@RequestScoped
@ManagedBean(name = "LDBean")
public class LData {
	private String data1;

	private String data2;

	private int modo;

	private int qtd;

	private int eixo;

	private int agr;

	private List<String> modos = Utils.getModos();

	private List<String> eixos = Utils.getEixos();

	private List<String> agrs = Utils.getAgrs();

	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public int getModo() {
		return modo;
	}

	public void setModo(int modo) {
		this.modo = modo;
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

	public List<String> getModos() {
		return modos;
	}

	public void setModos(List<String> modos) {
		this.modos = modos;
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

		if (!Utils.checkData(getData1())) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A DATA 1 É INVÁLIDA!" + "", ""));
		}

		if (getModo() == 3 && !Utils.checkData(getData2())) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A DATA 2 É INVÁLIDA!" + "", ""));
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
			String[] var = getData1().split("/");
			String d1 = var[2] + var[1] + var[0];
			String d2;

			if (getModo() == 0)
				d2 = d1;
			else if (getModo() == 1) {
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH) + 1;
				int day = c.get(Calendar.DAY_OF_MONTH);

				d2 = "" + year + (month < 10 ? "0" : "") + month + (day < 10 ? "0" : "") + day;
			} else if (getModo() == 2) {
				d2 = d1;
				d1 = "00000000";
			} else {
				var = getData2().split("/");
				d2 = var[2] + var[1] + var[0];
			}

			String url = "grafico.jsf?mod=" + (getAgr() == 0 ? "0" : "1");

			url += "&agr=" + getAgr() + "&d1=" + d1 + "&d2=" + d2 + "&qtd=" + getQtd() + "&pag=0" + "&eixo="
					+ getEixo();

			try {
				context.getExternalContext().redirect(url);
			} catch (Exception ex) {

			}
		}
	}

	public void limpar() {
		setData1("");
		setData2("");
		setModo(0);
		setQtd(0);
		setEixo(0);
		setAgr(0);
	}
}
