package grafico;

import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import utils.Utils;

@ManagedBean(name = "graficoL")
@RequestScoped
public class GraficoL {
	private LineChartModel model;

	private String erro;

	private String prev, next;

	public LineChartModel getModel() {
		return model;
	}

	public void setModel(LineChartModel model) {
		this.model = model;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public void init() {
		boolean ok = true;

		FacesContext context = FacesContext.getCurrentInstance();

		int modo = -1, agr = -1, qtd = -1, pag = -1, eixo = -1, d1 = -1, d2 = -1, h1 = -1, h2 = -1, v1 = -1, v2 = -1;

		String str1 = context.getExternalContext().getRequestParameterMap().get("mod");
		String str2 = context.getExternalContext().getRequestParameterMap().get("agr");
		String str3 = context.getExternalContext().getRequestParameterMap().get("qtd");
		String str4 = context.getExternalContext().getRequestParameterMap().get("pag");
		String str5 = context.getExternalContext().getRequestParameterMap().get("eixo");
		String str6 = context.getExternalContext().getRequestParameterMap().get("d1");
		String str7 = context.getExternalContext().getRequestParameterMap().get("d2");
		String str8 = context.getExternalContext().getRequestParameterMap().get("h1");
		String str9 = context.getExternalContext().getRequestParameterMap().get("h2");
		String str10 = context.getExternalContext().getRequestParameterMap().get("v1");
		String str11 = context.getExternalContext().getRequestParameterMap().get("v2");

		try {
			modo = Integer.parseInt(str1);
		} catch (Exception ex) {
			ok = false;
		}

		if (ok) {
			if (modo < 0 || modo > 3)
				ok = false;

			try {
				agr = Integer.parseInt(str2);
				qtd = Integer.parseInt(str3);
				pag = Integer.parseInt(str4);
				eixo = Integer.parseInt(str5);
				d1 = Integer.parseInt(str6);
				d2 = Integer.parseInt(str7);
			} catch (Exception ex) {
				ok = false;
			}

			if (modo == 2 || modo == 3) {
				try {
					h1 = Integer.parseInt(str8);
					h2 = Integer.parseInt(str9);
					v1 = Integer.parseInt(str10);
					v2 = Integer.parseInt(str11);
				} catch (Exception ex) {
					ok = false;
				}
			}
		}

		if (!ok) {
			erro = "Algum dos valores não é válido!";
		} else {
			BuscaL busca = new BuscaL();

			erro = "";

			JSONArray dados = new JSONArray();
			int total = 0;

			if (modo == 0 || modo == 2) {
				JSONArray data = modo == 0 ? busca.getDados1(d1, d2, qtd, pag)
						: busca.getDados1(d1, d2, h1, h2, v1, v2, qtd, pag);

				for (int i = 0; i < data.length() - 1; i++) {
					JSONObject novo = new JSONObject();
					JSONObject obj = data.getJSONObject(i);

					if (eixo == 0)
						novo.put("X", Utils.fix_data(obj.getString("field1")));
					else
						novo.put("X", obj.getString("field2"));

					novo.put("Y", obj.getInt("field4"));

					dados.put(novo);
				}

				total = data.getJSONObject(data.length() - 1).getInt("total");
			} else if (modo == 1 || modo == 3) {
				ArrayList<String> list = agr == 1 ? busca.getDias(d1, d2) : busca.getMeses(d1, d2);
				JSONArray data;

				if (modo == 1) {
					data = BuscaL.formatDados(agr == 1 ? busca.getDados2(list) : busca.getDados3(list), qtd, pag);
				} else {
					data = BuscaL.formatDados(
							agr == 1 ? busca.getDados2(list, h1, h2, v1, v2) : busca.getDados3(list, h1, h2, v1, v2),
							qtd, pag);
				}

				for (int i = 0; i < data.length(); i++) {
					JSONObject novo = new JSONObject();
					JSONObject obj = data.getJSONObject(i);

					novo.put("X", obj.getString("data"));
					novo.put("Y", obj.getInt("valor"));

					dados.put(novo);
				}

				total = list.size();
			}

			if (dados.length() > 0) {
				model = new LineChartModel();
				model.setTitle("Valores");
				model.setLegendPosition("e");

				CategoryAxis axis = new CategoryAxis();
				model.getAxes().put(AxisType.X, axis);

				LineChartSeries series = new LineChartSeries();
				series.setLabel("Valores");

				int min = Integer.MAX_VALUE;
				int max = Integer.MIN_VALUE;

				for (int i = 0; i < dados.length(); i++) {
					JSONObject obj = dados.getJSONObject(i);

					String x = (modo == 0 || modo == 2 ? (i + 1) + ": " : "") + obj.getString("X");
					int y = obj.getInt("Y");

					series.set(x, y);

					if (y < min)
						min = y;

					if (y > max)
						max = y;
				}

				min -= 10;
				max += 10;

				Axis yaxis = model.getAxis(AxisType.Y);
				yaxis.setMin(min);
				yaxis.setMax(max);

				model.addSeries(series);

				String initial = Utils.getURL(context.getExternalContext().getRequestParameterMap());

				if (pag > 0)
					prev = initial + "&pag=" + (pag - 1);
				else
					prev = initial + "&pag=" + pag;

				if ((pag + 1) * qtd < total)
					next = initial + "&pag=" + (pag + 1);
				else
					next = initial + "&pag=" + pag;
			} else {
				String initial = Utils.getOURL(context.getExternalContext().getRequestParameterMap());

				erro = "Nenhum registro encontrado!";
				prev = initial;
				next = initial;
			}
		}
	}
}
