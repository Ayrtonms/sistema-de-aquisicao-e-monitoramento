package utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

public class Utils {
	public static List<String> getMeses() {
		List<String> meses = new ArrayList<String>();

		meses.add("Janeiro");
		meses.add("Fevereiro");
		meses.add("Março");
		meses.add("Abril");
		meses.add("Maio");
		meses.add("Junho");
		meses.add("Julho");
		meses.add("Agosto");
		meses.add("Setembro");
		meses.add("Outubro");
		meses.add("Novembro");
		meses.add("Dezembro");

		return meses;
	}

	public static List<String> getEixos() {
		List<String> eixos = new ArrayList<String>();

		eixos.add("Data");
		eixos.add("Hora");

		return eixos;
	}

	public static List<String> getAgrs() {
		List<String> agrs = new ArrayList<String>();

		agrs.add("Nenhum");
		agrs.add("Diário");
		agrs.add("Mensal");

		return agrs;
	}

	public static List<String> getModos() {
		List<String> modos = new ArrayList<String>();

		modos.add(" = Data 1 ");
		modos.add(" >= Data 1 ");
		modos.add(" <= Data 1 ");
		modos.add(" Entre as duas datas ");

		return modos;
	}

	public static List<String> getModosD() {
		List<String> modos = new ArrayList<String>();

		modos.add("Qualquer data");
		modos.add(" = Data 1 ");
		modos.add(" >= Data 1 ");
		modos.add(" <= Data 1 ");
		modos.add(" Entre as duas datas ");

		return modos;
	}

	public static List<String> getModosH() {
		List<String> modos = new ArrayList<String>();

		modos.add("Qualquer hora");
		modos.add(" = Hora 1 ");
		modos.add(" >= Hora 1 ");
		modos.add(" <= Hora 1 ");
		modos.add(" Entre as duas horas ");

		return modos;
	}

	public static List<String> getModosV() {
		List<String> modos = new ArrayList<String>();

		modos.add("Qualquer valor");
		modos.add(" = Valor 1 ");
		modos.add(" >= Valor 1 ");
		modos.add(" <= Valor 1 ");
		modos.add(" Entre os dois valores ");

		return modos;
	}

	public static boolean checkData(String data) {
		if (data.isEmpty())
			return false;

		String[] var = data.split("/");

		if (var.length != 3)
			return false;
		if (var[0].length() != 2)
			return false;
		if (var[1].length() != 2)
			return false;
		if (var[2].length() != 4)
			return false;

		int[] date = new int[3];
		date[0] = Integer.parseInt(var[0]);
		date[1] = Integer.parseInt(var[1]);
		date[2] = Integer.parseInt(var[2]);

		if (date[0] < 0 || date[0] > 31)
			return false;
		if (date[1] < 0 || date[1] > 12)
			return false;
		if (date[2] < 1000)
			return false;

		return true;
	}

	public static boolean checkHora(String hora) {
		String[] var = hora.split(":");

		if (var.length != 3)
			return false;
		if (var[0].length() != 2)
			return false;
		if (var[1].length() != 2)
			return false;
		if (var[2].length() != 2)
			return false;

		int[] hour = new int[3];
		hour[0] = Integer.parseInt(var[0]);
		hour[1] = Integer.parseInt(var[1]);
		hour[2] = Integer.parseInt(var[2]);

		if (hour[0] < 0 || hour[0] > 23)
			return false;
		if (hour[1] < 0 || hour[1] > 59)
			return false;
		if (hour[2] < 0 || hour[2] > 59)
			return false;

		return true;
	}

	public static String fix_data(String data) {
		String[] var = data.split("-");

		return var[2] + "/" + var[1] + "/" + var[0];
	}

	public static String getURL(Map<String, String> map) {
		String url = "grafico.jsf";

		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();

		boolean first = true;

		while (iterator.hasNext()) {
			String key = iterator.next();

			if (!key.equals("pag")) {
				if (first)
					url += "?";
				else
					url += "&";

				url += key + "=";

				url += map.get(key);

				first = false;
			}
		}

		return url;
	}

	public static String getOURL(Map<String, String> map) {
		String url = "grafico.jsf";

		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();

		boolean first = true;

		while (iterator.hasNext()) {
			String key = iterator.next();

			if (first)
				url += "?";
			else
				url += "&";

			url += key + "=";

			url += map.get(key);

			first = false;
		}

		return url;
	}
}
