package utils;

public class Conversion {

    public static double round(double numberToConvert, double precision){
        double p = Math.pow(10, precision);
        return (double) Math.round(numberToConvert * p) / p;
    }

    public static double convertKGtoPounds(double numberToConvert, double precision){
        return round(numberToConvert * 2.2, precision);
    }

    public static double convertMetresToInches(double numberToConvert, double precision){
        return round(numberToConvert * 39.37, precision);
    }

}