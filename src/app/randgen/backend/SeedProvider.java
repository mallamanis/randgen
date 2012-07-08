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

import android.app.Activity;

/**
 * An abstract object that represents a seed getting method
 */
public abstract class SeedProvider {
	/* {author=Miltiadis Allamanis} */

	/**
	 * contains the seed that has been calculated. If nothing has been
	 * calculated then it contains 0
	 */
	protected long seed = 0;

	protected RandomGenerator myGenerator;

	public void SetMyGenerator(RandomGenerator theGenerator) {
		this.myGenerator = theGenerator;
	}

	/**
	 * returns a String containing the name of the SeedProvider
	 */
	public abstract String getName();

	/**
	 * returns the description of the seeder
	 */
	public abstract String getDescription();

	/**
	 * Takes as argument the Android Context so that it can present the
	 * appropriate dialogs (e.g. progress bars, prompts etc.). When it is
	 * finished it calls the RandomGenerator.getSeed()
	 */
	public abstract void getNewSeed(Activity myActivity);

	/**
	 * returns the long number representing the current seed
	 */
	public final long getCurrentSeed() {
		return seed;
	}

	/**
	 * returns true if the seeder has been seeded, false otherwise
	 */
	public boolean isSeeded() {
		return seed != 0;
	}
}