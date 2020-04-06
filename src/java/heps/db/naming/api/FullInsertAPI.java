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

/**
 *
 * @author BaiYu
 */
public class FullInsertAPI {
    
    public void insertDevice(ArrayList fullInfo){
        Iterator it = fullInfo.iterator();
        while (it.hasNext()) {
            ArrayList oneRow = (ArrayList) it.next();
            String sys = oneRow.get(0).toString();
            String seq = oneRow.get(1).toString();
            String chinese = oneRow.get(2).toString();
            String english = oneRow.get(3).toString();
            String design = oneRow.get(4).toString();
            String remark = oneRow.get(9).toString();
            String sub = oneRow.get(5).toString();
            String location = oneRow.get(6).toString();
            String yon = oneRow.get(7).toString();
            String another = oneRow.get(8).toString();
            Accsystem sysData;
            Subsystem subData;
            MoreThanNine mtnData;
            Location locationData;
            Device designData;
            DeviceSubsystemLocation dslData;
            InsertAPI in = new InsertAPI();
            if (in.getSystem(sys) == null) {
                in.setSystem(sys);
            }
            if (in.getSubsystem(sub) == null) {
                in.setSubsystem(sub);
            }
            if (in.queryByYesOrNo(yon, another) == null) {
                in.setMoreThanNine(yon, another);
            }
            if (in.getLocation(yon, another, location) == null) {
                mtnData = in.queryByYesOrNo(yon, another);
                in.setLocation(mtnData, location);
            }
            if (in.getDeviceBy(sys, seq, chinese, english, design, remark) == null) {
                sysData = in.getSystem(sys);
                in.setDevice(sysData, seq, chinese, english, design, remark);
            }
            
            subData = in.getSubsystem(sub);
            locationData = in.getLocation(yon, another, location);
            designData = in.getDeviceBy(sys, seq, chinese, english, design, remark);
            if (in.getDeviceSubsystemLocationBy(sys, seq, chinese, english, design, remark, sub, location, yon, another) == null) {
                in.setDeviceSubsystemLocation(designData, subData, locationData);
            }
        }
    }
    
    public void updateDevice(ArrayList fullInfo, Integer dslId){
        Iterator it = fullInfo.iterator();
        while (it.hasNext()) {
            ArrayList oneRow = (ArrayList) it.next();
            String sys = oneRow.get(0).toString();
            String seq = oneRow.get(1).toString();
            String chinese = oneRow.get(2).toString();
            String english = oneRow.get(3).toString();
            String design = oneRow.get(4).toString();
            String remark = oneRow.get(9).toString();
            String sub = oneRow.get(5).toString();
            String location = oneRow.get(6).toString();
            String yon = oneRow.get(7).toString();
            String another = oneRow.get(8).toString();
            Accsystem sysData;
            Subsystem subData;
            MoreThanNine mtnData;
            Location locationData;
            Device designData;
            DeviceSubsystemLocation dslData;
            InsertAPI in = new InsertAPI();
            if (in.getSystem(sys) == null) {
                in.setSystem(sys);
            }
            if (in.getSubsystem(sub) == null) {
                in.setSubsystem(sub);
            }
            if (in.queryByYesOrNo(yon, another) == null) {
                in.setMoreThanNine(yon, another);
            }
            if (in.getLocation(yon, another, location) == null) {
                mtnData = in.queryByYesOrNo(yon, another);
                in.setLocation(mtnData, location);
            }
            if (in.getDeviceBy(sys, seq, chinese, english, design, remark) == null) {
                sysData = in.getSystem(sys);
                in.setDevice(sysData, seq, chinese, english, design, remark);
            }
            
            subData = in.getSubsystem(sub);
            locationData = in.getLocation(yon, another, location);
            designData = in.getDeviceBy(sys, seq, chinese, english, design, remark);
            in.updateDeviceSubsystemLocation(designData, subData, locationData, dslId);
//            if (in.getDeviceSubsystemLocationBy(sys, seq, chinese, english, design, remark, sub, location, yon, another) == null) {
//                in.updateDeviceSubsystemLocation(designData, subData, locationData, dslId);
//            }
        }
    }
}
