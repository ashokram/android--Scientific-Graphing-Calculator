package com.android.calcApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Toast;
import java.lang.Exception;
import cliCalc.ComplexNumber;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.res.Configuration;

public class convCalc extends Activity {

    private EditText input;
    private TextView output;
    private int menFlag,from;
    private String[] curNames;
    private double[] curConvs;
    private View type;
    private double result;
    private String copy;
    
    private String[] massNames = { "Microgram","Milligram","Gram","Kilogram",
				   "Metric ton/Megagram","Grain","Dram",
				   "Ounce","Pound","US ton" };
    private double[] massConvs = { 0.000001,0.001,1.0,1000.0,1000000.0,0.06479891,
				1.7718451953,28.349523125,453.59237,907184.74 };
    private String[] tempNames = { "Celsius","Kelvin","Fahrenheit","Rankine" };
    private double[] tempConvs = { 274.15,1,255.9277778,0.5555556 };
    private String[] volNames = { "Microliter","Milliliter","Liter","Kiloliter","Megaliter",
				  "Fifth","Shot","Gallon (US)","Pint (US)","Quart (US)" };
    private double[] volConvs = { 0.000001,0.001,1.0,1000.0,1000000.0,0.7570823568,0.029573529563,
				  3.785411784,0.473176473,0.946352946 };
    private String[] distNames = { "Nanometer","Micrometer","Millimeter","Centimeter","Meter",
				   "Kilometer","Inch","Feet","Yard","Mile","League"};
    private double[] distConvs = { 0.000000001,0.000001,0.001,0.01,1.0,1000.0,0.0254,
				   0.3048,0.9144,1609.344,4828.0417 };
    private String[] cookNames = { "Teaspoon (US)","Tablespoon (US)","Ounce (US)","Cup (Metric)",
				   "Pint (US)","Quart (US)","Gallon (US)","Barrel (US)",
				   "Milliliter","Centiliter","Liter" };
    private double[] cookConvs = { 0.0049289215938,0.014786764781,0.029573529562,0.25,
				   0.473176473,0.946352946,3.785411784,119.2404712,
				   0.001,0.01,1.0 };
    private String[] speedNames = { "Mile/hour","Feet/second","Meter/second","Kilometer/hour",
				    "Kilometer/second","Mile/second","Speed of Light",
				    "Centimeter/second","Inch/second","Knot","Mach",};
    private double[] speedConvs = { 0.44704,0.3048,1.0,0.27777777778,1000.0,1609.344,
				    299792458.0,0.01,0.0254,0.51444444444,340.29 };
    private String[] baseNames = { "2","3","4","5","6","7","8","9","10", };
    private double[] baseConvs = { 0,0,0,0,0,0,0,0,0 };
    private String[] energyNames = { "Millijoule","Joule","Kilojoule","calorie (chem)",
				       "Calorie (nutr)","Watt/hour","Kilowatt/hour","Electronvolt" };
    private double[] energyConvs = { 0.001,1.0,1000.0,4.184,4186.8,3600.0,3600000.0,1.6021773e-19 };
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conv);

	Configuration config = this.getResources().getConfiguration();
	
	if(config.orientation == 1)
	    setContentView(R.layout.conv);
	else if(config.orientation == 2)
	    setContentView(R.layout.conv2);

	input = (EditText) findViewById(R.id.convInput);
	output = (TextView) findViewById(R.id.convResult);
	//output.setText("hello");
	menFlag = 0;
	from = -1;
	result = 0;
	//Toast.makeText(convCalc.this, "Created!",Toast.LENGTH_LONG).show();
	Button n = (Button) findViewById(R.id.mass);
	n.setOnClickListener(makeClickListener(massNames,massConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.temp);
	n.setOnClickListener(makeClickListener(tempNames,tempConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.volume);
	n.setOnClickListener(makeClickListener(volNames,volConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.dist);
	n.setOnClickListener(makeClickListener(distNames,distConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.speed);
	n.setOnClickListener(makeClickListener(speedNames,speedConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.cooking);
	n.setOnClickListener(makeClickListener(cookNames,cookConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.base);
	n.setOnClickListener(makeClickListener(baseNames,baseConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.energy);
	n.setOnClickListener(makeClickListener(energyNames,energyConvs,n));
	registerForContextMenu(n);
	n = (Button) findViewById(R.id.copyConv);
	n.setOnClickListener(copyBtn);
	n = (Button) findViewById(R.id.pasteConv);
	n.setOnClickListener(pasteBtn);
    }

    public void onPause() {
	super.onPause();
	mainTab.shared = copy;
    }

    public void onResume() {
	super.onResume();
	copy = mainTab.shared;
    }
    
    private OnClickListener makeClickListener(final String[] names,final double[] convs,final Button b) {
	return new OnClickListener() {
	    public void onClick(View v) {
		curNames = names;
		curConvs = convs;
		menFlag = 2;
		openContextMenu(b);
		Toast.makeText(convCalc.this, "Created!",Toast.LENGTH_LONG).show();
		menFlag = 1;
		openContextMenu(b);
	    }
	};
    }

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
	if(menFlag == 1) {
	    menu.setHeaderTitle("Convert from...");
	} else if(menFlag == 2) {
	    menu.close();
	    menu.setHeaderTitle("to...");
	}
	for(int i=0;i<curNames.length;i+=1) {
	    menu.add(0,i,0,curNames[i]);
	}
	type = v;
    }
    
    public boolean onContextItemSelected(MenuItem item) {
	if(menFlag == 1) {
	    from = item.getItemId();
	    menFlag = 2;
	} else {
	    try {
		String text = input.getText().toString();
		if(text.length() == 0) text = "0";
		double inp = Double.parseDouble(text);
		Toast.makeText(convCalc.this, Integer.toString(from)+" "+Integer.toString(item.getItemId()),Toast.LENGTH_LONG).show();
		if(curNames[0].compareTo("Celsius") == 0) {
		    // Temp conversion is special :/
		    result = convertTemp(curNames[from],curNames[item.getItemId()],inp);
		} else if(curNames[0].compareTo("2") == 0) {
		    result = Double.parseDouble(convertBase((int)inp,from+2,item.getItemId()+2));
		    result = ComplexNumber.round(result,10);
		    output.setText(Integer.toString((int)result));
		    return true;
		} else {
		    result = (inp * curConvs[from]) / curConvs[item.getItemId()];
		}
		result = ComplexNumber.round(result,10);
		output.setText(Double.toString(result));
		menFlag = 0;
	    } catch (Exception e) {
		Toast.makeText(convCalc.this, "Could not convert.",Toast.LENGTH_LONG).show();
	    }
	}
	return true;
    }

    private double convertTemp(String l1, String l2, double x) {
	double n=0;
	if(l1.compareTo("Celsius") == 0)
	    n = x + 273.15;
	else if(l1.compareTo("Kelvin") == 0)
	    n = x;
	else if(l1.compareTo("Fahrenheit") == 0)
	    n = (x + 459.67) * (5.0 / 9.0);
	else if(l1.compareTo("Rankine") == 0)
	    n = x * (5.0 / 9.0);
	if(l2.compareTo("Celsius") == 0)
	    return n - 273.15;
	else if(l2.compareTo("Kelvin") == 0)
	    return n;
	else if(l2.compareTo("Fahrenheit") == 0)
	    return (n * (9.0 / 5.0)) - 459.67;
	else if(l2.compareTo("Rankine") == 0)
	    return n * (9.0 / 5.0);
	return -1;
    }

    private String convertBase(int n,int b1, int b2) {
	int med = 0;
	char[] num = Integer.toString(n).toCharArray();
	int mult = 1;
	for(int i=num.length-1;i>=0;i-=1) {
	    med += (Integer.parseInt(Character.toString(num[i]))*mult);
	    mult *= b1;
	}
	String res = "";
	while(med > b2) {
	    res = Integer.toString(mod(med,b2)) + res;
	    med = med / b2;
	}
	return Integer.toString(med) + res;
    }
       
    private int mod(int x, int n) {
	while(x >= n) x -= n;
	return x;
    }


    // Res better be big enough!
    /*private void addBase(short res[],int y[], int b) {
	short carry=0;
	for(int i=0;i<y.length;i+=1) {
	    res[i] += carry;
	    carry = res[i] + y[i];
	    if(carry > b=) {
		res[i] = carry - b;
		carry = 1;
	    } else {
		res[i] = carry;
		carry = 0;
	    }
	}
    }*/
    
    private int indexOf(String s, String[] a) {
	for(int i=0;i<a.length;i+=1) {
	    if(s.compareTo(a[i]) == 0) {
		return i;
	    }
	}
	return -1;
    }

    private OnClickListener pasteBtn = new OnClickListener() {
	    public void onClick(View v) {
		input.setText(copy);
	    }
	};

    private OnClickListener copyBtn = new OnClickListener() {
	    public void onClick(View v) {
		copy = output.getText().toString();
	    }
	};

}