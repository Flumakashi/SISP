module org.example.diplommain {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens org.example.diplommain to javafx.fxml;
    exports org.example.diplommain;
}