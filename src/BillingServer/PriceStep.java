package BillingServer;

import java.io.Serializable;

/**
 *
 * @author Dave
 */
public class PriceStep implements Serializable{

    private double min_value;
    private double max_value;
    private double fee_fixed;
    private double fee_variable;
    public String INFINITY = "INFINITY";

    public PriceStep(double min_value, double max_value, double fee_fixed, double fee_variable) {
        this.min_value = min_value;
        this.max_value = max_value;
        this.fee_fixed = fee_fixed;
        this.fee_variable = fee_variable;
    }
    
    public double getMin_value() {
        return min_value;
    }

    public void setMin_value(double min_value) {
        this.min_value = min_value;
    }

    public double getMax_value() {
        return max_value;
    }

    public void setMax_value(double max_value) {
        this.max_value = max_value;
    }

    public double getFee_fixed() {
        return fee_fixed;
    }

    public void setFee_fixed(double fee_fixed) {
        this.fee_fixed = fee_fixed;
    }

    public double getFee_variable() {
        return fee_variable;
    }

    public void setFee_variable(double fee_variable) {
        this.fee_variable = fee_variable;
    }

    @Override
    public String toString() {
        if(max_value == 0){
            return "PriceStep{" + "min_value=" + min_value + ", max_value=" + "INFINITY" + ", fee_fixed=" + fee_fixed + ", fee_variable=" + fee_variable + '}';
        }
        return "PriceStep{" + "min_value=" + min_value + ", max_value=" + max_value + ", fee_fixed=" + fee_fixed + ", fee_variable=" + fee_variable + '}';
    }
}
