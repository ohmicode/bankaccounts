package ohmicode.example.config;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class HibernatePropertiesH2 implements HibernateProperties {

    @Override
    public Properties get() {
        final Properties result = new Properties();

        result.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        result.put("hibernate.show_sql", Boolean.FALSE);
        result.put("hibernate.format_sql", Boolean.TRUE);

        return result;
    }
}
