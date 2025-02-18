package com.ioteg.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ioteg.exprlang.ExprParser.ExprLangParsingException;
import com.ioteg.generation.Generable;
import com.ioteg.generation.GenerationContext;
import com.ioteg.generation.GeneratorsFactory;
import com.ioteg.generation.NotExistingGeneratorException;
import com.ioteg.model.Field;
import com.ioteg.resultmodel.ResultField;
import com.ioteg.resultmodel.ResultSimpleField;

public class AlphanumericGeneratorTestCase {

	private static final int DEFAULT_LENGTH = 10;

	@Test
	public void testRandomWithSpecifiedLengthAndEndcharacter()
			throws NotExistingGeneratorException, ExprLangParsingException, ParseException {

		Field field = new Field("cod_hex", true, "Alphanumeric");
		field.setLength(14);
		field.setEndcharacter("F");

		Generable generator = GeneratorsFactory.makeGenerator(field, new GenerationContext());

		/*
		 * <field name="cod_hex" quotes="true" type="Alphanumeric" length="14"
		 * endcharacter="F"></field>
		 */

		for (int i = 0; i < 100; ++i) {

			String strResult = ((ResultSimpleField) generator.generate(1).get(0)).getValue();
			assertTrue(strResult.matches("[0123456789ABCDEF]*"));
			assertEquals(14, strResult.length());
		}
	}

	@Test
	public void testRandomWithDefaultLengthAndLowercase()
			throws NotExistingGeneratorException, ExprLangParsingException, ParseException {

		Field field = new Field("cod_hex", true, "Alphanumeric");
		field.setCase("low");
		field.setLength(10);

		Generable generator = GeneratorsFactory.makeGenerator(field, new GenerationContext());

		/*
		 * <field name="cod_hex" quotes="true" type="Alphanumeric" length="10"
		 * case="low"></field>
		 */

		for (int i = 0; i < 100; ++i) {

			String strResult = ((ResultSimpleField) generator.generate(1).get(0)).getValue();
			assertTrue(strResult.matches("[0123456789abcdefghijklmnopqrstuvwxyz]*"));
			assertEquals(10, strResult.length());
		}
	}

	@Test
	public void testFixed() throws NotExistingGeneratorException, ExprLangParsingException, ParseException {

		Field field = new Field("cod_hex", true, "Alphanumeric");
		field.setValue("abc");

		Generable generator = GeneratorsFactory.makeGenerator(field, new GenerationContext());

		/*
		 * <field name="cod_hex" quotes="true" type="Alphanumeric" value="abc"></field>
		 */

		String strResult = ((ResultSimpleField) generator.generate(1).get(0)).getValue();
		assertEquals(String.valueOf("abc"), strResult);
	}

	@Test
	public void testRandomWithDefaultLength() throws NotExistingGeneratorException, ExprLangParsingException, ParseException {

		Field field = new Field("cod_hex", true, "Alphanumeric");

		field.setLength(DEFAULT_LENGTH);
		/* <field name="cod_hex" quotes="true" type="Alphanumeric"></field> */

		Generable generator = GeneratorsFactory.makeGenerator(field, new GenerationContext());
		List<ResultField> results = generator.generate(100);

		for (ResultField rF : results) {
			String strResult = ((ResultSimpleField) rF).getValue();
			assertTrue(strResult.matches("[0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ]{10}"));
		}
	}

	@Test
	public void testRandomWithDefaultLengthAndEndcharacter()
			throws NotExistingGeneratorException, ExprLangParsingException, ParseException {
		Field field = new Field("cod_hex", true, "Alphanumeric");

		field.setEndcharacter("F");
		field.setLength(DEFAULT_LENGTH);

		/*
		 * <field name="cod_hex" quotes="true" type="Alphanumeric"
		 * endcharacter="F"></field>
		 */

		Generable generator = GeneratorsFactory.makeGenerator(field, new GenerationContext());
		List<ResultField> results = generator.generate(100);

		for (ResultField rF : results) {
			String strResult = ((ResultSimpleField) rF).getValue();
			assertTrue(strResult.matches("[0123456789ABCDEF]{10}"));
		}
	}

	@Test
	public void testRandomWithDefaultLengthAndLow() throws NotExistingGeneratorException, ExprLangParsingException, ParseException {
		Field field = new Field("cod_hex", true, "Alphanumeric");
		field.setLength(DEFAULT_LENGTH);
		field.setCase("low");

		/*
		 * <field name="cod_hex" quotes="true" type="Alphanumeric" case="low"></field>
		 */

		Generable generator = GeneratorsFactory.makeGenerator(field, new GenerationContext());
		List<ResultField> results = generator.generate(100);

		for (ResultField rF : results) {
			String strResult = ((ResultSimpleField) rF).getValue();
			assertTrue(strResult.matches("[0123456789abcdefghijklmnopqrstuvwxyz]{10}"));
		}
	}
}
