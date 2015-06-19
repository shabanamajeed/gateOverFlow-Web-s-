package shabana.gateoverflow_web;

/**
 * Created by shabana on 6/17/15.
 */
public class Details {
    String name;
    String desc;
    String time;
    String autor;

    //Store URL on global basis
    String main_Page="http://gateoverflow.in";
    String questions="http://gateoverflow.in/questions";
    String qa="http://gateoverflow.in/qa";
    String rss="http://gateoverflow.in/feed/questions.rss";
    String answers="http://gateoverflow.in/feed/answers.rss";

    public String getAnswers() {
        return answers;
    }

    public String getRss() {
        return rss;
    }

    public String getQuestions() {
        return questions;
    }


    public String getQa() {
        return qa;
    }

    public String getMain_Page() {
        return main_Page;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public void setTime(String time) {
        this.time = time;
    }


    public void setAutor(String autor) {
        this.autor = autor;
    }
}
