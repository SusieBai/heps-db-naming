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
import heps.db.naming.excel.PrintDuplicate;
import java.io.File;
import java.util.ArrayList;
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
public class DesignAPI {
    
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
    //前端删除数据
    public Integer deleteDeviceSubsystemLocation(Integer dslId){
        DeviceSubsystemLocation dsl = em.find(DeviceSubsystemLocation.class, dslId);
        em.remove(dsl);
        et.commit();
        return 1;
    }
    

    public String queryAllSystems(){
        String re;
        Query query = em.createQuery("SELECT DISTINCT a.systemName FROM Accsystem a");
        List l = query.getResultList();
        re = l.toString().substring(1, l.toString().length()-1);
        return re;
    }
    
    public String getSystems() {
        Query query = em.createNamedQuery("Accsystem.findAll");
        List<Accsystem> re = query.getResultList();
        return re.toString();
    }
    
    public String queryAllSubsystems(){
        String re;
        Query query = em.createQuery("SELECT DISTINCT s.subsystemName FROM Subsystem s");
        List l = query.getResultList();
        re = l.toString().substring(1, l.toString().length()-1);
        return re;
    }
    
    public String queryAllLocations(){
        String re;
        Query query = em.createQuery("SELECT DISTINCT l.locationName FROM Location l");
        List l = query.getResultList();
        re = l.toString().substring(1, l.toString().length()-1);
        return re;
    }
    
    public String queryAllDesignNames(){
        String re;
        Query query = em.createQuery("SELECT DISTINCT d.designName FROM Device d");
        List l = query.getResultList();
        re = l.toString().substring(1, l.toString().length()-1);
        return re;
    }
    
    public String queryMaxSeqInDevice(String sysName){
        Integer re;
        Query q = em.createQuery("SELECT DISTINCT a.systemName FROM Accsystem a");
        List l = q.getResultList();
        String temp = l.toString();
        if (temp.contains(sysName)) {
            Query query = em.createQuery("SELECT MAX(d.seqNumber) FROM Device d WHERE d.systemId IN(SELECT a FROM Accsystem a WHERE a.systemName = :systemName)");
            query.setParameter("systemName", sysName);
            Query queryid = em.createQuery("SELECT DISTINCT d.systemId FROM Device d ");
            Query qsysid = em.createQuery("SELECT a.systemId FROM Accsystem a WHERE a.systemName = :systemName");
            qsysid.setParameter("systemName", sysName);
            if (!queryid.getResultList().toString().contains(qsysid.getSingleResult().toString())) {
                re = 1;
            }else{
                re = (Integer) query.getSingleResult();
                re += 1;
            }
            
        }else{
            re = 1;
        }
        return re.toString();
    }
    
    public String queryAllChineseName(String name){
        Query query = em.createQuery("SELECT d FROM Device d WHERE d.chinesename LIKE :param");
//        Query query = em.createQuery("SELECT new Device(d.chinesename,d.englishname,d.designName) from Device d");
        query.setParameter("param", "%"+name+"%");
        List<Device> re = query.getResultList();
        return re.toString();
    }
    
    public String queryEnglishNameByChinese(String chineseName) {
        Query query = em.createQuery("SELECT d FROM Device d WHERE d.chinesename = :chinesename");
        query.setParameter("chinesename", chineseName);
        List<Device> re = query.getResultList();
        return re.toString();
    }
    
