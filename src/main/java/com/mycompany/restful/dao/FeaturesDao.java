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
import org.json.simple.JSONObject;

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
  public JSONObject getFeatures(int length, int start){

        List feature = null;
        Session session = null;
        JSONObject obj = new JSONObject();
 
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = session.createQuery("from Features").setMaxResults(length).setFirstResult(start).list();
            Query q  = session.createQuery("SELECT COUNT(*) FROM Features");
            obj.put("count", q.list()); 
            obj.put("data", (List)feature);
            session.getTransaction().commit();
        } 
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return obj;
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
