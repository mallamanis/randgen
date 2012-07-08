/* Copyright (c) 2012 Michael Macnair
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

import gr.allamanis.randgen.randomMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.res.AssetManager;
import android.widget.Toast;
import android.widget.EditText;

/** 
 *  A RandomGenerator that produces pass phrases
 */
public class PassPhraseGenerator extends RandomGenerator {
	/* {author=Michael Macnair} */

	/** 
	 *  the number of words in the phrase
	 */
	protected int numWords = 4;
	
	protected String[] dict;
	
	private void initDict() throws IOException {
		ArrayList<String> dictList = new ArrayList<String>();
		
		AssetManager am = randomMain.getInstance().getAssets();
		
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			is = am.open("british-english");
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			// Slurp the dictionary into the lines list
			String contents = br.readLine();
			int i = 0;
		    while (contents != null)  
		    {
		    	dictList.add(contents);
		    	i = i + 1;
		        contents = br.readLine();
		    }
		} catch (IOException e) {
			throw e;
		} finally {
			br.close();
		    isr.close();
		    is.close();
		}
		
		// Convert dictList into array
		dict = new String[dictList.size()];
		dictList.toArray(dict);
	}
	
	@Override
	public String getDescription() {
		return "Produces pass phrases from a dictionary, all words have the same (uniform) probability";
	}
	
	@Override
	public String getName() {
		return "Pass Phrase" ;
	}
	
	@Override
	public String getNext() {	
		if (numWords<1) return "Parameters not set";//TODO: set for R.string
		
		if (dict == null) {
			try {
				initDict();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return "Could not initialise dictionary!";				
			}
		}
		
		String phrase = "";
		int wordIdx;
		for (int i = 0; i < numWords; i++) {
			wordIdx = this.generator.nextInt(dict.length);
			phrase = phrase + " " + dict[wordIdx];
		}
		return phrase + "\n";
	}
	
	@Override
	public int getParamsLayoutID() {
		return gr.allamanis.randgen.R.id.passPhraseParams;
	}
	
	@Override
	public boolean setParameters(final Activity myActivity) {
		try{
			EditText edit =(EditText) myActivity.findViewById(gr.allamanis.randgen.R.id.passPhraseWords);
			numWords=Integer.parseInt(edit.getText().toString());
			if (numWords < 1){
				throw new Exception("Passphrase must have at least one word.");
			} 
			return true;
		}catch(Exception e){
			Toast error = Toast.makeText(myActivity, gr.allamanis.randgen.R.string.paramError, Toast.LENGTH_SHORT);
			error.show();
			return false;
		}
	}
}