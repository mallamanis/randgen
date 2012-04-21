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
  
  public static final int GENERATOR_UNIFORM_INTEGER=1;
  public static final int GENERATOR_UNIFORM_FLOAT=2;
  public static final int GENERATOR_NORMAL=3;
  public static final int GENERATOR_POISSON=4;
  public static final int GENERATOR_UNIFORM_NOREPEAT=5;
  public static final int GENERATOR_PASS_PHRASE = 6;
  
  public static final int SEEDER_ACCELEROMETER=1;
  public static final int SEEDER_LINUX_RNG=2;
  public static final int SEEDER_COMPASS=3;
  public static final int SEEDER_TOUCHSCREEN=4;
  

  public static RandomGenerator createRandomGenerator(int type) {
	  switch (type){
	  	case GENERATOR_UNIFORM_INTEGER:
	  		randomGenerator=new UniformIntegerGenerator();
	  		break;
	  	case GENERATOR_UNIFORM_FLOAT:
	  		randomGenerator=new UniformFloatGenerator();
			break;
	  	case GENERATOR_NORMAL:
	  		randomGenerator=new NormalFloatGenerator();
	  		break;
	  	case GENERATOR_POISSON:
	  		randomGenerator=new PoissonIntegerGenerator();
	  		break;	  
	  	case GENERATOR_UNIFORM_NOREPEAT:
	  		randomGenerator=new UniformNonRepeatingInteger();
	  		break;
	  	case GENERATOR_PASS_PHRASE:
	  		randomGenerator =new PassPhraseGenerator();
	  		break;
	  	default:
	  		randomGenerator=new UniformIntegerGenerator();
	  		
	  }
	  return randomGenerator;
  }

  public static SeedProvider createSeedProvider(int type) {
	  if (randomGenerator==null) return null;
	  SeedProvider theSeedProvider=randomGenerator.getMySeedProvider();
	  
	  switch (type){
	  	case SEEDER_ACCELEROMETER:
	  		theSeedProvider=new AccelerometerSeeder();
	  		break;
	  	case SEEDER_LINUX_RNG:
	  		theSeedProvider=new LinuxRandomSeeder();
	  		break;
	  	case SEEDER_COMPASS:
	  		theSeedProvider= new CompassSeeder();
	  		break;
	  	case SEEDER_TOUCHSCREEN:
	  		theSeedProvider= new TouchSeeder();
	  		break;
	  	default:
	  		theSeedProvider=new AccelerometerSeeder();
	  }
	  randomGenerator.setSeedProvider(theSeedProvider);
	  return theSeedProvider;
  }

  public static RandomGenerator getRandomGenerator() {
	  return randomGenerator;
  }
}