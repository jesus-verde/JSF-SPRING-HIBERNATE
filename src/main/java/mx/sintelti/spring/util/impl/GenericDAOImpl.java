package mx.sintelti.spring.util.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import mx.sintelti.spring.util.GenericDAO;

public class GenericDAOImpl<T> implements GenericDAO<T> {
	
	private Class<T> persistentClass;

    protected Session getSession() {
    	return new Configuration().configure().
    			buildSessionFactory().openSession();
    }
    
	public GenericDAOImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getPersistentClass() {
        return this.persistentClass;
    }

	@Override
	@Transactional
	public T guardarRegistro(T entity) throws Exception {
		Session session = getSession();
        try {
            session.getTransaction().begin();
            session.saveOrUpdate(entity);
            session.flush();
            session.refresh(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            session.getTransaction().rollback();
            throw e;
        }
        return entity;
	}

	@Override
	@Transactional
	public T editarRegistro(T entity) throws Exception {
		Session session = getSession();
        try {
            session.getTransaction().begin();
            session.saveOrUpdate(entity);
            session.flush();
            session.refresh(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            throw e;
        }
        return entity;
	}

	@Override
	@Transactional
	public T borrarRegistro(T entity) throws Exception {
		Session session = getSession();
        try {
            session.getTransaction().begin();
            session.delete(entity);
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            throw e;
        }
        return entity;
	}

	@Override
	@Transactional
	public List<T> listarRegistros() throws Exception {
		Session session = getSession();
        Criteria criteria = null;
        List<T> list = null;
        try {
            session.getTransaction().begin();
            criteria = session.createCriteria(getPersistentClass());
            criteria.addOrder(Order.asc("id"));
            list = criteria.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
            throw e;
        }
        return list;
	}


	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}
}
