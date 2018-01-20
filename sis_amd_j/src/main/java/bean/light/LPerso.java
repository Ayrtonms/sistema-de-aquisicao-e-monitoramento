package bean.light;

import utils.Utils;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;

import java.util.Calendar;
import java.util.List;

@RequestScoped
@ManagedBean(name = "LPBean")
public class LPerso {
	private String data1;

	private String data2;

	private int modod;

	private String hora1;

	private String hora2;

	private int modoh;

	private double valor1;

	private double valor2;

	private int modov;

	private int qtd;

	private int eixo;

	private int agr;

	private List<String> modosd = Utils.getModosD();

	private List<String> modosh = Utils.getModosH();

	private List<String> modosv = Utils.getModosV();

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

	public int getModod() {
		return modod;
	}

	public void setModod(int modod) {
		this.modod = modod;
	}

	public String getHora1() {
		return hora1;
	}

	public void setHora1(String hora1) {
		this.hora1 = hora1;
	}

	public String getHora2() {
		return hora2;
	}

	public void setHora2(String hora2) {
		this.hora2 = hora2;
	}

	public int getModoh() {
		return modoh;
	}

	public void setModoh(int modoh) {
		this.modoh = modoh;
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

	public List<String> getModosd() {
		return modosd;
	}

	public void setModosd(List<String> modosd) {
		this.modosd = modosd;
	}

	public List<String> getModosh() {
		return modosh;
	}

	public void setModosh(List<String> modosh) {
		this.modosh = modosh;
	}

	public List<String> getModosv() {
		return modosv;
	}

	public void setModosv(List<String> modosv) {
		this.modosv = modosv;
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

		if (getModod() != 0) {
			if (!Utils.checkData(getData1())) {
				ok = false;
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "A DATA 1 É INVÁLIDA!" + "", ""));
			}

			if (getModod() == 4 && !Utils.checkData(getData2())) {
				ok = false;
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "A DATA 2 É INVÁLIDA!" + "", ""));
			}
		}

		if (getModoh() != 0) {
			if (!Utils.checkHora(getHora1())) {
				ok = false;
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "A HORA 1 É INVÁLIDA!" + "", ""));
			}

			if (getModoh() == 4 && !Utils.checkHora(getHora2())) {
				ok = false;
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "A HORA 2 É INVÁLIDA!" + "", ""));
			}
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
			String d1 = "00000000", d2;

			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);

			d2 = "" + year + (month < 10 ? "0" : "") + month + (day < 10 ? "0" : "") + day;

			if (getModod() != 0) {
				String[] var = getData1().split("/");
				d1 = var[2] + var[1] + var[0];

				if (getModod() == 1)
					d2 = d1;
				else if (getModod() == 2)
					;
				else if (getModod() == 3) {
					d2 = d1;
					d1 = "00000000";
				} else {
					var = getData2().split("/");
					d2 = var[2] + var[1] + var[0];
				}
			}

			String h1 = "000000";
			String h2 = "235959";

			if (getModoh() != 0) {
				h1 = getHora1().replaceAll(":", "");

				if (getModoh() == 1)
					h2 = h1;
				else if (getModoh() == 2)
					;
				else if (getModoh() == 3) {
					h2 = h1;
					h1 = "000000";
				} else
					h2 = getHora2().replaceAll(":", "");
			}

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

			String url = "grafico.jsf?mod=" + (getAgr() == 0 ? "2" : "3");
			url += "&agr=" + getAgr() + "&qtd=" + getQtd() + "&pag=0" + "&d1=" + d1 + "&d2=" + d2 + "&h1=" + h1 + "&h2="
					+ h2 + "&v1=" + v1 + "&v2=" + v2 + "&eixo=" + getEixo();

			try {
				context.getExternalContext().redirect(url);
			} catch (Exception ex) {

			}
		}
	}

	public void limpar() {
		setData1("");
		setData2("");
		setModod(0);
		setHora1("");
		setHora2("");
		setModoh(0);
		setValor1(0);
		setValor2(0);
		setModov(0);
		setQtd(0);
		setEixo(0);
		setAgr(0);
	}
}
