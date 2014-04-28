/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * PrimitiveArrayToObjectArrayTest.java
 * Copyright (C) 2013 University of Waikato, Hamilton, New Zealand
 */

package adams.flow.transformer;

import junit.framework.Test;
import junit.framework.TestSuite;
import adams.core.option.AbstractArgumentOption;
import adams.env.Environment;
import adams.flow.AbstractFlowTest;
import adams.flow.control.Flow;
import adams.flow.core.AbstractActor;
import adams.test.TmpFile;

/**
 * Test for PrimitiveArrayToObjectArray actor.
 *
 * @author fracpete
 * @author adams.core.option.FlowJUnitTestProducer (code generator)
 * @version $Revision$
 */
public class PrimitiveArrayToObjectArrayTest
  extends AbstractFlowTest {

  /**
   * Initializes the test.
   *
   * @param name	the name of the test
   */
  public PrimitiveArrayToObjectArrayTest(String name) {
    super(name);
  }

  /**
   * Called by JUnit before each test method.
   *
   * @throws Exception 	if an error occurs.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
    m_TestHelper.deleteFileFromTmp("dumpfile.txt");
  }

  /**
   * Called by JUnit after each test method.
   *
   * @throws Exception	if tear-down fails
   */
  @Override
  protected void tearDown() throws Exception {
    m_TestHelper.deleteFileFromTmp("dumpfile.txt");
    
    super.tearDown();
  }

  /**
   * Performs a regression test, comparing against previously generated output.
   */
  public void testRegression() {
    performRegressionTest(
        new TmpFile[]{
          new TmpFile("dumpfile.txt")
        });
  }

  /**
   * 
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(PrimitiveArrayToObjectArrayTest.class);
  }

  /**
   * Used to create an instance of a specific actor.
   *
   * @return a suitably configured <code>AbstractActor</code> value
   */
  @Override
  public AbstractActor getActor() {
    AbstractArgumentOption    argOption;
    
    Flow flow = new Flow();
    
    try {
      argOption = (AbstractArgumentOption) flow.getOptionManager().findByProperty("actors");
      adams.flow.core.AbstractActor[] abstractactor1 = new adams.flow.core.AbstractActor[6];

      // Flow.StringConstants
      adams.flow.source.StringConstants stringconstants2 = new adams.flow.source.StringConstants();
      stringconstants2.setOutputArray(true);

      argOption = (AbstractArgumentOption) stringconstants2.getOptionManager().findByProperty("strings");
      adams.core.base.BaseString[] basestring3 = new adams.core.base.BaseString[5];
      basestring3[0] = (adams.core.base.BaseString) argOption.valueOf("1");
      basestring3[1] = (adams.core.base.BaseString) argOption.valueOf("2");
      basestring3[2] = (adams.core.base.BaseString) argOption.valueOf("3");
      basestring3[3] = (adams.core.base.BaseString) argOption.valueOf("4");
      basestring3[4] = (adams.core.base.BaseString) argOption.valueOf("5");
      stringconstants2.setStrings(basestring3);

      abstractactor1[0] = stringconstants2;

      // Flow.ArrayProcess
      adams.flow.control.ArrayProcess arrayprocess4 = new adams.flow.control.ArrayProcess();
      argOption = (AbstractArgumentOption) arrayprocess4.getOptionManager().findByProperty("actors");
      adams.flow.core.AbstractActor[] abstractactor5 = new adams.flow.core.AbstractActor[1];

      // Flow.ArrayProcess.Convert
      adams.flow.transformer.Convert convert6 = new adams.flow.transformer.Convert();
      argOption = (AbstractArgumentOption) convert6.getOptionManager().findByProperty("conversion");
      adams.data.conversion.StringToInt stringtoint8 = new adams.data.conversion.StringToInt();
      convert6.setConversion(stringtoint8);

      abstractactor5[0] = convert6;
      arrayprocess4.setActors(abstractactor5);

      argOption = (AbstractArgumentOption) arrayprocess4.getOptionManager().findByProperty("arrayClass");
      arrayprocess4.setArrayClass((java.lang.String) argOption.valueOf("java.lang.Integer"));

      abstractactor1[1] = arrayprocess4;

      // Flow.ObjectArrayToPrimitiveArray
      adams.flow.transformer.ObjectArrayToPrimitiveArray objectarraytoprimitivearray10 = new adams.flow.transformer.ObjectArrayToPrimitiveArray();
      abstractactor1[2] = objectarraytoprimitivearray10;

      // Flow.PrimitiveArrayToObjectArray
      adams.flow.transformer.PrimitiveArrayToObjectArray primitivearraytoobjectarray11 = new adams.flow.transformer.PrimitiveArrayToObjectArray();
      abstractactor1[3] = primitivearraytoobjectarray11;

      // Flow.ArrayToSequence
      adams.flow.transformer.ArrayToSequence arraytosequence12 = new adams.flow.transformer.ArrayToSequence();
      abstractactor1[4] = arraytosequence12;

      // Flow.DumpFile
      adams.flow.sink.DumpFile dumpfile13 = new adams.flow.sink.DumpFile();
      argOption = (AbstractArgumentOption) dumpfile13.getOptionManager().findByProperty("outputFile");
      dumpfile13.setOutputFile((adams.core.io.PlaceholderFile) argOption.valueOf("${TMP}/dumpfile.txt"));

      dumpfile13.setAppend(true);

      abstractactor1[5] = dumpfile13;
      flow.setActors(abstractactor1);

      argOption = (AbstractArgumentOption) flow.getOptionManager().findByProperty("flowExecutionListener");
      adams.flow.execution.NullListener nulllistener16 = new adams.flow.execution.NullListener();
      flow.setFlowExecutionListener(nulllistener16);

    }
    catch (Exception e) {
      fail("Failed to set up actor: " + e);
    }
    
    return flow;
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    Environment.setEnvironmentClass(adams.env.Environment.class);
    runTest(suite());
  }
}

