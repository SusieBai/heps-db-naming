/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heps.db.naming.ejb;

import heps.db.naming.common.tools.EmProvider;
import static heps.db.naming.ejb.DeviceAPI.em;
import heps.db.naming.entity.Device;
import heps.db.naming.entity.DeviceSubsystemLocation;
import heps.db.naming.entity.Location;
import heps.db.naming.entity.Subsystem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author BaiYu
 */
public class DeviceSubsystemLocationAPI {
    public static EntityManager em = EmProvider.getInstance().getEntityManagerFactory().createEntityManager();
    
     public List<DeviceSubsystemLocation> getAllDevice(){
        Query query = em.createNamedQuery("DeviceSubsystemLocation.findAll");
        return query.getResultList();
    }
     
     public void setDeviceSubsystemLocation(Device device, Subsystem subsystem, Location location){
         em.getTransaction().begin();
         DeviceSubsystemLocation dsl = new DeviceSubsystemLocation();
         dsl.setDeviceId(device);
         dsl.setSubsystemId(subsystem);
         dsl.setLocationId(location);
         
        
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
