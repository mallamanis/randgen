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

import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.widget.Toast;


/** 
 *  Class that uses the /dev/random to produce the initial seed
 */
public class LinuxRandomSeeder extends SeedProvider {
	@Override
	public String getDescription() {
		return "Generates Random Seed using Linux random generator";
	}

	@Override
	public String getName() {
		return "Linux /dev/random";
	}

	@Override
	public void getNewSeed(Activity myActivity) {
		String toastText = "";
		
		try{
        	
			FileInputStream myfile=new FileInputStream("/dev/random");
			int sizeOfLong=Long.SIZE/8;
			byte theseed[]=new byte[sizeOfLong];
			myfile.read(theseed,0,sizeOfLong-1);
			myfile.close();	       
			seed=0; //Generate seed
			for (int i=0;i<sizeOfLong;i++){
				seed= seed << 8;
				seed+=theseed[i];								
			}
			RandGenApp.getRandomGenerator().getSeed();
			toastText = "Created seed.";
        }
        catch(IOException e){
			toastText = "Error: "+e.getLocalizedMessage();
        }
        catch(Exception e){
			toastText = "Error: "+e.getLocalizedMessage();
        }
		
		Toast toast = Toast.makeText(myActivity, toastText, Toast.LENGTH_SHORT);
		toast.show();
	}

	

}