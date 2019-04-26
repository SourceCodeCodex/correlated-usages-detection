package main;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.ui.IStartup;
import ro.lrg.insider.view.ToolRegistration;
import ro.lrg.insider.view.ToolRegistration.XEntityConverter;
import ro.lrg.xcore.metametamodel.XEntity;
import thesis.metamodel.factory.Factory;
import upt.se.utils.TypePair;
import upt.se.utils.helpers.Converter;

public class Startup implements IStartup {

  @Override
  public void earlyStartup() {
    ToolRegistration.getInstance().registerXEntityConverter(new XEntityConverter() {

      @Override
      public XEntity convert(Object element) {
        if (element instanceof IType) {
          return Factory.getInstance().createMClass((IType) element);
        } else if (element instanceof ITypeBinding) {
          return Factory.getInstance().createMArgumentType((ITypeBinding) element);
        } else if (element instanceof TypePair) {
          return Factory.getInstance().createMTypePair((TypePair) element);
        }
        return null;
      }
    });
  }

}
