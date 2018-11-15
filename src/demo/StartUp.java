package demo;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import com.salexandru.xcore.utils.interfaces.XEntity;

import exampletool.metamodel.factory.Factory;
import ro.lrg.insider.view.ToolRegistration;

public class StartUp implements org.eclipse.ui.IStartup{

	@Override
	public void earlyStartup() {
		ToolRegistration.getInstance().registerXEntityConverter(
				new ToolRegistration.XEntityConverter() {
					@Override
					public XEntity convert(Object elem) {
						if(elem instanceof IJavaElement) {
							IJavaElement element = (IJavaElement)elem;
							switch (element.getElementType()) {
								case IJavaElement.TYPE: return Factory.getInstance().createXClass((IType)element);
								case IJavaElement.COMPILATION_UNIT:
									ICompilationUnit unit = (ICompilationUnit)element;
									return Factory.getInstance().createXClass(unit.findPrimaryType());
									
							}
						}
						return null;
					}
				}
		);		
	}

}
