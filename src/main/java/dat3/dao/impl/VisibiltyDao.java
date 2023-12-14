package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.model.Visibility;
import jakarta.persistence.EntityManagerFactory;

public class VisibiltyDao extends CRUDDao<Visibility, String> {

        private static VisibiltyDao instance;

        private VisibiltyDao() {}

        public static VisibiltyDao getInstance(EntityManagerFactory emf) {
            if (instance == null) {
                instance = new VisibiltyDao();
                instance.setEntityManagerFactory(emf);
            }
            return instance;
        }
}
