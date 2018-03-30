package ohmicode.example.config;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Component;

@Component
public class DBConnectionH2 implements DBConnection {

    private final DataSource dataSource;

    public DBConnectionH2() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("/initScripts/database.sql").build();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
