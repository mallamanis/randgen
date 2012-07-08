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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.app.ProgressDialog;

/**
 * A seeder that uses the accelerometer to create a random seed
 */
public class AccelerometerSeeder extends SeedProvider {
	/* {author=Miltiadis Allamanis} */

	private ProgressDialog dialog;
	private int maxSamples = 20;

	@Override
	public String getDescription() {
		return "Creates a random seed by using the accelerometer data";
	}

	@Override
	public String getName() {
		return "Accelerometer";
	}

	@Override
	public void getNewSeed(final Activity myActivity) {
		SensorManager sensMan = (SensorManager) myActivity
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor myAccelerometer = sensMan
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (myAccelerometer == null) { // No accelerator found
			Toast.makeText(myActivity, R.string.errNoAccelerometer,
					Toast.LENGTH_LONG).show();
			return;
		}

		dialog = new ProgressDialog(myActivity);
		dialog.setMax(maxSamples);
		dialog.setCancelable(true);
		dialog.setProgress(0);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle("Seeding...");
		dialog.setMessage(myActivity.getText(R.string.takeAccelSeed));
		dialog.show();

		// TODO: Open "move it" dialog

		SensorEventListener evtLst = new SensorEventListener() {
			private int sampleNo = 0;
			private long generatedSeed = 1;

			@Override
			public void onAccuracyChanged(Sensor arg0, int accuracy) {
				return;
			}

			@Override
			public void onSensorChanged(SensorEvent event) {

				if (sampleNo < maxSamples) {
					if (event.values[0] != 0 && event.values[1] != 0
							&& event.values[2] != 0) {
						sampleNo++;
						dialog.incrementProgressBy(1);
						float randNum = event.values[0] * event.values[1]
								/ event.values[2] + event.values[2]
								* (event.timestamp % 13);
						generatedSeed = (generatedSeed * Float
								.floatToRawIntBits(randNum))
								% (generatedSeed + Float
										.floatToRawIntBits(randNum));
					}
					;
				} else {
					if (generatedSeed == 0) {
						maxSamples += 10;
						dialog.setMax(maxSamples);
						return;
					}
					seed = generatedSeed;

					SensorManager sensMan = (SensorManager) myActivity
							.getSystemService(Context.SENSOR_SERVICE);
					sensMan.unregisterListener(this);

					// Close dialog
					// dialog.dismiss();
					dialog.setMessage("New seed generated!");
					RandGenApp.getRandomGenerator().getSeed(); // Update the
																// Generator's
																// seed!
				}
			}
		};

		sensMan.registerListener(evtLst, myAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}
}