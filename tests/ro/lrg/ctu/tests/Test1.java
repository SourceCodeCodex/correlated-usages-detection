package ro.lrg.ctu.tests;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ro.lrg.ctu.tests.util.TestUtil;
import ro.lrg.xcore.metametamodel.Group;
import thesis.metamodel.entity.MClass;
import thesis.metamodel.entity.MClassPair;
import thesis.metamodel.entity.MParameterPair;
import thesis.metamodel.entity.MProject;

class Test1 {
	
	private Group<MClass> allGenericTypes;

	private MClass findClass(String name) {
		for (MClass aClass : allGenericTypes.getElements()) {
			if (aClass.getUnderlyingObject().getFullyQualifiedName().equals(name)) {
				return aClass;
			}
		}
		return null;
	}
	
	@BeforeEach
	public void loadProject() {
		TestUtil.importProject("TestProject1", "TestProject1.zip");
		MProject prj = thesis.metamodel.factory.Factory.getInstance().createMProject(TestUtil.getProject("TestProject1").get());
		allGenericTypes = prj.allBoundedGenericTypes();
	}
	
	@AfterEach
	public void deleteProject() {
		TestUtil.deleteProject("TestProject1");
	}
	
	@Test
	void test1() {
		MClass theClass = findClass("tc1.ClassOne");
		assertNotNull("tc1.ClassOne with 2 bounded parametes must be found", theClass);
		
		Group<MParameterPair> typeParameterPairs = theClass.allTypeParameterPairs();
		assertEquals("There is one pair of bounded type parameters for tc1.ClassOne", 1, typeParameterPairs.getElements().size());
		
		MParameterPair paramPair = typeParameterPairs.getElements().get(0);
		Group<MClassPair> possibleClassPairs = paramPair.allPossibleArgumentsTypes();
		System.out.println(possibleClassPairs.getElements());
		assertEquals("(A1,B1),(A1,B2),(A2,B1),(A2,B2) pairs are the POSSIBLE concrete type pairs for tc1.ClassOne.T and tc1.ClassOne.K", 4, possibleClassPairs.getElements().size());
		
		Group<MClassPair> usedClassPairs = paramPair.usedArgumentsTypes();
		assertEquals("(A1,B1),(A2,B2) pairs are the USED concrete type pair for tc1.ClassOne.T and tc1.ClassOne.K ", 2, usedClassPairs.getElements().size());
		assertEquals(0.5, paramPair.aperture(), 0);
	}

	@Test
	void test2() {
		MClass theClass = findClass("tc2.ClassOne");
		assertNotNull("tc2.ClassOne with 2 bounded parametes must be found", theClass);
		
		Group<MParameterPair> typeParameterPairs = theClass.allTypeParameterPairs();
		assertEquals("There is one pair of bounded type parameters for tc2.ClassOne", 1, typeParameterPairs.getElements().size());
		
		MParameterPair paramPair = typeParameterPairs.getElements().get(0);
		Group<MClassPair> possibleClassPairs = paramPair.allPossibleArgumentsTypes();
		assertEquals("(A1,B1),(A1,B2),(A2,B1),(A2,B2) pairs are the POSSIBLE concrete type pair for tc2.ClassOne.T and tc2.ClassOne.K", 4, possibleClassPairs.getElements().size());
		
		Group<MClassPair> usedClassPairs = paramPair.usedArgumentsTypes();
		System.out.println(usedClassPairs.getElements());
		assertEquals("(A1,B1),(A2,B2),(A1,B2) pairs are the USED concrete type pair for for tc2.ClassOne.T and tc2.ClassOne.K ", 3, usedClassPairs.getElements().size());
		assertEquals(paramPair.aperture(), 0.75, 0);
	}

	@Test
	void test3() {
		MClass theClass = findClass("tc3.ClassOne");
		assertNotNull("tc3.ClassOne with 2 bounded parametes must be found", theClass);
		Group<MParameterPair> typeParameterPairs = theClass.allTypeParameterPairs();
		assertEquals("There is one pair of bounded type parameters for tc3.ClassOne", 1, typeParameterPairs.getElements().size());
		MParameterPair paramPair = typeParameterPairs.getElements().get(0);
		Group<MClassPair> possibleClassPairs = paramPair.allPossibleArgumentsTypes();
		assertEquals("(A1,B1),(A1,B2),(A2,B1),(A2,B2) pairs are the POSSIBLE concrete type pairs for tc3.ClassOne.T and tc3.ClassOne.K", 4, possibleClassPairs.getElements().size());
		Group<MClassPair> usedClassPairs = paramPair.usedArgumentsTypes();
		assertEquals("(A1,B1),(A2,B2) pairs are the USED concrete type pair for tc3.ClassOne.T and tc3.ClassOne.K ", 2, usedClassPairs.getElements().size());
		assertEquals(paramPair.aperture(), 0.5, 0);
	}

}
