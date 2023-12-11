package dat3.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
//generic dao class that can be used for CRUD operations for all entities
// T is the class of the entity
// D is the class of the primary key
public abstract class CRUDDao<T, D> implements IDao<T, D> {
    private EntityManagerFactory emf;

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //queries

    //get a T object by primary key
    public T read(Class<T> tClass, D d){
        try(EntityManager em = emf.createEntityManager()){
            return em.find(tClass, d);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //get a list of all the T objects
    public List<T> readAll(Class<T> tClass){
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT t FROM " + tClass.getSimpleName() + " t", tClass).getResultList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //creates a T object
    public T create(T t){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            return t;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //updates a T object
    public T update(T t){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
            return t;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Class<T> tClass, D d){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            T t = em.find(tClass, d);
            em.remove(t);
            em.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean validatePrimaryKey(Class<T> tClass, D d) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            T t = em.find(tClass, d);
            em.getTransaction().commit();
            return t != null;
        }
    }

    protected EntityManagerFactory getEmf() {
        return emf;
    }
}
