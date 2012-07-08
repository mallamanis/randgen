package gr.allamanis.randgen.backend;

/** 
 *  Utility class that allows to store the static (and only) RandomGenerator to be accessed throughout the whole application
 */
public class RandGenApp {
  /* {author=Miltiadis Allamanis}*/


  /** 
   *  this static variable contains the random generator the application is using
   */
  private static RandomGenerator randomGenerator;
  
  public static RandomGenerator setRandomGenerator(RandomGenerator g) {
	  randomGenerator = g;
	  return randomGenerator;
  }

  public static RandomGenerator getRandomGenerator() {
	  return randomGenerator;
  }
}