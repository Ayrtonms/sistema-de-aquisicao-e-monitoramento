package grafico;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONObject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import utils.Utils;

import java.util.ArrayList;

@ManagedBean(name = "teste")
@RequestScoped
public class Teste {
	private JSONArray feeds;

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
		String jsonString = callURL("https://api.thingspeak.com/channels/201792/feeds.json?api_key=2YP59UR8MCB78VDJ");
		JSONArray jsonArray = new JSONArray("[" + jsonString + "]");
		feeds = new JSONArray(jsonArray.getJSONObject(0).get("feeds").toString());

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
			erro = "";

			JSONArray dados = new JSONArray();
			int total = 0;

			if (modo == 0 || modo == 2) {
				JSONArray data = modo == 0 ? getDados1(d1, d2, qtd, pag) : getDados1(d1, d2, h1, h2, v1, v2, qtd, pag);

				for (int i = 0; i < data.length() - 1; i++) {
					JSONObject novo = new JSONObject();
					JSONObject obj = data.getJSONObject(i);

					if (eixo == 0)
						novo.put("X", Utils.fix_data(obj.getString("field1")));
					else
						novo.put("X", obj.getString("field2"));

					novo.put("Y", obj.getInt("field3"));

					dados.put(novo);
				}
				
				total = data.getJSONObject(data.length() - 1).getInt("total");
			} else if (modo == 1 || modo == 3) {
				ArrayList<String> list = agr == 1 ? getDias(d1, d2) : getMeses(d1, d2);
				JSONArray data;
				
				if(modo == 1) {
					data = formatDados(agr == 1 ? getDados2(list) : getDados3(list), qtd, pag);
				}
				else {
					data = formatDados(agr == 1 ? getDados2(list, h1, h2, v1, v2) : getDados3(list, h1, h2, v1, v2), qtd, pag);
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
				
				if(pag > 0) 
					prev = initial + "&pag=" + (pag - 1);
				else
					prev = initial + "&pag=" + pag;
				
				if((pag + 1) * qtd < total) 
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

	public JSONArray getDados1(int d1, int d2, int h1, int h2, int v1, int v2, int qtd, int pag) {
		JSONArray list = new JSONArray();
		JSONArray result = new JSONArray();

		for (int i = 0; i < feeds.length(); i++) {
			JSONObject obj = feeds.getJSONObject(i);

			int x = Integer.parseInt(obj.getString("field1").replaceAll("-", ""));
			int y = Integer.parseInt(obj.getString("field2").replaceAll(":", ""));
			int z = obj.getInt("field3");

			if (d1 <= x && d2 >= x && h1 <= y && h2 >= y && v1 <= z && v2 >= z) {
				list.put(obj);
			}
		}

		int c1 = qtd * pag;
		int c2 = c1 + qtd;

		for (int i = 0; i < list.length(); i++) {
			if (c1 <= i && c2 > i) {
				result.put(list.getJSONObject(i));
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("total", list.length());
		
		result.put(obj);

		return result;
	}

	public JSONArray getDados1(int d1, int d2, int qtd, int pag) {
		return getDados1(d1, d2, 0, 235959, Integer.MIN_VALUE, Integer.MAX_VALUE, qtd, pag);
	}

	public ArrayList<String> getDias(int d1, int d2) {
		ArrayList<String> dias = new ArrayList<String>();

		for (int i = 0; i < feeds.length(); i++) {
			JSONObject obj = feeds.getJSONObject(i);

			String dia = obj.getString("field1").replaceAll("-", "");
			int x = Integer.parseInt(dia);

			if (d1 <= x && d2 >= x) {
				if (!dias.contains(dia))
					dias.add(dia);
			}
		}

		return dias;
	}

	public ArrayList<String> getMeses(int d1, int d2) {
		ArrayList<String> meses = new ArrayList<String>();
		
		d1 /= 100;
		d2 /= 100;

		for (int i = 0; i < feeds.length(); i++) {
			JSONObject obj = feeds.getJSONObject(i);

			String mes = obj.getString("field1").replaceAll("-", "").substring(0, 6);
			int x = Integer.parseInt(mes);

			if (d1 <= x && d2 >= x) {
				if (!meses.contains(mes)) {
					meses.add(mes);
				}
			}
		}

		return meses;
	}

	public JSONArray getDados2(ArrayList<String> dias, int h1, int h2, int v1, int v2) {
		JSONArray result = new JSONArray();

		for (int i = 0; i < dias.size(); i++) {
			String dia = dias.get(i);

			String form = dia.substring(0, 4) + "-" + dia.substring(4, 6) + "-" + dia.substring(6);
			int d1 = Integer.parseInt(dia);

			JSONObject novo = new JSONObject();
			novo.put("data", form);

			int total = 0;
			int c = 0;

			for (int j = 0; j < feeds.length(); j++) {
				JSONObject obj = feeds.getJSONObject(j);

				int d2 = Integer.parseInt(obj.getString("field1").replaceAll("-", ""));

				if (d1 == d2) {
					int y = Integer.parseInt(obj.getString("field2").replaceAll(":", ""));
					int z = obj.getInt("field3");

					if (h1 <= y && h2 >= y && v1 <= z && v2 >= z) {
						total += z;
						c++;
					}
				}
			}

			novo.put("valor", c == 0 ? 0 : total / c);

			result.put(novo);
		}

		return result;
	}

	public JSONArray getDados2(ArrayList<String> dias) {
		return getDados2(dias, 0, 235959, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public JSONArray getDados3(ArrayList<String> meses, int h1, int h2, int v1, int v2) {
		JSONArray result = new JSONArray();

		for (int i = 0; i < meses.size(); i++) {
			String mes = meses.get(i);

			String form = mes.substring(0, 4) + "-" + mes.substring(4);
			int d1 = Integer.parseInt(mes);

			JSONObject novo = new JSONObject();
			novo.put("data", form);

			int total = 0;
			int c = 0;

			for (int j = 0; j < feeds.length(); j++) {
				JSONObject obj = feeds.getJSONObject(j);

				int d2 = Integer.parseInt(obj.getString("field1").replaceAll("-", "").substring(0, 6));

				if (d1 == d2) {
					int y = Integer.parseInt(obj.getString("field2").replaceAll(":", ""));
					int z = obj.getInt("field3");

					if (h1 <= y && h2 >= y && v1 <= z && v2 >= z) {
						total += z;
						c++;
					}
				}
			}

			novo.put("valor", c == 0 ? 0 : total / c);

			result.put(novo);
		}

		return result;
	}

	public JSONArray getDados3(ArrayList<String> meses) {
		return getDados3(meses, 0, 235959, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public JSONArray formatDados(JSONArray list, int qtd, int pag) {
		JSONArray result = new JSONArray();

		int c1 = qtd * pag;
		int c2 = c1 + qtd;

		for (int i = 0; i < list.length(); i++) {
			if (c1 <= i && c2 > i) {
				JSONObject novo = new JSONObject();
				JSONObject obj = list.getJSONObject(i);

				String[] var;

				var = obj.getString("data").split("-");
				String data = var.length == 3 ? var[2] + "/" + var[1] + "/" + var[0] : var[1] + "/" + var[0];

				novo.put("data", data);
				novo.put("valor", obj.getInt("valor"));

				result.put(novo);
			}
		}

		return result;
	}

	private static String callURL(String myURL) {
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(15 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao chamar a URL:" + myURL, e);
		}

		return sb.toString();
	}
}
