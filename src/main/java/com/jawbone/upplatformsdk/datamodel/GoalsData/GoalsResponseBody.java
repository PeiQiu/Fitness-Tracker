package com.jawbone.upplatformsdk.datamodel.GoalsData;

public class GoalsResponseBody {
    private int move_steps;
    private int sleep_total;
    private float body_weight;
    private int body_weight_intent;

    public GoalsResponseBody() {
    }

    public int getMove_steps() {
        return move_steps;
    }

    public void setMove_steps(int move_steps) {
        this.move_steps = move_steps;
    }

    public int getSleep_total() {
        return sleep_total;
    }

    public void setSleep_total(int sleep_total) {
        this.sleep_total = sleep_total;
    }

    public float getBody_weight() {
        return body_weight;
    }

    public void setBody_weight(float body_weight) {
        this.body_weight = body_weight;
    }

    public int getBody_weight_intent() {
        return body_weight_intent;
    }

    public void setBody_weight_intent(int body_weight_intent) {
        this.body_weight_intent = body_weight_intent;
    }
}
