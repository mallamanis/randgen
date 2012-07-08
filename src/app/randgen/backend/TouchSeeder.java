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

import app.randgen.R;
import android.app.Activity;
import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A SeedProvider that uses the screen to acquire the random seed
 */
public class TouchSeeder extends SeedProvider {
	private int seedSamples = 0;
	private int numSeedSamples = 100;

	@Override
	public String getDescription() {
		return "Creates random seed using input from the touchscreen";
	}

	@Override
	public String getName() {
		return "Touchscreen";
	}

	@Override
	public void getNewSeed(Activity myActivity) {
		final Dialog dialog = new Dialog(myActivity);

		dialog.setContentView(R.layout.touchseedview);
		dialog.setTitle("Seeding...");
		seedSamples = 0;
		// Register listeners
		final ProgressBar seedProgress = (ProgressBar) dialog
				.findViewById(R.id.touchSeedingProgress);
		seedProgress.setMax(numSeedSamples);
		final LinearLayout touchField = (LinearLayout) dialog
				.findViewById(R.id.touchField);

		touchField.setOnTouchListener(new OnTouchListener() {
			private float prevX = 0, prevY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (seedSamples > numSeedSamples) {
					touchField.setOnTouchListener(null);

					dialog.setTitle("Seed Generated");

					touchField.setVisibility(View.INVISIBLE);
					touchField.setMinimumHeight(0);
					TextView infoText = (TextView) dialog
							.findViewById(R.id.touchInfo);
					infoText.setText("Press Back to continue..."); // TODO: From
																	// .xml and
																	// also more
																	// user
																	// friendly
					RandGenApp.getRandomGenerator().getSeed(); // Update the
																// Generator's
																// seed!

				} else {
					seed = seed
							- Math.round((event.getX() - prevX)
									* (event.getY() - prevY));
					prevX = event.getX();
					prevY = event.getY();
					seedSamples++;
					seedProgress.setProgress(seedSamples);
				}
				return true;
			}

		});

		dialog.show();
	}
}