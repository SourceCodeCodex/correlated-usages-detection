package upt.ac.cti.util;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.zip.ZipFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

public class TestUtil {

  public static void importProject(String projectName, String fileName) {

    try {
      var url = Platform.getBundle("FamilyPolymorphismDetection").getEntry("/");
      url = FileLocator.resolve(url);
      var path = url.getPath() + "res/testdata/";
      var theFile = new ZipFile(new File(path + fileName));
      var zp = new ZipFileStructureProvider(theFile);

      var workSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      var project = workSpaceRoot.getProject(projectName);
      project.create(new NullProgressMonitor());
      project.open(new NullProgressMonitor());

      var container = workSpaceRoot.getProject(projectName).getFullPath();
      var importOp =
          new ImportOperation(container, zp.getRoot(), zp, pathString -> IOverwriteQuery.ALL);

      importOp.setCreateContainerStructure(true);
      importOp.setOverwriteResources(true);
      importOp.run(new NullProgressMonitor());
      try {
        Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);
      } catch (InterruptedException e) {
      }
      theFile.close();

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public static void deleteProject(String projectName) {
    try {
      var workSpaceRoot = ResourcesPlugin.getWorkspace().getRoot();
      var project = workSpaceRoot.getProject(projectName);
      project.close(null);
      project.delete(true, null);
    } catch (CoreException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static Optional<IJavaProject> getProject(String projectName) {
    var workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    var javaModel = JavaCore.create(workspaceRoot);
    var theProject = javaModel.getJavaProject(projectName);

    try {
      Arrays.toString(theProject.getAllPackageFragmentRoots());
    } catch (JavaModelException e) {
      throw new Error(e);
    }
    return Optional.ofNullable(theProject);

  }
}
