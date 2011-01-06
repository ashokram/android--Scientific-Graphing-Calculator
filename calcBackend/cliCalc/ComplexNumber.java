package cliCalc;
import java.lang.Math;

class ComplexNumber{
    private double re,im;
    
    private ComplexNumber(double re, double im){
	this.re = re;
	this.im = im;
    }

    static ComplexNumber newCartesian(double re, double im){
	return new ComplexNumber(re,im);
    }

    static ComplexNumber newPolar(double R, double theta){
	return new ComplexNumber(R*Math.cos(theta) , R*Math.sin(theta) );
    }
    
    double Re(){
	return this.re;
    }
    
    double Im(){
	return this.im;
    }

    double R(){
	return Math.sqrt(Math.pow(re,2) + Math.pow(im,2) );
    }

    double Theta(){
	return Math.atan2(this.im , this.re);
    }

    ComplexNumber conjugate(){
	return newCartesian(this.re, - this.im );
    }
    
    public String toString(){
	String result;
	if(im == 0) result = Double.toString(re);
	else result = re + " " + "+" + " " + im + "I";
	if ( Double.isInfinite(this.Re() ) || Double.isInfinite(this.Im() ) )
	    result = "INFINITY";
	if ( Double.isNaN(this.Re() ) || Double.isNaN(this.Im() ) )
	    result = "NaN";
	return result;
    }

    ComplexNumber round(int places){
	if ( Double.isInfinite(this.Re() ) || Double.isInfinite(this.Im() ) )
	    return this;
	if ( Double.isNaN(this.Re() ) || Double.isNaN(this.Im() ) )
	    return this;
	return newCartesian(this.round(this.re, places), this.round(this.im, places) );
    }
    private double round(double d, int places){
	double rounder = Math.pow(10, places);
	double result = Math.round(d * rounder);
	return (result/rounder);
    }
}