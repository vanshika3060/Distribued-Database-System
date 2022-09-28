package analysis;

public class MyData {
    private String database;
    private Integer count;

    public MyData(String database, Integer count) {
        this.database = database;
        this.count = count;
    }

    public String getDatabase() {
        return database;
    }

    public MyData setDatabase(String database) {
        this.database = database;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public MyData setCount(Integer count) {
        this.count = count;
        return this;
    }

    @Override
    public String toString() {
        return "MyData{" +
                "database='" + database + '\'' +
                ", count=" + count +
                '}';
    }
}