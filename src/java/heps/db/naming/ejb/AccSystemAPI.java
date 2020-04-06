/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heps.db.naming.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import heps.db.naming.common.tools.EmProvider;
import heps.db.naming.entity.Accsystem;

/**
 *
 * @author BaiYu
 */

public class AccSystemAPI{
    public static EntityManager em = EmProvider.getInstance().getEntityManagerFactory().createEntityManager();
    
    public void setSystem(String name){
        Accsystem s = new Accsystem();
        s.setSystemName(name);
        em.getTransaction().begin();
        em.persist(s);
        em.getTransaction().commit();
    }

    public Accsystem getSystem(String name){
        Query query = em.createNamedQuery("Accsystem.findBySystemName");
        query.setParameter("systemName", name);
        List<Accsystem> result =query.getResultList();
        if (result.isEmpty()) {
            return null;
        }else{
            return result.get(0);
        }
    }
    
    public List<Accsystem> getAllSystem(){
        Query query = em.createNamedQuery("Accsystem.findAll");
        return query.getResultList();
    }
    

   
    
}
