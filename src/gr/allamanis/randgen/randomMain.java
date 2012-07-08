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


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;

import gr.allamanis.randgen.backend.AccelerometerSeeder;
import gr.allamanis.randgen.backend.CompassSeeder;
import gr.allamanis.randgen.backend.LinuxRandomSeeder;
import gr.allamanis.randgen.backend.RandGenApp;
import gr.allamanis.randgen.backend.RandomGenerator;
import gr.allamanis.randgen.backend.SeedProvider;
import gr.allamanis.randgen.backend.TouchSeeder;
import gr.allamanis.randgen.backend.UniformIntegerGenerator;
import gr.allamanis.randgen.backend.UniformFloatGenerator;
import gr.allamanis.randgen.backend.PoissonIntegerGenerator;
import gr.allamanis.randgen.backend.NormalFloatGenerator;
import gr.allamanis.randgen.backend.PassPhraseGenerator;

public class randomMain extends Activity {
	private Button setSeed;
	private Button startGenerator;
	private Activity thisActivity=this;
	private Spinner selectDistribution;
	private Spinner selectSeeder;
	
	private static randomMain singleton;
	private int currentLayout;

    public static randomMain getInstance(){
        return singleton;
    }

    private ArrayList<RandomGenerator> Generators;
    private ArrayList<SeedProvider> Seeders;
    
    // index into arrays - used for saving preferences
    private int generatorNum = -1; 
    private int seederNum = -1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	/**Create local UI objects */
    	super.onCreate(savedInstanceState);
    	
    	singleton = this;

    	setContentView(R.layout.main);
    	
    	Eula.show(this);
    	
    	// Populate generators
    	Generators = new ArrayList<RandomGenerator>();
    	Generators.add(new UniformIntegerGenerator());
    	Generators.add(new UniformFloatGenerator());
    	Generators.add(new NormalFloatGenerator());
    	Generators.add(new PoissonIntegerGenerator());
    	Generators.add(new PassPhraseGenerator());
    	
    	// Populate seeders
    	Seeders = new ArrayList<SeedProvider>();
    	Seeders.add(new LinuxRandomSeeder());
    	Seeders.add(new AccelerometerSeeder());
    	Seeders.add(new CompassSeeder());
    	Seeders.add(new TouchSeeder());
    	
    	//Set Button variables
    	setSeed =(Button) this.findViewById(R.id.setSeedButton);
    	startGenerator = (Button) this.findViewById(R.id.startGeneration);
    	final CheckBox chkRepeats = (CheckBox) findViewById(R.id.chkAllowRepeats); 
    	
    	//Create listeners
    	
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
				if (RandGenApp.getRandomGenerator()!=null) { // should never fail
					if (RandGenApp.getRandomGenerator().getMySeedProvider()!=null){ // should never fail
						if (RandGenApp.getRandomGenerator().getMySeedProvider().isSeeded()) {
							RandGenApp.getRandomGenerator().repeating = chkRepeats.isChecked();
							if(RandGenApp.getRandomGenerator().setParameters(singleton)) {
								//TODO: Delete and instead start new activity
								Intent intent= new Intent();
								intent.setClassName("gr.allamanis.randgen","gr.allamanis.randgen.RandomGeneration");
								thisActivity.startActivity(intent);
								return;
							}  // else - setParameters failed and will have created a toast
						} else {
							Toast error = Toast.makeText(singleton, "Please seed first.", Toast.LENGTH_SHORT);
							error.show();
						}
					};
				}
				//TODO: Determine if the Seeder has been seeded...
			}
    	};
    	startGenerator.setOnClickListener(startGeneratorListener);
        
    	//Set Spinner variables
    	selectDistribution=(Spinner)this.findViewById(R.id.distrSelect);
    	selectSeeder=(Spinner)this.findViewById(R.id.seedSelect);
    	
    	//Configure spinners
    	selectDistribution.setPrompt(getText(R.string.selDistr));
    	selectSeeder.setPrompt(getText(R.string.selSeeder));
    	
    	// Fill Distribution Spinner with the name of each generator in Generators
        ArrayAdapter<CharSequence> distributionAdapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item);
        distributionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (RandomGenerator g : Generators) {
    		distributionAdapter.add(g.getName());
    	}
        selectDistribution.setAdapter(distributionAdapter);
        
        OnItemSelectedListener distributionSelectListener=new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				generatorNum = arg2; // for prefs
				
				SeedProvider currentSeeder=RandGenApp.getRandomGenerator().getMySeedProvider();
				RandGenApp.setRandomGenerator(Generators.get(arg2)).setSeedProvider(currentSeeder);
				
				singleton.findViewById(currentLayout).setVisibility(View.GONE);
				currentLayout = Generators.get(arg2).getParamsLayoutID();
				singleton.findViewById(currentLayout).setVisibility(View.VISIBLE);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {// TODO Do we add something here?
				
			};
        };
        selectDistribution.setOnItemSelectedListener(distributionSelectListener);
    	
        // Fill Seeder Spinner with the name of each seeder in Seeders
        ArrayAdapter<CharSequence> seederAdapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item);
        seederAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (SeedProvider s : Seeders) {
    		seederAdapter.add(s.getName());
    	}
        selectSeeder.setAdapter(seederAdapter);
        
        OnItemSelectedListener seederSelectListener=new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				seederNum = arg2;
				RandGenApp.getRandomGenerator().setSeedProvider(Seeders.get(arg2)); //The actual ID as defined by RandGenApp, is as it is sorted
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {// TODO Do we add something here?
				
			};
        };
        selectSeeder.setOnItemSelectedListener(seederSelectListener);
        
        // Restore previous session / set defaults
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
    	generatorNum = settings.getInt("generator", 0); // default generator = 0
    	selectDistribution.setSelection(generatorNum);
    	seederNum = settings.getInt("seeder", 0);
    	selectSeeder.setSelection(seederNum);
    	RandGenApp.setRandomGenerator(Generators.get(generatorNum)).setSeedProvider(Seeders.get(seederNum));
    	((CheckBox) findViewById(R.id.chkAllowRepeats)).setChecked(settings.getBoolean("allowRepeats", true));
    	
        currentLayout = RandGenApp.getRandomGenerator().getParamsLayoutID(); // the spinner callback will make this view visible
    }
    
    @Override
    protected void onStop(){
    	super.onStop();

    	SharedPreferences settings = getPreferences(MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	editor.putBoolean("allowRepeats", ((CheckBox) findViewById(R.id.chkAllowRepeats)).isChecked() );
    	editor.putInt("generator", generatorNum);
    	editor.putInt("seeder", seederNum);
    	
    	editor.commit();
	}
}