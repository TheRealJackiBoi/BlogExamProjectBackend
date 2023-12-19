# Backend for Blogged 
[Blogged frontend](https://github.com/TheRealJackiBoi/BlogExamProject)

## Description

This is the backend for our Blogged project. It is a REST API that uses JPA to communicate with a PostgresSQL database. It is hosted on a Digital Ocean droplet.


## Features

- Create, read, update and delete users
- Create, read, update and delete posts

## CRUD Operationer
We mostly use generic crud operations in our DAO classes. This is to avoid having to write the same code over and over again. We have a generic DAO class that we extend from in our DAO classes. This class contains the generic crud operations.

### Generic create
```Java
    public T read(Class<T> tClass, D d){
        try(EntityManager em = emf.createEntityManager()){
            return em.find(tClass, d);
            } 
            catch (Exception e){
            e.printStackTrace();
            return null;
            }
        }
```

### Generic read all
```Java
    public List<T> readAll(Class<T> tClass){
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT t FROM " + tClass.getSimpleName() + " t", tClass).getResultList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
```

### Generic create
```Java
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
```

### Generic update
```Java
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
```

### Generic delete
```Java
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
```

### Read all public posts
```java
    public List<Post> getAllPublicPosts() {
        try (EntityManager em = getEmf().createEntityManager()) {
            em.getTransaction().begin();
            List<Post> posts = em.createQuery("SELECT p FROM Post p WHERE p.visibility = PUBLIC", Post.class).getResultList();
            em.getTransaction().commit();
            return posts;
            }
        }
```

## Contributing

If you are interested in contribuing to this or any of our future projects, apply via this [Contribute doc](https://youtu.be/dQw4w9WgXcQ) link.