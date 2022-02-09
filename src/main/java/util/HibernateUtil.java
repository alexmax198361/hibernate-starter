package util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import converter.BirthDayConverter;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configureProperties(configuration);
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static void configureProperties(Configuration configuration) {
        configuration.addAttributeConverter(new BirthDayConverter(), true);
        configuration.registerTypeOverride(new JsonBinaryType());
    }
}
