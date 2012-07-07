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

import gr.allamanis.randgen.backend.RandGenApp;
import gr.allamanis.randgen.backend.RandomGenerator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author miltiadis
 *
 */
public class RandomGeneration extends Activity {
	private TextView theGenerator;
	private TextView theSeeder;
	private Button reSeed;
	private Button generate;
	private Button copy;
	private TextView resultPlaceholder;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.generation);
    	
    	//Set info panel
    	theGenerator=(TextView)this.findViewById(R.id.distributionName);
    	theSeeder=(TextView)this.findViewById(R.id.seederName);
    	
    	theGenerator.setText(RandGenApp.getRandomGenerator().getName());
    	theSeeder.setText(RandGenApp.getRandomGenerator().getMySeedProvider().getName());
    	
    	reSeed=(Button)this.findViewById(R.id.reseedButton);
    	generate=(Button)this.findViewById(R.id.generateNext);
    	copy=(Button)this.findViewById(R.id.copyNumbers);
    	
    	resultPlaceholder=(TextView)this.findViewById(R.id.generatedNumber);
    	
    	OnClickListener reSeedListener=new OnClickListener(){
			@Override
			public void onClick(View v) {
					RandGenApp.getRandomGenerator().requestNewSeed(RandomGeneration.this);			
			}    		
    	};
    	reSeed.setOnClickListener(reSeedListener);
    	
    	OnClickListener generateListener=new OnClickListener(){
			@Override
			public void onClick(View v) {
				String result;
				if (RandGenApp.getRandomGenerator().repeating) {
					result = RandGenApp.getRandomGenerator().getNext();
				} else {
					try {
						result = RandGenApp.getRandomGenerator().getNextNonRepeating();
					} catch (RandomGenerator.OnlyRepeatsFound e) {
						result = e.getMessage();
					}
				}
				
				resultPlaceholder.setText(result+"\n"+resultPlaceholder.getText());	
			}    		
    	};
    	generate.setOnClickListener(generateListener);
    	
    	OnClickListener copyListener= new OnClickListener(){
			@Override
			public void onClick(View v) {
				ClipboardManager clip= (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
				clip.setPrimaryClip(ClipData.newPlainText("Random data from randgen", resultPlaceholder.getText()));
			}
    	};
    	copy.setOnClickListener(copyListener);
    	
	}

}
