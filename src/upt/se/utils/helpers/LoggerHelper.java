package upt.se.utils.helpers;

import java.util.logging.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;

public class LoggerHelper {
  public static final NullProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();
  
  public static final Logger LOGGER = Logger.getGlobal();
}
