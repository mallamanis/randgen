/* Copyright (c) 2009 Miltiadis Allamanis
 * 
 * The MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package gr.allamanis.randgen.backend;

import java.util.Random;
import android.app.Activity;

/** 
 *  An abstract object that represents a random generator
 */
public abstract class RandomGenerator {
  /* {author=Miltiadis Allamanis}*/


  protected Random generator;

    protected SeedProvider mySeedProvider;

  public RandomGenerator(){
	generator=new Random();  
  }
  /** 
   *  returns the next random result from the generator in a String
   */
  public abstract String getNext();

  /** 
   *  gets the seed from the SeedProvider and update's the generator's by calling the SeedProvider
   */
  public final void getSeed() {
	  generator.setSeed(mySeedProvider.getCurrentSeed());
  }

  /** 
   *  presents a dialog/ activity to set the necessary parameters of the Generator
   */
  public abstract void setParameters(Activity myActivity);

  /** 
   *  returns a String containing the name of the generator
   */
  public abstract String getName();

  /** 
   *  returns a String containing the description of the generator
   */
  public abstract String getDescription();

  /** 
   *  Asks for the SeedProvider to produce a new seed, by calling it's getNewSeed()
   */
  public final void requestNewSeed(Activity myActivity) {
	  mySeedProvider.getNewSeed(myActivity);
  }

  /** 
   *  sets the SeedProvider of the Object and the Seed provider's RandomGenerator
   */
  public final void setSeedProvider(SeedProvider provider) {
	  this.mySeedProvider=provider;
	  mySeedProvider.SetMyGenerator(this);
	  if (provider.getCurrentSeed()!=0)
		  generator.setSeed(provider.getCurrentSeed());
	  
  }
  
  public final SeedProvider getMySeedProvider(){
	  return mySeedProvider;
  }

}