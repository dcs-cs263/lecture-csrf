package dcs;

public class DCSNews {
    public String body;

    public DCSNews(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return this.body;
    }
}
