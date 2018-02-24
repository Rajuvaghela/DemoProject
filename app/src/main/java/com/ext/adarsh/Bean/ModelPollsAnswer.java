package com.ext.adarsh.Bean;

/**
 * Created by ExT-Emp-011 on 1/16/2018.
 */

public class ModelPollsAnswer {

    String polls_id = "";
    String polls_answer = "";
    String choice_id = "";

    public String getChoice_id() {
        return choice_id;
    }

    public void setChoice_id(String choice_id) {
        this.choice_id = choice_id;
    }

    public String getPolls_id() {
        return polls_id;
    }

    public void setPolls_id(String polls_id) {
        this.polls_id = polls_id;
    }

    public String getPolls_answer() {
        return polls_answer;
    }

    public void setPolls_answer(String polls_answer) {
        this.polls_answer = polls_answer;
    }
}
