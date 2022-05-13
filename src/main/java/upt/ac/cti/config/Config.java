package upt.ac.cti.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

// Writing binding inconclusive for unknown reason: Writing [element=variables,
// writingExpression=parentObject, accessExpression=Left(Database), depth=1,
// binding=org.pentaho.di.core.logging.LoggingObjectInterface]
// Writing binding inconclusive for unknown reason: Writing [element=variables,
// writingExpression=parentObject, accessExpression=Left(Database), depth=1,
// binding=org.pentaho.di.core.logging.LoggingObjectInterface]
// Writing binding inconclusive for unknown reason: Writing [element=variableSpace,
// writingExpression=meta, accessExpression=Right(delegates.partitions), depth=3,
// binding=org.pentaho.di.core.EngineMetaInterface]
// Writing binding inconclusive for unknown reason: Writing [element=variables,
// writingExpression=parentObject, accessExpression=Right(databaseMeta), depth=2,
// binding=org.pentaho.di.core.logging.LoggingObjectInterface]
// Writing binding inconclusive for unknown reason: Writing [element=baseStepMeta,
// writingExpression=baseStepMeta, accessExpression=Left(BaseStepDialog), depth=0,
// binding=org.pentaho.di.trans.step.BaseStepMeta]

/*
 * No derivation is possible for Writing [element=varDisabledListener, writingExpression=rowNr -> {
 * paramRowNum=rowNr; paramColNum=PARAM_COL_1; return false; } ,
 * accessExpression=Left(ParameterTableHelper), depth=0,
 * binding=org.pentaho.di.ui.core.widget.FieldDisabledListener]. Writing expression type is 86
 */

public class Config {
	public final int MAX_DEPTH_THRESHOLD;
	public final int MIN_HIERARCHY_SIZE;
	public final int MAX_DEPTH_DIFF;
	public final int CLASS_ANALYSIS_PARALLELISM;

	public Config() {

		var config = new Properties();

		try {
			var fis = getClass().getClassLoader().getResourceAsStream("../resources/config.properties");

			config.load(fis);

			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MAX_DEPTH_THRESHOLD = Integer
				.parseInt(Optional.ofNullable(config.getProperty("MAX_DEPTH_THRESHOLD")).orElse("1"));
		MIN_HIERARCHY_SIZE = Integer
				.parseInt(Optional.ofNullable(config.getProperty("MIN_HIERARCHY_SIZE")).orElse("1"));
		MAX_DEPTH_DIFF = Integer.parseInt(Optional.ofNullable(config.getProperty("MAX_DEPTH_DIFF")).orElse("2"));
		CLASS_ANALYSIS_PARALLELISM = Integer
				.parseInt(Optional.ofNullable(config.getProperty("CLASS_ANALYSIS_PARALLELISM")).orElse("4"));

	}

	public Map<String, String> asStrings() {
		var map = new HashMap<String, String>();

		map.put("MAX_DEPTH_THRESHOLD", "" + MAX_DEPTH_THRESHOLD);
		map.put("MIN_HIERARCHY_SIZE", "" + MIN_HIERARCHY_SIZE);
		map.put("MAX_DEPTH_DIFF", "" + MAX_DEPTH_DIFF);
		map.put("CLASS_ANALYSIS_PARALLELISM", "" + CLASS_ANALYSIS_PARALLELISM);

		return map;
	}

}
