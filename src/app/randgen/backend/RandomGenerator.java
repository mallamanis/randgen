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

package app.randgen.backend;

import java.security.SecureRandom;
import android.app.Activity;
import java.util.TreeSet;

/**
 * An abstract object that represents a random generator
 */
public abstract class RandomGenerator {
	/* {author=Miltiadis Allamanis} */

	public class OnlyRepeatsFound extends Exception {
		private static final long serialVersionUID = 1;

		public OnlyRepeatsFound() {
			super();
		}

		public OnlyRepeatsFound(String message) {
			super(message);
		}
	}

	public boolean repeating = true;
	private TreeSet<String> previousResults;
	public static final int REPEAT_LIMIT = 100;

	protected SecureRandom generator;

	protected SeedProvider mySeedProvider;

	public RandomGenerator() {
		generator = new SecureRandom();
		previousResults = new TreeSet<String>(); // Only populated by
													// getNextNonRepeating
	}

	/**
	 * returns the next random result from the generator in a String
	 */
	public abstract String getNext();

	/**
	 * returns the next random result, running getNext up to REPEAT_LIMIT times
	 * to avoid returning the same result as any previous results from
	 * getNextNonRepeating. Raises OnlyRepeatsFound after calling getNext
	 * REPEAT_LIMIT times and only getting repeats. Previous results are cleared
	 * on new seed.
	 */
	public String getNextNonRepeating() throws OnlyRepeatsFound {
		String result;
		int count = 0;

		result = getNext();
		while (previousResults.contains(result)) {
			count++;
			if (count == RandomGenerator.REPEAT_LIMIT) {
				throw new OnlyRepeatsFound("Tried " + REPEAT_LIMIT
						+ " times and didn't find a fresh result.");
			}
			result = getNext();
		}

		previousResults.add(result);
		return result;
	}

	/**
	 * gets the seed from the SeedProvider and updates the generators by calling
	 * the SeedProvider Resets any saved results from getNextNonRepeating
	 */
	public final void getSeed() {
		generator.setSeed(mySeedProvider.getCurrentSeed());
		previousResults.clear();
	}

	/**
	 * presents a dialog/ activity to set the necessary parameters of the
	 * Generator returns false if the parameters weren't correctly set, true
	 * otherwise
	 */
	public abstract boolean setParameters(Activity myActivity);

	/**
	 * returns a String containing the short name (for display in the drop down)
	 */
	public abstract String getName();

	/**
	 * returns the id of the parameter layout associated with this generator
	 */
	public abstract int getParamsLayoutID();

	/**
	 * returns a String containing the description of the generator
	 */
	public abstract String getDescription();

	/**
	 * Asks for the SeedProvider to produce a new seed, by calling it's
	 * getNewSeed()
	 */
	public final void requestNewSeed(Activity myActivity) {
		mySeedProvider.getNewSeed(myActivity);
	}

	/**
	 * sets the SeedProvider of the Object and the Seed provider's
	 * RandomGenerator
	 */
	public final void setSeedProvider(SeedProvider provider) {
		this.mySeedProvider = provider;
		mySeedProvider.SetMyGenerator(this);
		if (provider.getCurrentSeed() != 0)
			generator.setSeed(provider.getCurrentSeed());

	}

	public final SeedProvider getMySeedProvider() {
		return mySeedProvider;
	}

}