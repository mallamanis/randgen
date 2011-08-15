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

import gr.allamanis.randgen.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/** 
 *  A SeedProvider that get the seed from the compass data
 */
public class CompassSeeder extends SeedProvider {

	private ProgressDialog dialog; 
	private int maxSamples=20;
	
	@Override
	public String getDescription() {
		return "Creates random seed using compass data";
	}

	@Override
	public String getName() {
		return "Compass Seeding";
	}

	@Override
	public void getNewSeed(final Activity myActivity) {
		SensorManager sensMan = (SensorManager) myActivity.getSystemService(Context.SENSOR_SERVICE);
        Sensor myCompass = sensMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); 
        
        if (myCompass==null){ //If sensMan returns null
        	Toast.makeText(myActivity, R.string.errNoCompass, Toast.LENGTH_LONG).show();
        	return;
        }
        
        //TODO: Merge into a single class to use have the same iface with accelerometer or create superclass
        dialog= new ProgressDialog(myActivity);
        dialog.setMax(maxSamples);
        dialog.setCancelable(true); 
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Seeding...");
        dialog.setMessage(myActivity.getText(R.string.takeCompassSeed));
        dialog.show();
        
        //Create Listener!
        SensorEventListener evtLst = new SensorEventListener(){
        	private int sampleNo=0;        	
        	private long generatedSeed=1;
        	       	
        	@Override
			public void onAccuracyChanged(Sensor arg0, int accuracy) { return; }

			@Override
			public void onSensorChanged(SensorEvent event) {
				
				if (sampleNo<maxSamples){
					 
					if(event.values[0]!=0 && event.values[1]!=0 && event.values[2]!=0){
					 	sampleNo++;
					 	dialog.incrementProgressBy(1);
						float randNum = event.values[SensorManager.DATA_X]*event.values[SensorManager.DATA_Y]/event.values[SensorManager.DATA_Z] +event.values[2]*(event.timestamp % 13);
						generatedSeed=(generatedSeed * Float.floatToRawIntBits(randNum)) % (generatedSeed + Float.floatToRawIntBits(randNum));
					};
					
				}else{
					if (generatedSeed==0){
						maxSamples+=10;
						dialog.setMax(maxSamples);
						return;
					}
					seed =generatedSeed;
										
					SensorManager sensMan = (SensorManager) myActivity.getSystemService(Context.SENSOR_SERVICE);
					sensMan.unregisterListener(this);
					
					//Close dialog
					//dialog.dismiss();
					dialog.setMessage("New seed generated!");
					RandGenApp.getRandomGenerator().getSeed(); //Update the Generator's seed!
					
				}
				
			}
        	
        };
        
        sensMan.registerListener(evtLst, myCompass, SensorManager.SENSOR_DELAY_NORMAL);
        
		
	}

}