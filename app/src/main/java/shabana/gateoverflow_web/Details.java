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
    final String site_name="http://gateoverflow.in";
    String main_Page=site_name+"/qa";
    String questions=site_name+"/questions";
    String qa=site_name+"/qa";
    String rss=site_name+"/feed/questions.rss";
    String answers=site_name+"/feed/answers.rss";
    private String activity=site_name+"/activity";
    private String rss_activity=site_name+"/feed/activity";
    private String rss_answers=site_name+"/feed/answers.rss";
    private String prev_years=site_name+"/previous-years";
    private String topics=site_name+"/tags";
    //String activity=site_name+"/activity";

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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getRss_activity() {
        return rss_activity;
    }

    public void setRss_activity(String rss_activity) {
        this.rss_activity = rss_activity;
    }

    public String getPrev_years() {
        return prev_years;
    }

    public void setPrev_years(String prev_years) {
        this.prev_years = prev_years;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getRss_answers() {
        return rss_answers;
    }

    public void setRss_answers(String rss_answers) {
        this.rss_answers = rss_answers;
    }
}