    public String queryDeviceNameByEnglish(String englishName){
        Query query = em.createQuery("SELECT d FROM Device d WHERE d.englishname = :englishname");
        query.setParameter("englishname", englishName);
        List<Device> re = query.getResultList();
        return re.toString();
    }
    
    
    public String queryAll(int currentPage,int pageSize ){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d ORDER BY d.id ASC");
        List<DeviceSubsystemLocation> re = query.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryAllCount() {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d");
        Long re = (Long) query.getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySystemAcc(String sysname,int currentPage,int pageSize ){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery(" SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId IN(SELECT a FROM Accsystem a WHERE a.systemName = :systemName) ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        // System.out.println(re.toString());
        return re.toString();
    }
    public String queryDeviceBySystemAccCount(String sysname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId IN(SELECT a FROM Accsystem a WHERE a.systemName = :systemName)");
        Long re = (Long) query.setParameter("systemName", sysname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySubsystem(String subname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery(" SELECT d FROM DeviceSubsystemLocation d WHERE d.subsystemId IN(SELECT s FROM Subsystem s WHERE s.subsystemName = :subsystemName) ORDER BY d.id ASC");
        query.setParameter("subsystemName", subname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        // System.out.println(re.toString());
        return re.toString();
    }
    public String queryDeviceBySubsystemCount(String subname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.subsystemId IN(SELECT s FROM Subsystem s WHERE s.subsystemName = :subsystemName)");
        Long re = (Long) query.setParameter("subsystemName", subname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceByDevname(String devname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.designName = :designName ORDER BY d.id ASC");
        query.setParameter("designName", devname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String queryDeviceByDevnameCount(String devname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.designName = :designName");
        Long re = (Long) query.setParameter("designName", devname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
   
    public String queryDeviceByLocation(String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query =em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.locationId IN (SELECT l FROM Location l WHERE l.locationName = :locationName) ORDER BY d.id ASC");
        query.setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String queryDeviceByLocationCount(String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.locationId IN (SELECT l FROM Location l WHERE l.locationName = :locationName)");
        Long re = (Long) query.setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySystemAccSubsystem(String sysname, String subname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }   
    public String queryDeviceBySystemAccSubsystemCount(String sysname, String subname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("subsystemName", subname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySystemAccDevname(String sysname, String devname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.deviceId.designName = :designName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("designName", devname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String queryDeviceBySystemAccDevnameCount(String sysname, String devname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.deviceId.designName = :designName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("designName", devname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySystemAccLocation(String sysname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    } 
    public String queryDeviceBySystemAccLocationCount(String sysname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySubsystemDevname(String subname, String devname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName ORDER BY d.id ASC");
        query.setParameter("subsystemName", subname).setParameter("designName", devname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String queryDeviceBySubsystemDevnameCount(String subname, String devname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName");
        Long re = (Long) query.setParameter("subsystemName", subname).setParameter("designName", devname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceBySubsystemLocation(String subname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.subsystemId.subsystemName = :subsystemName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("subsystemName", subname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String queryDeviceBySubsystemLocationCount(String subname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.subsystemId.subsystemName = :subsystemName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("subsystemName", subname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String queryDeviceByDevnameLocation(String devname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.designName = :designName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("designName", devname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String queryDeviceByDevnameLocationCount(String devname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.designName = :designName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("designName", devname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String querySystemAccSubsystemDevname(String sysname, String subname, String devname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setParameter("designName", devname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String querySystemAccSubsystemDevnameCount(String sysname, String subname, String devname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setParameter("designName", devname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String querySystemAccSubsystemLocation(String sysname, String subname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String querySystemAccSubsystemLocationCount(String sysname, String subname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String querySubsystemDevnameLocation(String subname, String devname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("subsystemName", subname).setParameter("designName", devname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String querySubsystemDevnameLocationCount(String subname, String devname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("subsystemName", subname).setParameter("designName", devname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public  String querySystemAccDevnameLocation(String sysname, String devname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.deviceId.designName = :designName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("designName", devname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String querySystemAccDevnameLocationCount(String sysname, String devname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.deviceId.designName = :designName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("designName", devname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public String querySystemAccSubsystemDevnameLocation(String sysname, String subname, String devname, String locname,int currentPage,int pageSize){
        int firstResult = (currentPage - 1) * pageSize;
        int maxResults = pageSize;
        Query query = em.createQuery("SELECT d FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName AND d.locationId.locationName = :locationName ORDER BY d.id ASC");
        query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setParameter("designName", devname).setParameter("locationName", locname).setFirstResult(firstResult).setMaxResults(maxResults);
        List<DeviceSubsystemLocation> re = query.getResultList();
        return re.toString();
    }
    public String querySystemAccSubsystemDevnameLocationCount(String sysname, String subname, String devname, String locname) {
        Query query = em.createQuery("SELECT COUNT(d) FROM DeviceSubsystemLocation d WHERE d.deviceId.systemId.systemName = :systemName AND d.subsystemId.subsystemName = :subsystemName AND d.deviceId.designName = :designName AND d.locationId.locationName = :locationName");
        Long re = (Long) query.setParameter("systemName", sysname).setParameter("subsystemName", subname).setParameter("designName", devname).setParameter("locationName", locname).getSingleResult();
        // System.out.println(re.toString());
        return re.toString();
    }
    
    public Integer deleteDeviceById(Integer deviceId){
        Device device = em.find(Device.class, deviceId);
        em.remove(device);
        et.commit();
        return 1;
    }
    
}
