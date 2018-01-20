package bean.temp;

import utils.Utils;

import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import java.util.List;

@RequestScoped
@ManagedBean(name = "THBean")
public class THora {
	private String data;

	private int qtd;

	private double valor1;

	private double valor2;

	private int modov;

	private List<String> modosv = Utils.getModosV();

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public double getValor1() {
		return valor1;
	}

	public void setValor1(double valor1) {
		this.valor1 = valor1;
	}

	public double getValor2() {
		return valor2;
	}

	public void setValor2(double valor2) {
		this.valor2 = valor2;
	}

	public int getModov() {
		return modov;
	}

	public void setModov(int modov) {
		this.modov = modov;
	}

	public List<String> getModosv() {
		return modosv;
	}

	public void setModosv(List<String> modosv) {
		this.modosv = modosv;
	}

	public void trigger() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean ok = true;

		if (!Utils.checkData(getData())) {
			ok = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A DATA É INVÁLIDA!" + "", ""));
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

		if (ok) {
			String[] var = getData().split("/");
			String date = var[2] + "-" + var[1] + "-" + var[0];

			double v1 = 0;
			double v2 = Integer.MAX_VALUE;

			if (getModov() != 0) {
				v1 = getValor1();

				if (getModov() == 1)
					v2 = v1;
				else if (getModov() == 2)
					;
				else if (getModov() == 3) {
					v2 = v1;
					v1 = 0;
				} else {
					v2 = getValor2();
				}
			}

			String url = "grafico.jsf?mod=4";

			url += "&date=" + date + "&qtd=" + getQtd() + "&pag=0" + "&eixo=0&v1=" + v1 + "&v2=" + v2;

			try {
				context.getExternalContext().redirect(url);
			} catch (Exception ex) {

			}
		}
	}

	public void limpar() {
		setData("");
		setModov(0);
		setQtd(0);
		setValor1(0);
		setValor2(0);
	}
}
