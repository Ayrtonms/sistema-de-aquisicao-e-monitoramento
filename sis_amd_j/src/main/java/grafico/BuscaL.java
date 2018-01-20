package grafico;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class BuscaL {
	private JSONArray feeds;

	public BuscaL() {
		String jsonString = callURL("https://api.thingspeak.com/channels/406317/feeds.json?api_key=ZBD3P1L1TPWIQOOE");
		JSONArray jsonArray = new JSONArray("[" + jsonString + "]");
		feeds = new JSONArray(jsonArray.getJSONObject(0).get("feeds").toString());
	}

	public JSONArray getDados1(int d1, int d2, int h1, int h2, int v1, int v2, int qtd, int pag) {
		JSONArray list = new JSONArray();
		JSONArray result = new JSONArray();

		for (int i = 0; i < feeds.length(); i++) {
			JSONObject obj = feeds.getJSONObject(i);

			int x = Integer.parseInt(obj.getString("field1").replaceAll("-", ""));
			int y = Integer.parseInt(obj.getString("field2").replaceAll(":", ""));
			int z = obj.getInt("field4");

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
					int z = obj.getInt("field4");

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
					int z = obj.getInt("field4");

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

	public static JSONArray formatDados(JSONArray list, int qtd, int pag) {
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
