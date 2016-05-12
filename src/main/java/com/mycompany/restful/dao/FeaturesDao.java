/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restful.dao;

import com.mycompany.restful.model.Features;
import com.mycompany.restful.util.HibernateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author ibilanchuk
 */
public class FeaturesDao {
     SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Features getFeatureById(int id) {

        Features feature = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = (Features) session.createQuery("from Features where id= :ID").setParameter("ID", id).uniqueResult();
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return feature;
    }
   public List<Features> getFeatures() {

        List<Features> features = null;
        Session session = null;

        try {
           
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query q = session.createQuery("from Features F");             
            q.setMaxResults(5);
            q.setFirstResult(0);
            //session.createQuery("from Features F order by count(F.id) desc limit start,lim ").setParameter("start",start).setParameter("length", length).list();
            features = q.list();
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return features;
    }
  
    public List<Features> getAllFeatures() {

        List<Features> features = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            features = session.createQuery("from Features").list();
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return features;
    }
   
    public List<Features> getRenderingEngine() {

        List<Features> features = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            features = session.createQuery("select renderingEngine, count(renderingEngine) from Features group by renderingEngine").list();
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return features;
    }

    public boolean saveFeature(Features feature) {
        Session session = null;
        boolean hasErrors = false;
    
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(feature);
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
                hasErrors = true;
            }
        } finally {
            if (session != null) {
                session.close();
            }

        }
        return hasErrors;
    }
      public boolean updateFeature(int id,Features feature) {
        Session session = null;
        boolean hasErrors = false;
        feature.setId(id);
    
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(feature);
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
                hasErrors = true;
            }
        } finally {
            if (session != null) {
                session.close();
            }

        }
        return hasErrors;
    }
    
     public boolean deleteFeature(int id) {
        Session session = null;
        boolean hasErrors = false;
        Features feature = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = (Features) session.createQuery("from Features where id= :ID").setParameter("ID", id).uniqueResult();
            session.delete(feature);
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
                hasErrors = true;
            }
        } finally {
            if (session != null) {
                session.close();
            }

        }
        return hasErrors;
    }
}
