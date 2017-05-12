package com.koceeng.freedonation.help;

import com.koceeng.freedonation.base.FirebaseObject;

public class Faq extends FirebaseObject {

    private String question;
    private String answer;

    public Faq() {
    }

    public Faq(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
