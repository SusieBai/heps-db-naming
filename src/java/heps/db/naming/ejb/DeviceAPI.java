/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heps.db.naming.ejb;

import heps.db.naming.common.tools.EmProvider;
import heps.db.naming.entity.Device;
import heps.db.naming.entity.Location;
import heps.db.naming.entity.MoreThanNine;
import heps.db.naming.entity.Subsystem;
import heps.db.naming.entity.Accsystem;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author BaiYu
 */

public class DeviceAPI {

    public static EntityManager em = EmProvider.getInstance().getEntityManagerFactory().createEntityManager();
    
    public List<Device> getAllDevice(){
        Query query = em.createNamedQuery("Device.findAll");
        return query.getResultList();
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
    
    public void setDevice(Device device){
        em.getTransaction().begin();
        em.persist(device);
        em.getTransaction().commit();
    }
    
    public void updateDevice(Device newDevice){
        em.getTransaction().begin();
        em.persist(newDevice);
        em.getTransaction().commit();
    }
    
    public Integer deleteDeviceById(Integer deviceId){
        Device d = em.find(Device.class, deviceId);
        em.remove(d);
        em.getTransaction().commit();
        return 1;
    }
    
    public Device findById(Integer deviceId){
        Query query = em.createNamedQuery("Device.findByDeviceId");
        query.setParameter("deviceId", deviceId);
        List<Device> result = query.getResultList();
        if (result.isEmpty() || result == null) {
            return null;
        }else{
            Iterator it = result.iterator();
            while(it.hasNext()){
                Device device = (Device) it.next();
                return device;
            }
        }
        return null;
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
}
