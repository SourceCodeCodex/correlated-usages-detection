package upt.se.utils.crawlers;

import static upt.se.utils.helpers.Equals.isEqual;

import org.eclipse.jdt.core.dom.ITypeBinding;

import io.vavr.collection.List;

public abstract class Crawler {

	public static int getParameterNumber(ITypeBinding actualParameter) {
		return List.of(actualParameter.getDeclaringClass().getTypeParameters()).zipWithIndex()
				.filter(parameter -> isEqual(parameter._1, actualParameter)).map(parameter -> parameter._2).head();
	}

}
