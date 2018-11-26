package groups.xclass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.core.search.TypeReferenceMatch;

import com.salexandru.xcore.utils.interfaces.Group;
import com.salexandru.xcore.utils.interfaces.IRelationBuilder;
import com.salexandru.xcore.utils.metaAnnotation.RelationBuilder;

import exampletool.metamodel.entity.XClass;
import exampletool.metamodel.entity.XPair;
import exampletool.metamodel.factory.Factory;
import utils.Pair;
import visitors.AttributeBindingVisitor;
import visitors.VariableBindingVisitor;

@RelationBuilder
public class GenericParametersAttributeUsagesGroup implements IRelationBuilder<XPair, XClass> {

	@Override
	public Group<XPair> buildGroup(XClass entity) {
		Group<XPair> group = new Group<>();

		List<ITypeBinding> parameters = entity.genericParametersGroup().getElements().stream()
				.map(e -> e.getUnderlyingObject()).collect(Collectors.toList());

		Map<IVariableBinding, List<ITypeBinding>> usages = findAttributeUsages(entity.getUnderlyingObject());
		findVariableUsages(entity.getUnderlyingObject());
		if (usages.entrySet().stream().anyMatch(entry -> entry.getValue().size() == parameters.size())) {
			Pair<List<ITypeBinding>, List<List<ITypeBinding>>> pair = new Pair<>(parameters,
					usages.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList()));

			group.add(Factory.getInstance().createXPair(pair));
		}
		return group;
	}

	private Map<IVariableBinding, List<ITypeBinding>> findVariableUsages(IType type) {
		final Map<IVariableBinding, List<ITypeBinding>> attributes = new HashMap<>();

		SearchPattern pattern = SearchPattern.createPattern(type,
				IJavaSearchConstants.LOCAL_VARIABLE_DECLARATION_TYPE_REFERENCE);

		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) {

				TypeReferenceMatch typeMatch = (TypeReferenceMatch) match;
				IMethod method = ((IMethod) typeMatch.getElement());
				HashSet<IVariableBinding> fields = AttributeBindingVisitor.convert(method.getCompilationUnit());
				Optional<IVariableBinding> maybeField = fields.stream()
						.filter(f -> f.getJavaElement().getElementName().equals(method.getElementName())).findFirst();

				maybeField.ifPresent(
						attribute -> attributes.put(attribute, Arrays.asList(attribute.getType().getTypeArguments())));
			}
		};

		SearchEngine searchEngine = new SearchEngine();

		try {
			searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
					requestor, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return attributes;
	}

	private Map<IVariableBinding, List<ITypeBinding>> findAttributeUsages(IType type) {
		final Map<IVariableBinding, List<ITypeBinding>> attributes = new HashMap<>();

		SearchPattern pattern = SearchPattern.createPattern(type, IJavaSearchConstants.FIELD_DECLARATION_TYPE_REFERENCE
				| IJavaSearchConstants.LOCAL_VARIABLE_DECLARATION_TYPE_REFERENCE);

		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {
			public void acceptSearchMatch(SearchMatch match) {

				TypeReferenceMatch typeMatch = (TypeReferenceMatch) match;
				Optional<ICompilationUnit> maybeCompilationUnit = Optional.empty();
				if (typeMatch.getElement() instanceof IField) {
					maybeCompilationUnit = Optional.of(((IField) typeMatch.getElement()).getCompilationUnit());
				} else if (typeMatch.getElement() instanceof IMethod) {
					maybeCompilationUnit = Optional.of(((IMethod) typeMatch.getElement()).getCompilationUnit());
				}

				maybeCompilationUnit.ifPresent(compilationUnit -> {
					HashSet<IVariableBinding> variables = AttributeBindingVisitor.convert(compilationUnit);

					variables.forEach(variable -> {
						if (!attributes.containsKey(variable)) {
							attributes.put(variable, Arrays.asList(variable.getType().getTypeArguments()));
						}
					});

					variables = VariableBindingVisitor.convert(compilationUnit);
					variables.forEach(variable -> {
						if (!attributes.containsKey(variable)) {
							attributes.put(variable, Arrays.asList(variable.getType().getTypeArguments()));
						}
					});
				});
			}
		};

		SearchEngine searchEngine = new SearchEngine();

		try {
			searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
					requestor, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return attributes;
	}

}
