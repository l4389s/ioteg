package com.ioteg.serializers.csv;

import java.io.IOException;

import com.ioteg.resultmodel.ResultBlock;
import com.ioteg.resultmodel.ResultComplexField;
import com.ioteg.resultmodel.ResultField;
import com.ioteg.resultmodel.ResultSimpleField;

/**
 * <p>CSVResultBlockSerializer class.</p>
 *
 * @author antonio
 * @version $Id: $Id
 */
public class CSVResultBlockSerializer implements CSVSerializer<ResultBlock> {

	private static CSVResultComplexFieldSerializer csvResultComplexFieldSerializer;

	static {
		csvResultComplexFieldSerializer = new CSVResultComplexFieldSerializer();
	}

	/** {@inheritDoc} */
	@Override
	public void serialize(ResultBlock value, CSVGenerator csvGen) throws IOException {
		csvGen.writeStartObject();

		for (ResultField resultField : value.getResultFields()) {
			if (resultField instanceof ResultSimpleField) {
				ResultSimpleField resultSimpleField = (ResultSimpleField) resultField;

				StringBuilder val = new StringBuilder(resultSimpleField.getValue());
				if (resultSimpleField.getQuotes()) {
					val.insert(0, "\"");
					val.append("\"");
				}

				csvGen.writeField(resultSimpleField.getName(), val.toString());

			} else {
				ResultComplexField rCF = (ResultComplexField) resultField;
				csvResultComplexFieldSerializer.serialize(rCF, csvGen);
			}
		}

		csvGen.writeEndObject();
	}

}
