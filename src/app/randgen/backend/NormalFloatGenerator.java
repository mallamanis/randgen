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
import android.widget.EditText;
import android.widget.Toast;

/**
 * A RandomGenerator that produces float numbers following the normal
 * distribution
 */
public class NormalFloatGenerator extends RandomGenerator {
	/* {author=Miltiadis Allamanis} */

	/**
	 * the mean (mu) of the normal distribution
	 */
	private double mean;

	/**
	 * the standard deviation (sigma) of the Normal
	 */
	private double std;

	private boolean parametrized = false;

	@Override
	public String getDescription() {
		return "Produces random numbers that follow the normal distribution";
	}

	@Override
	public String getName() {
		return "Normal (Gaussian)";
	}

	@Override
	public String getNext() {
		if (parametrized)
			return Double
					.toString(mean + (std * this.generator.nextGaussian()));
		else
			return "Parameters not set";
	}

	@Override
	public int getParamsLayoutID() {
		return app.randgen.R.id.normalParams;
	}

	@Override
	public boolean setParameters(final Activity myActivity) {
		try {
			EditText edit = (EditText) myActivity
					.findViewById(app.randgen.R.id.normalMean);
			mean = Double.parseDouble(edit.getText().toString());
			edit = (EditText) myActivity
					.findViewById(app.randgen.R.id.normalStd);
			std = Double.parseDouble(edit.getText().toString());
			parametrized = true;
			// TODO: check mean and std are reasonable
			return true;
		} catch (Exception e) {
			Toast error = Toast.makeText(myActivity,
					app.randgen.R.string.paramError, Toast.LENGTH_SHORT);
			error.show();
			return false;
		}
	}

}