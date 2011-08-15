/**
 * 
 */
package gr.allamanis.randgen.backend;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author miltiadis
 *
 */
public class UniformNonRepeatingInteger extends RandomGenerator {
	
	protected int a=0;
	protected int b=0;
	
	private int numOfGeneratedNumbers=0;
	private Dialog dialog;
	
	private boolean[] generatedNumbers;
	
	@Override
	public String getDescription() {
		return "Produces random integers that have the same (uniform) probability in a specified interval without repeating them.";
	}

	@Override
	public String getName() {
		return "Uniform Integer (non-repeating)" ;
	}
	
	@Override
	public String getNext() {	
		if (a>=b) return "Parameters not Set";//TODO: set for R.string?
		int randomNumber=0;
		int possibleNums = b-a+1-numOfGeneratedNumbers;
		if (possibleNums==0) return "No more numbers";
		int pos = this.generator.nextInt(possibleNums);
		//return Integer.toString(numOfGeneratedNumbers);
		int i=0;
		while (i<generatedNumbers.length){
			if (!generatedNumbers[i]){ 				
				if (pos==0){
					randomNumber=a+i;
					generatedNumbers[i]=true;
					break;
				}	
				pos--;
			}
			i++;
		};
		
		numOfGeneratedNumbers++;
		return Integer.toString(randomNumber);
	}
	
	@Override
	public void setParameters(final Activity myActivity) {
		dialog=new Dialog(myActivity);
		dialog.setTitle(gr.allamanis.randgen.R.string.unifIntParam);
		dialog.setContentView(gr.allamanis.randgen.R.layout.uniforminteger);
		dialog.show();
		Button done=(Button)dialog.findViewById(gr.allamanis.randgen.R.id.uniIntOK);
		OnClickListener doneButton=new OnClickListener(){
			@Override
			public void onClick(View v) {
				try{
					EditText edit =(EditText) dialog.findViewById(gr.allamanis.randgen.R.id.uniformLow);
					a=Integer.parseInt(edit.getText().toString());
					edit =(EditText) dialog.findViewById(gr.allamanis.randgen.R.id.uniformHigh);
					b=Integer.parseInt(edit.getText().toString()); 
					dialog.dismiss();
					if (a>=b) return; //TODO: Error Message
					numOfGeneratedNumbers=0;
					generatedNumbers = new boolean[b-a+1];
					for (int i=0;i<generatedNumbers.length;i++)
						generatedNumbers[i]=false;
				}catch(Exception e){
					TextView done=(TextView)dialog.findViewById(gr.allamanis.randgen.R.id.notification);
					done.setVisibility(View.VISIBLE);
									
				}
				
			}		
		};
		done.setOnClickListener(doneButton);	
		
		
	}

}
