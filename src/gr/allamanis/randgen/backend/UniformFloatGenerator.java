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

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

/** 
 *  A RandomGenerator that produces float's in a uniform distribution
 */
public class UniformFloatGenerator extends RandomGenerator {
	/* {author=Miltiadis Allamanis}*/
	
	
	/** 
	 *  the smallest number of the uniform distribution
	 */
	public double a=0;
	
	/** 
	 *  the biggest value of the uniform distribution
	 */
	public double b=0;
	  
	@Override
	public String getDescription() {
		return "Produces random decimal numbers that have the same (uniform) probability in a specified interval";
	}
	
	@Override
	public String getName() {
		return "Uniform Real";
	}
	
	@Override
	public int getParamsLayoutID() {
		return  gr.allamanis.randgen.R.id.uniformFloatParams;
	}
	
	@Override
	public String getNext() {
		if (a==0 && b==0)
			return "Parameters not set";
		return Double.toString(a+(b-a)*this.generator.nextDouble());
	}
	
	@Override
	public boolean setParameters(final Activity myActivity) {
		try{
			EditText edit =(EditText) myActivity.findViewById(gr.allamanis.randgen.R.id.uniformFLow);
			a=Double.parseDouble(edit.getText().toString());
			edit =(EditText) myActivity.findViewById(gr.allamanis.randgen.R.id.uniformFHigh);
			b=Double.parseDouble(edit.getText().toString());
			return true;
		}catch(Exception e){
			Toast error = Toast.makeText(myActivity, gr.allamanis.randgen.R.string.paramError, Toast.LENGTH_SHORT);
			error.show();
			return false;
		}
	}
}