package com.sebb77.denormalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.javafaker.Faker;
import com.google.common.collect.Table;

public class DenormalizerTest {

	private static Faker faker = Faker.instance();

	@Test
	public void denormalizeObject() throws Exception {
		// Create class structure to convert into table.
		// There is no need that the class is a serializable class. Any class will work,
		// however only lists are being checked for the moment.
		// If other types of lists needs to be checked, fix the getType function and the
		// relevant loops for the lists.
		Test1 c1 = new Test1();
		c1.setA(11);
		c1.setB("12");
		c1.setC(new Test2(21, "22", new ArrayList<String>(Arrays.asList(new String[] { "231", "232" }))));
		c1.setD(new ArrayList<String>(Arrays.asList(new String[] { "131", "132" })));
		c1.setE(new ArrayList<Test2>(Arrays.asList(
				new Test2[] { new Test2(31, "32", new ArrayList<String>(Arrays.asList(new String[] { "331", "332" }))),
						new Test2(41, "42") })));

		long t = System.currentTimeMillis();
		// convert class into a guava table
		Table<Integer, String, Object> m = Denormalizer.denormalize(c1);
		System.out.println("Total Time: " + (System.currentTimeMillis() - t) + "ms");

		// print result of conversion
		System.out.print("\t");
		for (String c : m.columnKeySet())
			System.out.print(c + "\t");
		System.out.println();

		for (Integer r : m.rowKeySet()) {
			System.out.print(r + 1 + ":\t");
			for (String c : m.columnKeySet()) {
				Object x = m.get(r, c);
				x = x == null ? "~" : x;
				System.out.print(x + "\t");
			}
			System.out.println();
		}
	}

	@Test
	public void denormalizeMap() throws Exception {

		List<Map<String, Object>> gestores = new ArrayList<>();
		Map<String, Object> gestor1 = new LinkedHashMap<>();
		gestor1.put("codigo", "G001");
		gestor1.put("nome", "Gestor 1 Pessoa Física");
		gestor1.put("cpf_cnpj", "111.111.111-11");

		List<Map<String, Object>> fundosGestor1 = new ArrayList<>();

		Map<String, Object> fundo1Gestor1 = new LinkedHashMap<>();
		fundo1Gestor1.put("codigo", "A123");
		fundo1Gestor1.put("nome", "Fundo de Investimento Abelha");
		fundo1Gestor1.put("valor_cota", faker.number().randomNumber() / 100);
		fundo1Gestor1.put("patrimonio_liquido", faker.number().randomNumber() / 100);
		fundo1Gestor1.put("data_inicio", faker.date().birthday(1, 15));
		fundosGestor1.add(fundo1Gestor1);

		Map<String, Object> fundo2Gestor1 = new LinkedHashMap<>();
		fundo2Gestor1.put("codigo", "B456");
		fundo2Gestor1.put("nome", "Fundo de Investimento Batata");
		fundo2Gestor1.put("valor_cota", faker.number().randomNumber() / 100);
		fundo2Gestor1.put("patrimonio_liquido", faker.number().randomNumber() / 100);
		fundo2Gestor1.put("data_inicio", faker.date().birthday(1, 15));
		fundosGestor1.add(fundo2Gestor1);

		Map<String, Object> fundo3Gestor1 = new LinkedHashMap<>();
		fundo3Gestor1.put("codigo", "C789");
		fundo3Gestor1.put("nome", "Fundo de Investimento Casa");
		fundo3Gestor1.put("valor_cota", faker.number().randomNumber() / 100);
		fundo3Gestor1.put("patrimonio_liquido", faker.number().randomNumber() / 100);
		fundo3Gestor1.put("data_inicio", faker.date().birthday(1, 15));
		fundosGestor1.add(fundo3Gestor1);

		gestor1.put("fundos", fundosGestor1);
		gestores.add(gestor1);

		Map<String, Object> gestor2 = new LinkedHashMap<>();
		gestor2.put("codigo", "G002");
		gestor2.put("nome", "Gestor 2 Pessoa Jurídica");
		gestor2.put("cpf_cnpj", "22.222.222/2222-22");

		List<Map<String, Object>> fundosGestor2 = new ArrayList<>();

		Map<String, Object> fundo1Gestor2 = new LinkedHashMap<>();
		fundo1Gestor2.put("codigo", "D147");
		fundo1Gestor2.put("nome", "Fundo de Investimento Dados");
		fundo1Gestor2.put("valor_cota", faker.number().randomNumber() / 100);
		fundo1Gestor2.put("patrimonio_liquido", faker.number().randomNumber() / 100);
		fundo1Gestor2.put("data_inicio", faker.date().birthday(1, 15));
		fundosGestor2.add(fundo1Gestor2);

		Map<String, Object> fundo2Gestor2 = new LinkedHashMap<>();
		fundo2Gestor2.put("codigo", "E258");
		fundo2Gestor2.put("nome", "Fundo de Investimento Escola");
		fundo2Gestor2.put("valor_cota", faker.number().randomNumber() / 100);
		fundo2Gestor2.put("patrimonio_liquido", faker.number().randomNumber() / 100);
		fundo2Gestor2.put("data_inicio", faker.date().birthday(1, 15));
		fundosGestor2.add(fundo2Gestor2);

		gestor2.put("fundos", fundosGestor2);
		gestores.add(gestor2);

		Map<String, Object> map = new LinkedHashMap<>();
		map.put("allGestores", gestores);

		long t = System.currentTimeMillis();
		// convert class into a guava table
		Table<Integer, String, Object> m = Denormalizer.denormalize(map);
		System.out.println("Total Time: " + (System.currentTimeMillis() - t) + "ms");

		// print result of conversion
		System.out.print("\t");
		for (String c : m.columnKeySet())
			System.out.print(c + "\t");
		System.out.println();

		for (Integer r : m.rowKeySet()) {
			System.out.print(r + 1 + ":\t");
			for (String c : m.columnKeySet()) {
				Object x = m.get(r, c);
				x = x == null ? "~" : x;
				System.out.print(x + "\t");
			}
			System.out.println();
		}
	}

}
