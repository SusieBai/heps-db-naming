/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heps.db.naming.api;

import heps.db.naming.entity.Accsystem;
import heps.db.naming.entity.Device;
import heps.db.naming.entity.DeviceSubsystemLocation;
import heps.db.naming.entity.Location;
import heps.db.naming.entity.MoreThanNine;
import heps.db.naming.entity.Subsystem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author BaiYu
 */
public class InsertAPI {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("heps-db-namingPU");
    EntityManager em = emf.createEntityManager();
    EntityTransaction et = em.getTransaction();
    
    public void init(){
        emf = Persistence.createEntityManagerFactory("heps-db-namingPU");
        em = emf.createEntityManager();
        et = em.getTransaction();
        et.begin();
    }
    
    public void destory(){
        em.close();
        emf.close();
    }
    
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
    
    public void setMoreThanNine(String judge,String anotherName){
        MoreThanNine mtn = new MoreThanNine();
        mtn.setYesOrNo(judge);
        mtn.setAnotherName(anotherName);
        em.getTransaction().begin();
        em.persist(mtn);
        em.getTransaction().commit();
    }
    public MoreThanNine queryByYesOrNo(String judge,String another){
        if(judge==null||("".equals(judge))) return null;
        Query query = em.createQuery("SELECT m FROM MoreThanNine m WHERE m.yesOrNo = :yesOrNo AND m.anotherName = :anotherName");
        query.setParameter("yesOrNo", judge).setParameter("anotherName", another);
        List<MoreThanNine> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }else{
            return result.get(0);
        }
    }
    
     public void setSubsystem(String name){
        Subsystem sub = new Subsystem();
        sub.setSubsystemName(name);
        em.getTransaction().begin();
        em.persist(sub);
        em.getTransaction().commit();
    } 
    public Subsystem getSubsystem(String name){
        Query query = em.createNamedQuery("Subsystem.findBySubsystemName");
        query.setParameter("subsystemName", name);
        List<Subsystem> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }else{
            return result.get(0);
        }
    }
    
    public void setLocation(MoreThanNine mtn, String name){
        Location l = new Location();
        l.setJudgeId(mtn);
        l.setLocationName(name);
        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
    }
    public Location getLocation(String mtn, String another, String name){
        Query query = em.createQuery("SELECT l FROM Location l WHERE l.locationName = :locationName AND l.judgeId.yesOrNo = :yesOrNo AND l.judgeId.anotherName = :anotherName");
        query.setParameter("locationName", name).setParameter("yesOrNo", mtn).setParameter("anotherName", another);
        List<Location> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        }else{
            return result.get(0);
        }
    }
    
    public void setDevice(Accsystem system, String seqNumber, String ChineseName, String EnglishName, String designName, String remarks){
        Device d = new Device();
        d.setSystemId(system);
        d.setSeqNumber(Integer.parseInt(seqNumber));
        d.setEnglishname(EnglishName);
        d.setChinesename(ChineseName);
        d.setDesignName(designName);
        d.setRemark(remarks);
        
        em.getTransaction().begin();
        em.persist(d);
        em.getTransaction().commit();
    }
    public Device getDeviceBy(String system, String seqNumber, String ChineseName, String EnglishName, String designName, String remarks) {
        Query query = em.createQuery("SELECT d FROM Device d WHERE d.systemId.systemName = :systemName AND d.seqNumber = :seqNumber AND d.chinesename = :chinesename AND d.englishname = :englishname "
                + "AND d.designName = :designName AND d.remark = :remark");
        query.setParameter("systemName", system).setParameter("seqNumber", Integer.parseInt(seqNumber)).setParameter("chinesename", ChineseName).setParameter("englishname", EnglishName).setParameter("designName", designName).setParameter("remark", remarks);
        List<Device> re = query.getResultList();
        if (re.isEmpty()) {
            return null;
        } else {
            return re.get(0);
        }
    }
    
    public void setDeviceSubsystemLocation(Device device, Subsystem subsystem, Location location){
        DeviceSubsystemLocation dsl = new DeviceSubsystemLocation();
        dsl.setDeviceId(device);
        dsl.setSubsystemId(subsystem);
        dsl.setLocationId(location);
         
        em.getTransaction().begin();
        em.persist(dsl);
        em.getTransaction().commit();
    }     
    public void updateDeviceSubsystemLocation(Device device, Subsystem subsystem, Location location, Integer dslId){
        DeviceSubsystemLocation dsl = (DeviceSubsystemLocation) em.createNamedQuery("DeviceSubsystemLocation.findById").setParameter("id", dslId).getSingleResult();
        dsl.setDeviceId(device);
        dsl.setSubsystemId(subsystem);
        dsl.setLocationId(location);
        em.getTransaction().begin();
        em.persist(dsl);
        em.getTransaction().commit();
    }
    public DeviceSubsystemLocation getDeviceSubsystemLocationBy(String system, String seqNumber, String ChineseName, String EnglishName, String designName, String remarks, String subsystem, String location, String yon, String another){
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.deviceId.seqNumber = :seqNumber AND d.deviceId.chinesename = :chinesename AND d.deviceId.englishname = :englishname AND d.deviceId.designName = :designName AND d.deviceId.remark = :remark AND d.subsystemId.subsystemName = :subsystemName AND d.locationId.locationName = :locationName AND d.locationId.judgeId.yesOrNo = :yesOrNo AND d.locationId.judgeId.anotherName = :anotherName");
        query.setParameter("systemName", system).setParameter("seqNumber", Integer.parseInt(seqNumber)).setParameter("chinesename", ChineseName).setParameter("englishname", EnglishName).setParameter("designName", designName).setParameter("remark", remarks).setParameter("subsystemName", subsystem).setParameter("locationName", location).setParameter("yesOrNo", yon).setParameter("anotherName", another);
        List<DeviceSubsystemLocation> re = query.getResultList();
        if (re.isEmpty()) {
            return null;
        } else {
            return re.get(0);
        }
    }
}
