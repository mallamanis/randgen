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


package gr.allamanis.randgen;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;


import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;



import gr.allamanis.randgen.backend.RandGenApp;
import gr.allamanis.randgen.backend.SeedProvider;





public class randomMain extends Activity {
	
	private Button setGeneratorParameters;
	private Button setSeed;
	private Button startGenerator;
	private Activity thisActivity=this;
	private Spinner selectDistribution;
	private Spinner selectSeeder;
	
	private static randomMain singleton;

    public static randomMain getInstance(){
        return singleton;
    }

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	/**Create local UI objects */
    	super.onCreate(savedInstanceState);
    	
    	singleton = this;

    	setContentView(R.layout.main);
    	
    	Eula.show(this);
    	
    	//Set Button variables
    	setGeneratorParameters=(Button) this.findViewById(R.id.setDistributionParametersButton);
    	setSeed =(Button) this.findViewById(R.id.setSeedButton);
    	startGenerator = (Button) this.findViewById(R.id.startGeneration);
    	
    	
    	
    	//Create listeners
    	
    	OnClickListener setGeneratorParametersListner=new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				if (RandGenApp.getRandomGenerator()!=null)
					RandGenApp.getRandomGenerator().setParameters(thisActivity);
				else
					Toast.makeText(thisActivity, R.string.errNoDistr, Toast.LENGTH_LONG);
			}    		
    	};
    	setGeneratorParameters.setOnClickListener(setGeneratorParametersListner);
    	
    	
    	OnClickListener setSeedListener=new OnClickListener(){
			@Override
			public void onClick(View arg0) {	
				RandGenApp.getRandomGenerator().getMySeedProvider().getNewSeed(thisActivity);
				
			}    		
    	};
    	setSeed.setOnClickListener(setSeedListener);
    	    	
    	OnClickListener startGeneratorListener=new OnClickListener(){
			@Override
			public void onClick(View arg0) {				
				if (RandGenApp.getRandomGenerator()!=null)
					if (RandGenApp.getRandomGenerator().getMySeedProvider()!=null){
							//TODO: Delete and instead start new activity
							Intent intent= new Intent();
							intent.setClassName("gr.allamanis.randgen","gr.allamanis.randgen.RandomGeneration");
							thisActivity.startActivity(intent);
							//test.setText(RandGenApp.getRandomGenerator().getNext());
							return;
					};
				Toast.makeText(thisActivity, R.string.errNoDistr, Toast.LENGTH_LONG);
				//TODO: Determine if parameters have been set and the Seeder has been seeded...
			}    		
    	};
    	startGenerator.setOnClickListener(startGeneratorListener);
    	
        
    	//Set Spinner variables
    	selectDistribution=(Spinner)this.findViewById(R.id.distrSelect);
    	selectSeeder=(Spinner)this.findViewById(R.id.seedSelect);
    	
    	//Configure spinners
    	selectDistribution.setPrompt(getText(R.string.selDistr));
    	selectSeeder.setPrompt(getText(R.string.selSeeder));
    	
    	//Configure Distribution Spinner
        ArrayAdapter<CharSequence> distributionAdapter = ArrayAdapter.createFromResource(
                this, R.array.distributionTypes, android.R.layout.simple_spinner_item);
        distributionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDistribution.setAdapter(distributionAdapter);
        
        OnItemSelectedListener distributionSelectListener=new OnItemSelectedListener(){
        	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SeedProvider oldSeeder=RandGenApp.getRandomGenerator().getMySeedProvider();
				RandGenApp.createRandomGenerator(arg2+1).setSeedProvider(oldSeeder); //The actual ID as defined by RandGenApp, is as it is sorted
				//TODO: Change to something better?
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {// TODO Do we add something here?
				
			};
        
        };
        selectDistribution.setOnItemSelectedListener(distributionSelectListener);
        
    	
        ArrayAdapter<CharSequence> seederAdapter = ArrayAdapter.createFromResource(
                this, R.array.seedTypes, android.R.layout.simple_spinner_item);
        seederAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSeeder.setAdapter(seederAdapter);
        
        OnItemSelectedListener seederSelectListener=new OnItemSelectedListener(){
        	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				RandGenApp.createSeedProvider(arg2+1); //The actual ID as defined by RandGenApp, is as it is sorted
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {// TODO Do we add something here?
				
			};
        
        };
        selectSeeder.setOnItemSelectedListener(seederSelectListener);
    	
        
        //Set defaults
        RandGenApp.createRandomGenerator(1);
        RandGenApp.createSeedProvider(1);
           
    }
  		

}