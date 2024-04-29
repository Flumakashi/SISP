module org.example.diplommain {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;


    opens org.example.diplommain to javafx.fxml;
    exports org.example.diplommain;
}