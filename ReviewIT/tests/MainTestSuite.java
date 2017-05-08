import ConferenceTests.ConferenceModelTests;
import SerializationTests.SerializationTest;
import business_rules.Rule1a;
import business_rules.Rule1b;
import business_rules.Rule2a;
import business_rules.Rule2b;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
/**
 * This is the main test suite for the ReviewIt application. The purpose of this class
 * is to gather all of the unit tests that are contained in separate classes and create 
 * the ability to run them all at once.
 * @author Harlan Stewart
 * @version 1.0
 *
 */
@RunWith(Suite.class)
@SuiteClasses({Rule1a.class,Rule1b.class,Rule2a.class,Rule2b.class,ConferenceModelTests.class, SerializationTest.class,})
public class MainTestSuite {

}
